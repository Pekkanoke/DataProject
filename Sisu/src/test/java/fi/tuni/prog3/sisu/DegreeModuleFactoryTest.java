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
 * Test class for the DegreeModuleFactory class that creates the whole class
 * structure.
 * 
 * @author pekkanokelainen
 */
public class DegreeModuleFactoryTest {
    
    private JsonObject degreeProgrammeJson;
    private JsonObject studyModuleJson;

    /**
     * Test of createDegreeModuleFromJson method, of class DegreeModuleFactory.
     */
    @Test
    public void testCreateDegreeModuleFromJson() {
        degreeProgrammeJson = JsonParser.parseString("{\"type\":\"DegreeProgramme\",\"name\":\"Degree Programme\",\"id\":\"degree_programme_id\",\"groupId\":\"degree_programme_group_id\",\"rule\":{\"credits\":{\"min\":180}}}")
                .getAsJsonObject();
        studyModuleJson = JsonParser.parseString("{\"type\":\"StudyModule\",\"name\":\"Study Module\",\"id\":\"study_module_id\",\"groupId\":\"study_module_group_id\",\"rule\":{\"credits\":{\"min\":60}}}")
                .getAsJsonObject();
        
        DegreeModule degreeProgramme = DegreeModuleFactory.createDegreeModuleFromJson(degreeProgrammeJson);
        assertTrue(degreeProgramme instanceof DegreeProgramme);
        assertEquals("Degree Programme", degreeProgramme.getName());
        assertEquals("degree_programme_id", degreeProgramme.getId());
        assertEquals("degree_programme_group_id", degreeProgramme.getGroupId());
        assertEquals(180, degreeProgramme.getMinCredits());

        DegreeModule studyModule = DegreeModuleFactory.createDegreeModuleFromJson(studyModuleJson);
        assertTrue(studyModule instanceof StudyModule);
        assertEquals("Study Module", studyModule.getName());
        assertEquals("study_module_id", studyModule.getId());
        assertEquals("study_module_group_id", studyModule.getGroupId());
        assertEquals(60, studyModule.getMinCredits());
    } 
}
