
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpellCorrector {

    final private CorpusReader cr;
    final private ConfusionMatrixReader cmr;

    final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz'".toCharArray();

    public final int WORD_SMOOTHING_VALUE;
    public final double BIGRAM_SMOOTHING_VALUE;
    public final double WORD_WEIGHT;
    public final double WORD_WEIGHT_TRANSPOSE;

    public SpellCorrector(CorpusReader cr, ConfusionMatrixReader cmr) {
        this.cr = cr;
        this.cmr = cmr;
        WORD_SMOOTHING_VALUE = 2;
        BIGRAM_SMOOTHING_VALUE = 0.00002d;
        WORD_WEIGHT = 0.66;
        WORD_WEIGHT_TRANSPOSE = 1 / WORD_WEIGHT;
    }

    public SpellCorrector(CorpusReader cr, ConfusionMatrixReader cmr, int wordSmoothingValue, double bigramSmoothingValue, double wordWeight) {
        this.cr = cr;
        this.cmr = cmr;
        WORD_SMOOTHING_VALUE = wordSmoothingValue;
        BIGRAM_SMOOTHING_VALUE = bigramSmoothingValue;
        WORD_WEIGHT = wordWeight;
        WORD_WEIGHT_TRANSPOSE = 1 / WORD_WEIGHT;
    }

    /**
     * Corrects the phrase given.
     *
     * @param phrase the phrase that has to be corrected.
     * @return Corrected phrase.
     */
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

    /**
     * Class in which result evaluations are executed.
     *
     * In here you can find: the original phrase, the currently suggested
     * phrase, the likelihoods of the words in the suggested phrase, the sum of
     * the likelihoods, the best phrase encountered yet, and the sum of the
     * likelihoods for the best phrase.
     */
    private class IntermediatePhrase {

        private final ArrayList<Map<String, Double>> candidates;
        private final String[] originalPhrase;

        private final String[] suggestionPhrase;
        private final double[] likelihoods;
        private double likelihoodSum;

        private String[] bestPhrase;
        private double bestLikelihoodSum;

        /**
         * Constructor.
         *
         * @param phrase the words of the phrase.
         * @param candidates list of information about the candidate words.
         */
        private IntermediatePhrase(String[] phrase, ArrayList<Map<String, Double>> candidates) {
            this.candidates = candidates;
            this.originalPhrase = phrase.clone();

            //set original phrase as suggested phrase.
            this.suggestionPhrase = phrase.clone();
            this.likelihoods = new double[phrase.length];

            //calculate the likelihood of all the words.
            this.likelihoodSum = 0;
            for (int i = 0; i < suggestionPhrase.length; i++) {
                likelihoods[i] = getLikelihoodOfWordAt(i);
                likelihoodSum += likelihoods[i];
            }

            //set temporary best phrase.
            this.bestPhrase = phrase.clone();
            this.bestLikelihoodSum = likelihoodSum;
        }

        /**
         * Restore the i'th word.
         *
         * @param i index of the word you want to reset.
         */
        private void restore(int i) {
            //reset the i'th word.
            suggestionPhrase[i] = originalPhrase[i];

            //recalculate probability.
            recalculateLikelihoodOfWordAt(i);
        }

        /**
         * Evaluate the phrase with the given word at the i'th position.
         *
         * @param i desired index.
         * @param word desired word.
         */
        private void evaluate(int i, String word) {
            //set the i'th word to the specified word.
            suggestionPhrase[i] = word;

            //recalculate probability, and if better, change best suggestion.
            if (recalculateLikelihoodOfWordAt(i)) {
                bestPhrase = suggestionPhrase.clone();
                this.bestLikelihoodSum = likelihoodSum;
            }
        }

        /**
         * Converts the best phrase found to a string.
         *
         * @return the answer.
         */
        private String getFinalSuggestion() {
            //take the best phrase array, and convert it to a string.
            return String.join(" ", bestPhrase);
        }

        /**
         * Recalculate the likelihood of the desired word.
         *
         * @param i index of the suggested word.
         * @return whether the word is better or not than the original.
         */
        private boolean recalculateLikelihoodOfWordAt(int i) {
            //as we use log probability, we can do addition instead of multiplication.
            //this is useful, as we can easily calculate the new situation
            //by using the difference between the likelihoods.
            //calculate the difference.
            double difference = -likelihoods[i];
            likelihoods[i] = getLikelihoodOfWordAt(i);
            difference += likelihoods[i];

            //this word is used at the next word as well.
            //if we are at the last word, skip this part.
            if (i < suggestionPhrase.length - 1) {
                difference -= likelihoods[i + 1];
                likelihoods[i + 1] = getLikelihoodOfWordAt(i + 1);
                difference += likelihoods[i + 1];
            }

            //Check if the likelyhood sum is still usable.
            if (Double.isFinite(likelihoodSum)) {
                likelihoodSum += difference;
            } else {
                //recalculate.
                likelihoodSum = 0;
                for (double d : likelihoods) {
                    likelihoodSum += d;
                }
            }

            //the likelihood is negative. So a bigger likelihood is better.
            return likelihoodSum > bestLikelihoodSum;
        }

        /**
         * Calculates the log probability of the word.
         *
         * @param i index of the word in the suggestion list.
         * @return likelihood of the i'th word in the suggestion.
         */
        private double getLikelihoodOfWordAt(int i) {
            //the word we are looking at.
            String word = suggestionPhrase[i];

            double probability;

            //if the word is not in the vocabulary, we have to replace it.
            if (!cr.inVocabulary(word)) {
                probability = Math.log(Double.MIN_VALUE);
            } else {
                //cannot multiply something that isn't present.
                if (i == 0) {
                    //nothing to multiply with, so make it 1.
                    probability = Math.log(1d);
                } else {
                    //calculate the probability of P(vw|v). Use smoothening for this.
                    //Also take the logarithm, so that we can use addition.
                    probability = WORD_WEIGHT_TRANSPOSE * Math.log(addNSmoothing(suggestionPhrase[i - 1], word, WORD_SMOOTHING_VALUE, BIGRAM_SMOOTHING_VALUE));
                }

                //if the word is not the original word, an error has been corrected.
                if (!word.equals(originalPhrase[i])) {
                    //so multiply by that error chance.
                    //Also take the logarithm, so that we can use addition.
                    probability += WORD_WEIGHT * Math.log(candidates.get(i).get(word));
                }
            }

            //we try to avoid infinity at any cost in our answers. 
            //So if infinite, change it to the minimal value of a double.
            /*if (Double.isInfinite(Math.log(probability))) {
                probability = Double.MIN_VALUE;
            }*/
            //return the likelihood.
            return probability;
        }
    }

    public double addOneSmoothing(String previous, String current) {
        return addNSmoothing(previous, current, 1, 1);
    }

    public double addTwoSmoothing(String previous, String current) {
        return addNSmoothing(previous, current, 2, 2);
    }

    public double addOneSmoothingNoBigram(String previous, String current) {
        return addNSmoothing(previous, current, 1, 0);
    }

    public double addTwoSmoothingNoBigram(String previous, String current) {
        return addNSmoothing(previous, current, 2, 0);
    }

    public double addOneSmoothingSmallBigram(String previous, String current) {
        return addNSmoothing(previous, current, 1, 0.00001d);
    }

    public double addTwoSmoothingSmallBigram(String previous, String current) {
        return addNSmoothing(previous, current, 2, 0.00002d);
    }

    public double addNSmoothing(String previous, String current, int n, double b) {
        return (cr.getSmoothedCount(previous + " " + current, n, b)) / (cr.getSmoothedCount(previous) + n * cr.getNGramCount());
    }

    public double getGTSmoothing(String previous, String current) {
        int c = cr.getNGramCount(previous + " " + current);

        double temp = 0;
        if (c > 0) {
            //Nc
            double nc = cr.getFrequencyOfFrequency(c);

            //Nc+1
            double ncp = cr.getFrequencyOfFrequency(c + 1);

            temp = (c + 1) * ((ncp + 1) / (nc + 1));
        } else {
            //N1
            temp = cr.getFrequencyOfFrequency(1);

        }

        temp *= cr.getGoodTuringNDiv();

        return temp;
    }

    /**
     * returns a map with candidate words and their noisy channel probability.
     *
     *
     * @param word the word to get candidate words for
     * @return a map with candidate words and their noisy channel probability
     */
    public Map<String, Double> getCandidateWords(String word) {
        //map of candidates.
        Map<String, Double> candidates = new HashMap<>();

        //the new word
        String candidate;

        //the part of the word that is considered "wrong"
        String error;

        //the correction of the "wrong" part of the word.
        String correction;

        // generate insertions
        for (int i = 0; i <= word.length(); i++) {
            //insert a char at the i'th position, shift everything else.
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
            //leave out i'th char in the word.
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
            //switch i and i + 1.
            candidate = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2);
            error = "" + word.charAt(i) + word.charAt(i + 1);
            correction = "" + word.charAt(i + 1) + word.charAt(i);
            addCandidate(candidate, error, correction, candidates);
        }

        // generate substitutions
        for (int i = 0; i < word.length(); i++) {
            //simply replace each character in the word with another.
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

    /**
     * Determines if a candidate has to be added, and adds it if applicable.
     *
     * @param candidate the candidate word.
     * @param error the part of the original word that was wrong.
     * @param correction the correction of the wrong part of the original word.
     * @param candidates list with all information about the candidates.
     */
    public void addCandidate(String candidate, String error, String correction, Map<String, Double> candidates) {
        //we only want replacements that are in the vocabulary.
        if (cr.inVocabulary(candidate)) {
            //calculate the probability that the error is made.
            double errorP = cmr.getErrorProbability(error, correction);

            //calculate the probability that the word is chosen.
            double wordP = cr.getWordProbability(candidate);

            //get the total probability.
            double p = errorP * wordP + candidates.getOrDefault(candidate, 0d);

            //store the new total probability.
            candidates.put(candidate, p);
        }
    }
}
