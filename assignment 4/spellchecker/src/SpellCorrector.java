
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
        String finalSuggestion = "";

        /**
         * CODE TO BE ADDED *
         */
        return finalSuggestion.trim();
    }

    /**
     * returns a map with candidate words and their noisy channel probability. *
     * @param word the word to get candidate words for
     * @return a map with candidate words and their noisy channel probability
     */
    public Map<String, Double> getCandidateWords(String word) {
        Map<String, Double> mapOfWords = new HashMap<>();
        Map<String, Double> tempMapOfWords = new HashMap<>();

        // generate insertions
        Set<String> insertions = new HashSet<>();
        for (int i = 0; i <= word.length(); i++) {
            for (char c : ALPHABET) {
                String temp = word.substring(0, i) + c + word.substring(i);
                insertions.add(temp);
                double probability = 1d;/*replace with probability*/
                if (tempMapOfWords.getOrDefault(temp, -1d) < probability) {
                    tempMapOfWords.put(temp, probability);
                }
            }
        }

        insertions = cr.inVocabulary(insertions);
        for (String s : insertions) {
            mapOfWords.put(s, tempMapOfWords.get(s));
        }

        // generate deletions
        Set<String> deletions = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            String temp = word.substring(0, i) + word.substring(i + 1);
            deletions.add(temp);
            double probability = 1d;/*replace with probability*/
            if (tempMapOfWords.getOrDefault(temp, -1d) < probability) {
                tempMapOfWords.put(temp, probability);
            }
        }

        deletions = cr.inVocabulary(deletions);
        for (String s : deletions) {
            mapOfWords.put(s, tempMapOfWords.get(s));
        }

        // generate transpositions
        Set<String> transpositions = new HashSet<>();
        for (int i = 0; i < word.length() - 1; i++) {
            String temp = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 1);
            transpositions.add(temp);
            double probability = 1d;/*replace with probability*/
            if (tempMapOfWords.getOrDefault(temp, -1d) < probability) {
                tempMapOfWords.put(temp, probability);
            }
        }

        transpositions = cr.inVocabulary(transpositions);
        for (String s : transpositions) {
            mapOfWords.put(s, tempMapOfWords.get(s));
        }

        // generate substitutions
        Set<String> substitutions = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            for (char c : ALPHABET) {
                char[] wordA = word.toCharArray();
                wordA[i] = c;
                String temp = new String(wordA);
                substitutions.add(temp);
                double probability = 1d;/*replace with probability*/
                if (tempMapOfWords.getOrDefault(temp, -1d) < probability) {
                    tempMapOfWords.put(temp, probability);
                }
            }
        }
        substitutions = cr.inVocabulary(substitutions);
        for (String s : substitutions) {
            mapOfWords.put(s, tempMapOfWords.get(s));
        }

        return mapOfWords;
    }
}
