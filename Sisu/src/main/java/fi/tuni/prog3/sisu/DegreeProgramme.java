package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 *Class for storing information on Degree Programmes.
 */
public class DegreeProgramme extends DegreeModule {
    /**
     * Constructor for a degree programme.
     * 
     * @param json file of the programme.
     */
    public DegreeProgramme(JsonObject json) {
        super(json);
    }
}
