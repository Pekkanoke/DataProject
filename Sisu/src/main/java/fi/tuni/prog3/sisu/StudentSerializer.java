/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Class for jsonManager to write the contents to the .json file and save the
 * nessessary data of the student objects.
 * @author sampo.suokuisma
 */
public class StudentSerializer implements JsonSerializer<Student> {
    @Override
    public JsonElement serialize(Student src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("id", src.getId());
            jsonObject.addProperty("name", src.getName());
            jsonObject.addProperty("currentDegreeProgramme", src.getDegreeProgramme());
            jsonObject.addProperty("TotalCredits", src.getTotalCredits());

            JsonArray creditsArray = new JsonArray();
            for (String credit : src.getCredits()) {
                creditsArray.add(credit);
            }
            jsonObject.add("credits", creditsArray);

            return jsonObject;
            
        } catch (Exception e){
            return null;
        }
    }
}