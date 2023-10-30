/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;

/**
 * Tests GroupingModule class.
 * 
 * @author pekkanokelainen
 */
public class GroupingModuleTest {
    /**
     * Tests constructor with a json object.
     */
    @Test
    void createGroupingModuleFromJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", "123");
        json.addProperty("groupId", "456");
        json.addProperty("name", "Test Grouping Module");

        JsonObject rule = new JsonObject();
        JsonObject credits = new JsonObject();
        credits.addProperty("min", 5);
        rule.add("credits", credits);
        json.add("rule", rule);

        GroupingModule groupingModule = new GroupingModule(json);
        assertEquals("Test Grouping Module", groupingModule.getName());
        assertEquals("123", groupingModule.getId());
        assertEquals("456", groupingModule.getGroupId());
        assertEquals(5, groupingModule.getMinCredits());
    }
}
