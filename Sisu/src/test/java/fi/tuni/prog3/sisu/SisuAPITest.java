/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SisuAPI class.
 * 
 * @author pekkanokelainen
 */
public class SisuAPITest {

    /**
     * Test of getJsonObjectFromApi method, of class SisuAPI.
     */
    @Test
    void getJsonObjectFromApi() {
        SisuAPI sisuAPI = new SisuAPI();
        JsonObject jsonObject = sisuAPI.getJsonObjectFromApi("https://jsonplaceholder.typicode.com/todos/1");
        assertEquals(1, jsonObject.get("userId").getAsInt());
        assertEquals(1, jsonObject.get("id").getAsInt());
        assertEquals("delectus aut autem", jsonObject.get("title").getAsString());
        assertFalse(jsonObject.get("completed").getAsBoolean());
    }
    
}
