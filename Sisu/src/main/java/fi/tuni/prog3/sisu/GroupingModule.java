package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 *Class for storing information on Grouping Modules.
 */
public class GroupingModule extends DegreeModule {
    /**
     * Constructor for the module.
     * 
     * @param json file with the data for the module.
     */
    public GroupingModule(JsonObject json) {
        super(json);
    }
}
