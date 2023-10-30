/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for Student class.
 * 
 * @author pekkanokelainen
 */
public class StudentTest {
    private Student student;

    /**
     * Tests adding and getting credits.
     */
    @Test
    public void testAddCredit() {
        student = new Student(123, "John Doe");
        student.addCredit("Course1");
        student.addCredit("Course2");

        ArrayList<String> credits = student.getCredits();
        assertNotNull(credits);
        assertEquals(2, credits.size());
        assertEquals("Course1", credits.get(0));
        assertEquals("Course2", credits.get(1));
    }

    /**
     * Tests getId method.
     */
    @Test
    public void testGetId() {
        student = new Student(123, "John Doe");
        assertEquals(123, student.getId());
    }

    /**
     * Tests the getName method.
     */
    @Test
    public void testGetName() {
        student = new Student(123, "John Doe");
        assertEquals("John Doe", student.getName());
    }
}