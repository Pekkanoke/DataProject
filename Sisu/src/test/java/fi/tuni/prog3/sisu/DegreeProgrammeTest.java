/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DegreeProgramme class.
 * 
 * @author pekkanokelainen
 */
public class DegreeProgrammeTest {
    /**
     * Tests if the class returns right values.
     */
    @Test
    public void testDegreeProgramme() {
        String jsonStr = "{ \"name\": { \"fi\": \"Test Degree Programme\", \"en\": \"Test Degree Programme\" }, \"id\": \"DP101\", \"groupId\": \"Group1\", \"rule\": { \"credits\": { \"min\": 180 } } }";
        JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
        DegreeProgramme instance = new DegreeProgramme(json);

        String expectedName = "Test Degree Programme";
        String expectedId = "DP101";
        String expectedGroupId = "Group1";
        int expectedMinCredits = 180;

        assertEquals(expectedName, instance.getName());
        assertEquals(expectedId, instance.getId());
        assertEquals(expectedGroupId, instance.getGroupId());
        assertEquals(expectedMinCredits, instance.getMinCredits());
    }
}
