/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * Class for jsonManager to get the contents of the .json file and create the 
 * nessessary student objects.
 */
public class StudentDeserializer implements JsonDeserializer<Student> {

    @Override
    public Student deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try{
            JsonObject jsonObject = json.getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            String currentDegreeProgramme = jsonObject.get("currentDegreeProgramme").getAsString();

            int TotalCredits = jsonObject.get("TotalCredits").getAsInt();

            Student student = new Student(id, name);
            student.addTotalCredits(TotalCredits);
            student.changeDegreeProgramme(currentDegreeProgramme);

            for (JsonElement creditElement : jsonObject.get("credits").getAsJsonArray()) {
                student.addCredit(creditElement.getAsString());
            }
            return student;

        }catch(Exception e){
            return null;
        }   
    }
}