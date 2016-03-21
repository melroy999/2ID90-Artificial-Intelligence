
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpellCorrector {

    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;

    final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz'".toCharArray();

    public SpellCorrector(CorpusReader cr, ConfusionMatrixReader cmr) {
        this.cr = cr;
        this.cmr = cmr;
    }

    public String correctPhrase(String phrase) {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException("phrase must be non-empty.");
        }

        String[] words = phrase.split(" ");

        ArrayList<Map<String, Double>> wordCandidates = new ArrayList<>();

        for (String word : words) {
            Map<String, Double> candidates = getCandidateWords(word);
            wordCandidates.add(candidates);

            /*for(String key : candidates.keySet()){
                System.out.println("word: " + key +", prob: " + candidates.get(key));
            }*/
        }

        IntermediatePhrase intermediate = new IntermediatePhrase(words, wordCandidates);
        //it is extremely important to evaluate the original state as well,
        //as the phrase might not have an error.

        //we have at most 2 errors in one sentence. Thus we make a nested for loop.
        //here i is the i'th word in the sentence.
        //it is the first word that is marked as an error.
        for (int i = 0; i < words.length; i++) {
            //iterate over all correction candidates.
            for (String iCandidate : wordCandidates.get(i).keySet()) {
                //evaluate the word.
                intermediate.evaluate(i, iCandidate);

                //mispelled words are never consecutive. So skip one index.
                for (int j = i + 2; j < words.length; j++) {
                    for (String jCandidate : wordCandidates.get(j).keySet()) {
                        //evaluate the word.
                        intermediate.evaluate(j, jCandidate);
                    }
                    //Restore j'th word.
                    intermediate.restore(j);
                }
            }
            //Restore i'th word.
            intermediate.restore(i);
        }

        return intermediate.getFinalSuggestion().trim();
    }

    private class IntermediatePhrase {

        private final ArrayList<Map<String, Double>> candidates;
        private final String[] originalPhrase;

        private final String[] suggestionPhrase;
        private final double[] likelihoods;
        private double aggLikelihood;

        private String[] bestPhrase;
        private double bestAggLikelihood;

        private IntermediatePhrase(String[] phrase, ArrayList<Map<String, Double>> candidates) {
            this.candidates = candidates;
            this.originalPhrase = phrase.clone();

            this.suggestionPhrase = phrase.clone();
            this.likelihoods = new double[phrase.length];

            this.aggLikelihood = 0;
            System.out.println("Listing intermediate likelihoods: ");
            for (int i = 0; i < suggestionPhrase.length; i++) {
                likelihoods[i] = getLikelyhoodOfWordAt(i);
                aggLikelihood += likelihoods[i];
                System.out.println(likelihoods[i]);
            }
            
            System.out.println("Likelihood: " + aggLikelihood);

            this.bestPhrase = phrase.clone();
            this.bestAggLikelihood = aggLikelihood;
        }

        private void restore(int i) {
            suggestionPhrase[i] = originalPhrase[i];
            //recalculate probability.
            recalculateLikelihoodOfWordAt(i);
        }

        private void evaluate(int i, String word) {
            suggestionPhrase[i] = word;
            
            //recalculate probability, and if better, store.
            if(recalculateLikelihoodOfWordAt(i)){
                bestPhrase = suggestionPhrase.clone();
                this.bestAggLikelihood = aggLikelihood;
                System.out.println("Likelihood: " + aggLikelihood);
            }
        }

        private String getFinalSuggestion() {
            if(Double.isInfinite(bestAggLikelihood)){
                System.out.println("Answer is not trustable!");
            }
            return String.join(" ", bestPhrase);
        }

        private boolean recalculateLikelihoodOfWordAt(int i) {
            double difference = -likelihoods[i];
            likelihoods[i] = getLikelyhoodOfWordAt(i);
            difference += likelihoods[i];

            //this word is used at the next word as well.
            //if we are at the last word, skip this part.
            if (i < suggestionPhrase.length - 1) {
                difference -= likelihoods[i + 1];
                likelihoods[i + 1] = getLikelyhoodOfWordAt(i + 1);
                difference += likelihoods[i + 1];
            }

            //Check if the likelyhood sum is still usable.
            if(Double.isFinite(aggLikelihood)){
                aggLikelihood += difference;
            } else {
                //recalculate.
                aggLikelihood = 0;
                for(double d : likelihoods){
                    aggLikelihood += d;
                }
            }
            
            return aggLikelihood > bestAggLikelihood;
        }

        private double getLikelyhoodOfWordAt(int i) {
            String word = suggestionPhrase[i];

            double probability;
            if (!cr.inVocabulary(word)) {
                probability = 0d;
            } else {
                //cannot multiply something that isn't present.
                if (i == 0) {
                    probability = 1d;
                } else {
                    probability = cr.getSmoothedCount(suggestionPhrase[i - 1] + " " + word) / cr.getSmoothedCount(suggestionPhrase[i - 1]);
                }

                if (!word.equals(originalPhrase[i])) {
                    probability *= candidates.get(i).get(word);
                } 
            }
            
            if(Double.isInfinite(Math.log(probability))){
                System.out.println(word + " is infinite!");
                System.out.println("Probability value of " + probability);
            }

            return Math.log(probability);
        }
    }

    /**
     * returns a map with candidate words and their noisy channel probability.
     *
     *
     * @param word the word to get candidate words for
     * @return a map with candidate words and their noisy channel probability
     */
    public Map<String, Double> getCandidateWords(String word) {
        Map<String, Double> candidates = new HashMap<>();

        String candidate;
        String error;
        String correction;

        // generate insertions
        for (int i = 0; i <= word.length(); i++) {
            for (char c : ALPHABET) {
                candidate = word.substring(0, i) + c + word.substring(i);
                if (i != 0) {
                    error = "" + word.charAt(i - 1);
                } else {
                    error = " ";
                }
                correction = error + c;
                addCandidate(candidate, error, correction, candidates);
            }
        }

        // generate deletions
        for (int i = 0; i < word.length(); i++) {
            candidate = word.substring(0, i) + word.substring(i + 1);
            if (i != 0) {
                correction = "" + word.charAt(i - 1);
            } else {
                correction = " ";
            }
            error = correction + word.charAt(i);
            addCandidate(candidate, error, correction, candidates);
        }

        // generate transpositions
        for (int i = 0; i < word.length() - 1; i++) {
            candidate = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 1);
            error = "" + word.charAt(i) + word.charAt(i + 1);
            correction = "" + word.charAt(i + 1) + word.charAt(i);
            addCandidate(candidate, error, correction, candidates);
        }

        // generate substitutions
        for (int i = 0; i < word.length(); i++) {
            for (char c : ALPHABET) {
                char[] wordA = word.toCharArray();
                wordA[i] = c;
                candidate = new String(wordA);
                error = "" + word.charAt(i);
                correction = "" + c;
                addCandidate(candidate, error, correction, candidates);
            }
        }

        //we don't want to suggest the problem as a solution, obviously.
        candidates.remove(word);

        return candidates;
    }

    private final static int HIGH_POWER = (int) Math.pow(10, 9);

    public void addCandidate(String candidate, String error, String correction, Map<String, Double> candidates) {
        if (cr.inVocabulary(candidate)) {
            /*System.out.println("> " + candidate);*/

            double errorP = cmr.getErrorProbability(error, correction);
            double wordP = cr.getWordProbability(candidate);

            double p
                    = errorP
                    * wordP /** HIGH_POWER*/
                    + candidates.getOrDefault(candidate, 0d);

            if(p > 1){
                System.out.println("WHAT?!");
                p = 1;
            }
            
            /*System.out.println("noise chance: " + errorP
                    * wordP);
            System.out.println("noise aggrigated: " + p);*/
            candidates.put(candidate, p);
        }
    }
}
