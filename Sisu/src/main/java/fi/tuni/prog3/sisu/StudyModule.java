package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 *An abstract class for storing information on Study Modules.
 */
public class StudyModule extends DegreeModule {
    /**
     * Constructor for creating a StudyModule class.
     * 
     * @param json object with the nessessary data for the constructor.
     */
    public StudyModule(JsonObject json) {
        super(json);
    }
}
