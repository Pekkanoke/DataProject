package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 *An abstract class for storing courses.
 */
public class CourseUnit {
    private String name;
    private String id;
    private String groupId;
    private int minCredits;
    private boolean completed;
    
    /**
     * A constructor for initializing the member variables.
     * @param name name of the Course.
     * @param id id of the Course.
     * @param groupId group id of the Course.
     * @param minCredits minimum credits of the Course.
     */
    public CourseUnit(String name, String id, String groupId, 
            int minCredits) {
        
        this.name = name;
        this.id = id;
        this.groupId = groupId;
        this.minCredits = minCredits;
        this.completed = false;
    }

    /**
     * A constructor for initializing the member variables.
     * @param  json file with all the information for constructor.
     */
    public CourseUnit(JsonObject json) {
        if (json.get("name").isJsonObject()) {
            JsonObject nameJson = json.get("name").getAsJsonObject();
            this.name = nameJson.has("fi") ? nameJson.get("fi").getAsString() : nameJson.get("en").getAsString();
        } else {
            this.name = json.get("name").getAsString();
        }
        this.id = json.get("id").getAsString();
        this.groupId = json.get("groupId").getAsString();

        try {
            JsonObject minCreditsJson = json.get("credits").getAsJsonObject();
            this.minCredits = minCreditsJson.get("min").getAsInt();
        } catch (Exception e){
            this.minCredits = 0;
        }
    }

    /**
     * Returns the name of the Course.
     * @return name of the Course.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the id of the Course.
     * @return id of the Course.
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Returns the group id of the Course.
     * @return group id of the Course.
     */
    public String getGroupId() {
        return this.groupId;
    }
    
    /**
     * Returns the minimum credits of the Course.
     * @return minimum credits of the Course.
     */
    public int getMinCredits() {
        return this.minCredits;
    }

    /**
     * Returns status of the course.
     * @return boolean value if completed.
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Sets the course status as completed.
     * @param value 
     */
    public void setCompleted(boolean value){
        completed = value;
    }
}
