
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
        //set containing all possible words that can be derived from the given word.
        Set<String> foundWords = new HashSet<>();
        
        //iterate over all unique insertion positions, i.e. word.length + 1. .w.o.r.d.
        for (int i = 0; i < word.length() + 1; i++) {
            String candidate;

            //iterate over all characters in the alphabet.
            for (char c : ALPHABET) {
                //generate insertion.
                candidate = insertCharacter(word, i, c);
                foundWords.add(candidate);

                //generate substitution.
                candidate = substituteCharacter(word, i, c);
                foundWords.add(candidate);
            }

            //generate transposition.
            candidate = transposeCharacter(word, i);
            foundWords.add(candidate);
            
            //generate deletion.
            candidate = deleteCharacter(word, i);
            foundWords.add(candidate);
        }

        //get all words that are in the vocabulary. 
        Set<String> validWords = cr.inVocabulary(foundWords);
        
        //create a mapping between the valid words and the probability.
        Map<String, Double> mapOfWords = new HashMap<>();

        //for all words that are in the vocabulary.
        for (String key : validWords) {
            //get the probability of the error being made.
            double probability = getNoisyChannelProbability(word, key);
            mapOfWords.put(key, probability);
        }

        return mapOfWords;
    }
    
    // <editor-fold desc="Character operations" defaultstate="collapsed">
    private String insertCharacter(String word, int i, char c) {
        return word.substring(0, i) + c + word.substring(i);
    }

    private String transposeCharacter(String word, int i) {
        return word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 1);
    }

    private String substituteCharacter(String word, int i, char c) {
        char[] wordA = word.toCharArray();
        wordA[i] = c;
        return new String(wordA);
    }

    private String deleteCharacter(String word, int i) {
        return word.substring(0, i) + word.substring(i + 1);
    }
    // </editor-fold>

    public double getNoisyChannelProbability(String word, String candidate) {
        return Double.NaN;
    }
}
