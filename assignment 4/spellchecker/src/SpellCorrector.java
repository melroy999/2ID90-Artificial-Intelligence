
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
        String finalSuggestion = "";

        for (String word : words) {
            Map<String, Double> candidates = getCandidateWords(word);
            for(String key : candidates.keySet()){
                System.out.println("word: " + key +", prob: " + candidates.get(key));
            }
        }

        /**
         * CODE TO BE ADDED *
         */
        return finalSuggestion.trim();
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
            System.out.println("> " + candidate);
            
            double errorP = cmr.getErrorProbability(error, correction);
            double wordP = cr.getWordProbability(candidate);

            double p
                    = errorP
                    * wordP * HIGH_POWER
                    + candidates.getOrDefault(candidate, 0d);

            System.out.println("noise chance: " + errorP
                    * wordP);
            System.out.println("noise aggrigated: " + p);

            candidates.put(candidate, p);
        }
    }
}
