/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the DegreeModule class.
 * 
 * @author pekkanokelainen
 */
public class DegreeModuleTest {
    /**
     * This nested class extends the DegreeModule class and is used to test
     * its protected methods.
     */
    static class TestDegreeModule extends DegreeModule {
        public TestDegreeModule(String name, String id, String groupId, 
            int minCredits) {
            super(name, id, groupId, minCredits);
        }
    }

    /**
     * Test the getName() method of the DegreeModule class.
     */
    @Test
    public void testGetName() {
        DegreeModule instance = new TestDegreeModule("Degree Module 101", "DM101", "Group1", 30);
        String expResult = "Degree Module 101";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test the getId() method of the DegreeModule class.
     */
    @Test
    public void testGetId() {
        DegreeModule instance = new TestDegreeModule("Degree Module 101", "DM101", "Group1", 30);
        String expResult = "DM101";
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test the getMinCredits() method of the DegreeModule class.
     */
    @Test
    public void testGetMinCredits() {
        DegreeModule instance = new TestDegreeModule("Degree Module 101", "DM101", "Group1", 30);
        int expResult = 30;
        int result = instance.getMinCredits();
        assertEquals(expResult, result);
    }

    /**
     * Test the addChildModule(), addChildCourse(), getChildModules(), and
     * getChildCourses() methods of the DegreeModule class.
     */
    @Test
    public void testChildModulesAndCourses() {
        DegreeModule parentModule = new TestDegreeModule("Degree Module 101","DM101", "Group1", 30);
        DegreeModule childModule = new TestDegreeModule("Degree Module 102","DM102", "Group1", 20);
        CourseUnit course = new CourseUnit("Test Course", "TC101", "Group1", 5);

        parentModule.addChildModule(childModule);
        parentModule.addChildCourse(course);

        List<DegreeModule> childModules = parentModule.getChildModules();
        List<CourseUnit> childCourses = parentModule.getChildCourses();

        assertTrue(childModules.contains(childModule));
        assertTrue(childCourses.contains(course));

        parentModule.removeChildModule(childModule);
        parentModule.removeChildCourse(course);

        assertFalse(childModules.contains(childModule));
        assertFalse(childCourses.contains(course));
    }
}
