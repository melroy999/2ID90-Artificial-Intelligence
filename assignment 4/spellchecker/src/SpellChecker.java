
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SpellChecker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean inPeach = false; // set this to true if you submit to peach!!!

        try {
            if (inPeach) {
                CorpusReader cr = new CorpusReader();
                ConfusionMatrixReader cmr = new ConfusionMatrixReader();
                SpellCorrector sc = new SpellCorrector(cr, cmr);
                peachTest(sc);
            } else {
                PrintWriter writer = new PrintWriter("testResults.csv", "UTF-8");
                PrintWriter log = new PrintWriter("testResults.txt", "UTF-8");
                writer.println("sep=;");

                for (int i = 0; i < 10; i++) {
                    for (double j = 1; j < Math.pow(2, 18); j *= 2) {
                        CorpusReader cr = new CorpusReader();
                        ConfusionMatrixReader cmr = new ConfusionMatrixReader();
                        SpellCorrector sc = new SpellCorrector(cr, cmr, i, 1 / j);
                        nonPeachTest(sc, writer, log);
                    }
                }

                writer.close();

            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    static void nonPeachTest(SpellCorrector sc, PrintWriter writer, PrintWriter log) throws IOException {

        String[] sentences = {
            //peach tests
            "this assay allowed us to measure a wide variety of conitions",
            "at the hme locations there were traces of water",
            "this assay allowed us to meassure a wide variety of conditions",
            "this assay allowed us to measure a wide vareity of conditions",
            "at the hoome locations there were traces of water",
            "at the home locasions there were traces of water",
            "the development of diabetes is present in moce that carry a transgen",
            "the development of idabetes is present in mice that carry a transgen",
            "the development of diabetes us present in mice that harry a transgene",
            "boxing glowes shield the knockles not the head",
            "boxing loves shield the knuckles nots the head",
            "boing gloves shield the knuckles nut the head",
            "she still refers to me has a friend but i fel i am treated quite badly",
            "she still refers to me as a friendd but i feel i am traeted quite badly",
            "she still refers too me as a friend but i feel i am treated quite batly",
            "she still refers to me as a fiend but i feel i am treated quite badly",
            "a repsonse may be any measurable biological parameter that is correlated witth the toxicant",
            "a response may be any measurable biological prameter that is corelated with the toxicant",
            "a respunse may be any measurable biologecal parameter that is correlated with the toxicant",
            "a responses may be any measurable biological parametre that is correlated with the toxicant",
            "esentially there has been no change in japan",
            "essentially there has been no change in japan",
            "essentially here has bien no change in japan",
            "this advise is taking into consideration the fact that the govenrment bans political parties",
            "this advices is taking into consideration the fact that the government bans political parties",
            "this addvice is taking into consideration the fact that the goverment bans political parties",
            "ancient china was one of the longst lasting societies iin the history of the world",
            "ancient china was one of the longest lasting sosieties in the history of the world",
            "anicent china was one of the longest lasting societties in the history of the world",
            "ancient china wqs one of the longest lasting societies in the histori of the world",
            "laying in the national footbal league was my dream",
            "playing in the national football laegue was my draem",
            "playing in the national fotball league was my dream"
        };

        String[] correctSentences = {
            //peach tests
            "this assay allowed us to measure a wide variety of conditions",
            "at the home locations there were traces of water",
            "this assay allowed us to measure a wide variety of conditions",
            "this assay allowed us to measure a wide variety of conditions",
            "at the home locations there were traces of water",
            "at the home locations there were traces of water",
            "the development of diabetes is present in mice that carry a transgene",
            "the development of diabetes is present in mice that carry a transgene",
            "the development of diabetes is present in mice that carry a transgene",
            "boxing gloves shield the knuckles not the head",
            "boxing gloves shield the knuckles not the head",
            "boxing gloves shield the knuckles not the head",
            "she still refers to me as a friend but i feel i am treated quite badly",
            "she still refers to me as a friend but i feel i am treated quite badly",
            "she still refers to me as a friend but i feel i am treated quite badly",
            "she still refers to me as a friend but i feel i am treated quite badly",
            "a response may be any measurable biological parameter that is correlated with the toxicant",
            "a response may be any measurable biological parameter that is correlated with the toxicant",
            "a response may be any measurable biological parameter that is correlated with the toxicant",
            "a response may be any measurable biological parameter that is correlated with the toxicant",
            "essentially there has been no change in japan",
            "essentially there has been no change in japan",
            "essentially there has been no change in japan",
            "this advice is taking into consideration the fact that the government bans political parties",
            "this advice is taking into consideration the fact that the government bans political parties",
            "this advice is taking into consideration the fact that the government bans political parties",
            "ancient china was one of the longest lasting societies in the history of the world",
            "ancient china was one of the longest lasting societies in the history of the world",
            "ancient china was one of the longest lasting societies in the history of the world",
            "ancient china was one of the longest lasting societies in the history of the world",
            "playing in the national football league was my dream",
            "playing in the national football league was my dream",
            "playing in the national football league was my dream"
        };

        int counter = 0;
        String resultInfo = "\n";
        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i];
            String result = sc.correctPhrase(sentence);

            if (result.equals(correctSentences[i])) {
                counter++;
            } else {
                resultInfo += "Test " + i + ": \n";
                resultInfo += "Input :  " + sentence + "\n";
                resultInfo += "Answer:  " + result + "\n";
                resultInfo += "Correct: " + correctSentences[i] + "\n\n";
            }
        }

        writer.print("Score: " + counter + "/" + sentences.length + "; With: WS: " + sc.WORD_SMOOTHING_VALUE + " and BS: " + sc.BIGRAM_SMOOTHING_VALUE + ";");
        writer.print("\n");
        
        log.println("##################################################################");
        log.println("Score: " + counter + "/" + sentences.length + "; With: WS: " + sc.WORD_SMOOTHING_VALUE + " and BS: " + sc.BIGRAM_SMOOTHING_VALUE + ";");
        log.println(resultInfo);
        
        System.out.println("##################################################################");
        System.out.println("Score: " + counter + "/" + sentences.length + " With: WS: " + sc.WORD_SMOOTHING_VALUE + " and BS: " + sc.BIGRAM_SMOOTHING_VALUE);
        System.out.println(resultInfo);
    }

    static void peachTest(SpellCorrector sc) throws IOException {
        Scanner input = new Scanner(System.in);

        String sentence = input.nextLine();
        System.out.println("Answer: " + sc.correctPhrase(sentence));
    }
}
