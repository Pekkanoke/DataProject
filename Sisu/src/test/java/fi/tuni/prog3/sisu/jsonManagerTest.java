/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests jsonManager class.
 * 
 * @author pekkanokelainen
 */
public class jsonManagerTest {
    private jsonManager manager;

    /**
     * Tests if addStudent, findStudent and other get methods.
     */
    @Test
    public void testAddAndFindStudent() {
        manager = new jsonManager();
        Student student = new Student(123, "John Doe");
        manager.addStudent(student);

        Student foundStudent = manager.findStudent("John Doe", 123);
        assertNotNull(foundStudent);
        assertEquals(student.getName(), foundStudent.getName());
        assertEquals(student.getId(), foundStudent.getId());
    }

    /**
     * Tests read and write to fiel methods.
     * 
     * @param tempDir path of the file.
     * @throws Exception
     */
    @Test
    public void testReadAndWriteToFile(@TempDir Path tempDir) throws Exception {
        manager = new jsonManager();
        Student student = new Student(123, "John Doe");
        student.addCredit("Course1");
        student.changeDegreeProgramme("DegreeProgramme1");
        manager.addStudent(student);

        File tempFile = tempDir.resolve("students.json").toFile();
        String tempFilePath = tempFile.getAbsolutePath();
        
        assertTrue(manager.writeToFile(tempFilePath));

        jsonManager newManager = new jsonManager();
        assertTrue(newManager.readFromFile(tempFilePath));

        Student foundStudent = newManager.findStudent("John Doe", 123);
        assertNotNull(foundStudent);
        assertEquals(student.getName(), foundStudent.getName());
        assertEquals(student.getId(), foundStudent.getId());
        assertEquals(student.getCredits(), foundStudent.getCredits());
        assertEquals(student.getDegreeProgramme(), foundStudent.getDegreeProgramme());
    }
}