/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import java.util.*;

/**
 *An abstract class for storing information on Modules and Courses.
 */
public abstract class DegreeModule {
    private String name;
    private String id;
    private String groupId;
    private int minCredits;
    private List<DegreeModule> childModules;
    private List<CourseUnit> childCourses;
    
    /**
     * A constructor for initializing the member variables.
     * @param name name of the Module or Course.
     * @param id id of the Module or Course.
     * @param groupId group id of the Module or Course.
     * @param minCredits minimum credits of the Module or Course.
     */
    public DegreeModule(String name, String id, String groupId, 
            int minCredits) {
        
        this.name = name;
        this.id = id;
        this.groupId = groupId;
        this.minCredits = minCredits;
        this.childModules = new ArrayList<>();
        this.childCourses = new ArrayList<>();
        
    }

    /**
     * A constructor for initializing the member variables.
     * @param  json file.
     */
    public DegreeModule(JsonObject json) {
        if (json.get("name").isJsonObject()) {
            JsonObject nameJson = json.get("name").getAsJsonObject();
            this.name = nameJson.has("fi") ? nameJson.get("fi").getAsString() : nameJson.get("en").getAsString();
        } else {
            this.name = json.get("name").getAsString();
        }

        this.id = json.get("id").getAsString();
        this.groupId = json.get("groupId").getAsString();

        JsonObject rule = json.get("rule").getAsJsonObject();
        
        try {
            JsonObject minCreditsJson = rule.get("credits").getAsJsonObject();
            this.minCredits = minCreditsJson.get("min").getAsInt();
           
        } catch (Exception e){
            if(json.has("credits")){
                this.minCredits = json.get("credits").getAsJsonObject().get("min").getAsInt();
            }
            else if(json.has("targetCredits")){
                this.minCredits = json.get("targetCredits").getAsJsonObject().get("min").getAsInt();
            }
        }

        this.childModules = new ArrayList<>();
        this.childCourses = new ArrayList<>();
        
    }
    
    /**
     * Returns the name of the Module or Course.
     * @return name of the Module or Course.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the id of the Module or Course.
     * @return id of the Module or Course.
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Returns the group id of the Module or Course.
     * @return group id of the Module or Course.
     */
    public String getGroupId() {
        return this.groupId;
    }
    
    /**
     * Returns the minimum credits of the Module or Course.
     * @return minimum credits of the Module or Course.
     */
    public int getMinCredits() {
        return this.minCredits;
    }

    /**
    * Add a child module.
    * @param module The DegreeModule to add as a child.
    */
    public void addChildModule(DegreeModule module) {
        this.childModules.add(module);
    }

    /**
    * Remove a child module.
    * @param module The DegreeModule to remove from child modules.
    */
    public void removeChildModule(DegreeModule module) {
        this.childModules.remove(module);
    }

    /**
    * Retrieve child modules.
    * @return A list of child modules.
    */
    public List<DegreeModule> getChildModules() {
        return this.childModules;
    }

    /**
    * Add a child course.
    * @param course The CourseUnit to add as a child.
    */
    public void addChildCourse(CourseUnit course) {
        this.childCourses.add(course);
    }

    /**
     * Remove a child course.
    * @param course The CourseUnit to remove from child courses.
    */
    public void removeChildCourse(CourseUnit course) {
        this.childCourses.remove(course);
    }

    /**
    * Retrieve child courses.
    * @return A list of child courses.
    */
    public List<CourseUnit> getChildCourses() {
        return this.childCourses;
    }
}
