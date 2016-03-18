/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class SpellCorrectorTest {
    
    public SpellCorrectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of correctPhrase method, of class SpellCorrector.
     */
    @Test
    public void testCorrectPhrase() {
        System.out.println("correctPhrase");
        String phrase = "";
        SpellCorrector instance = null;
        String expResult = "";
        String result = instance.correctPhrase(phrase);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCandidateWords method, of class SpellCorrector.
     */
    @Test
    public void testGetCandidateWords() {
        System.out.println("getCandidateWords");
        String word = "";
        SpellCorrector instance = null;
        Map<String, Double> expResult = null;
        Map<String, Double> result = instance.getCandidateWords(word);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
