/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.net.httpserver.Authenticator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Map;
import java.util.ArrayList;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;

/**
 *
 * @author Administrator
 */
public class SpellCorrectorTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    public SpellCorrectorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {

        FileInputStream fis;
        fis = new FileInputStream("test sentences custom.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        ArrayList<String> variationsList = new ArrayList<>();
        while (in.ready()) {
            String input = in.readLine();
            if (input == "end") {
                continue;
            }
            if (input.trim().isEmpty()) {
                if (!variationsList.isEmpty()) {
                    testSentences.add(variationsList);
                }
                variationsList = new ArrayList<>();
                continue;
            }

            variationsList.add(input);
        }
    }

    @After
    public void tearDown() {
    }

    ArrayList<ArrayList<String>> testSentences = new ArrayList<>();

    /**
     * Test of getCandidateWords method, of class SpellCorrector.
     */
    @Test
    public void testGetCandidateWords() throws IOException {
        System.out.println("getCandidateWords");
        String word = "idabetes";
        CorpusReader cr = new CorpusReader();
        ConfusionMatrixReader cmr = new ConfusionMatrixReader();
        SpellCorrector sc = new SpellCorrector(cr, cmr);
        Map<String, Double> result = sc.getCandidateWords(word);
        for (String key : result.keySet()) {
            System.out.println(key + ", " + result.get(key));
        }

        /*assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");*/
    }

    /*Variations of Shakespears famous words*/
    @Test
    public void testToBeOrNotToBe() throws IOException {
        System.out.println("testing 'To be or not to be' variations");
        String[] sentences = new String[]{
            "to be or not to be", // correct sentence
            "to ben or not to be",
            "ro be ot not to be",
            "to be or nod to bee"};

        CorpusReader cr = new CorpusReader();
        ConfusionMatrixReader cmr = new ConfusionMatrixReader();
        SpellCorrector sc = new SpellCorrector(cr, cmr);
        for (String s : sentences) {
            String output = sc.correctPhrase(s);
            System.out.println(String.format("Input \"%0$s\" returned \"%1$s\"", s, output));
            collector.checkThat("input sentence: " + s + ". ", "to be or not to be", IsEqual.equalTo(output));
        }
    }

    /*Variations of Shakespears famous words*/
    @Test
    public void testSentencesFromFile() throws IOException {
        System.out.print("Testing all sentences from file");
        int countCorrect = 0;
        int countTestedSentences = 0;

        for (ArrayList<String> sentences : testSentences) {
            String correctSentence = sentences.get(0);
            System.out.println("testing '" + correctSentence + "' variations");
            CorpusReader cr = new CorpusReader();
            ConfusionMatrixReader cmr = new ConfusionMatrixReader();
            SpellCorrector sc = new SpellCorrector(cr, cmr);
            int countSub = 0;
            for (String s : sentences) {
                String output = sc.correctPhrase(s);
                countSub += correctSentence.equals(output) ? 1 : 0;
                System.out.println(String.format("Input \"%1$s\" returned \"%2$s\", equal: %3$b", s, output, correctSentence.equals(output)));
                collector.checkThat("input sentence: " + s + ". ", output, IsEqual.equalTo(correctSentence));
                countTestedSentences++;
            }
            System.out.println(String.format("Correct answers for '%3$s': (%1$2d/%2$2d)", countSub, sentences.size(), correctSentence));
            System.out.println();
            countCorrect += countSub;
        }

        System.out.println(String.format("Correct answers in total: (%1$2d/%2$2d)", countCorrect, countTestedSentences));
        System.out.println();
    }
}
