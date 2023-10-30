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
 * Test class for CourseUnit class.
 * 
 * @author pekkanokelainen
 */
public class CourseUnitTest {
    /**
     * Test of json constructor.
     */
    @Test
    public void testCourseUnitConstructor() {
        String jsonStr = "{\"id\":\"course1\",\"name\":\"Test Course Unit\","
                + "\"groupId\":\"Group1\", \"minCredits\":\"5\"}";
        JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();

        CourseUnit courseUnit = new CourseUnit(json);
        assertEquals("course1", courseUnit.getId());
        assertEquals("Test Course Unit", courseUnit.getName());
    }

    /**
     * Test of getName method, of class CourseUnit.
     */
    @Test
    public void testGetName() {
        CourseUnit instance = new CourseUnit("Test Course", "TC101", 
        "Group1", 5);
        String expResult = "Test Course";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getId method, of class CourseUnit.
     */
    @Test
    public void testGetId() {
        CourseUnit instance = new CourseUnit("Test Course", "TC101", 
        "Group1", 5);
        String expResult = "TC101";
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getGroupId method, of class CourseUnit.
     */
    @Test
    public void testGetGroupId() {
        CourseUnit instance = new CourseUnit("Test Course", "TC101", 
        "Group1", 5);
        String expResult = "Group1";
        String result = instance.getGroupId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMinCredits method, of class CourseUnit.
     */
    @Test
    public void testGetMinCredits() {
        CourseUnit instance = new CourseUnit("Test Course", "TC101", 
        "Group1", 5);
        int expResult = 5;
        int result = instance.getMinCredits();
        assertEquals(expResult, result);
    }
}
