/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonObject;

/**
 * Tests the study module class.
 * 
 * @author pekkanokelainen
 */
public class StudyModuleTest {
    /**
     * Tests constructing, adding and getting methods.
     */
    @Test
    void createStudyModule() {
        JsonObject json = new JsonObject();
        json.addProperty("id", "123");
        json.addProperty("groupId", "456");
        json.addProperty("name", "Test Study Module");

        JsonObject rule = new JsonObject();
        JsonObject credits = new JsonObject();
        credits.addProperty("min", 5);
        rule.add("credits", credits);
        json.add("rule", rule);

        StudyModule studyModule = new StudyModule(json);
        assertEquals("Test Study Module", studyModule.getName());
        assertEquals("123", studyModule.getId());
        assertEquals("456", studyModule.getGroupId());
        assertEquals(5, studyModule.getMinCredits());
    }
    
}
