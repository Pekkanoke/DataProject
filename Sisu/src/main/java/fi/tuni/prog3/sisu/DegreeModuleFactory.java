package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class DegreeModuleFactory {

    private static List<CourseUnit> allCourses = new ArrayList<>();
    private static List<DegreeModule> allModules = new ArrayList<>();

    /**
     * Creates a DegreeModule object from the provided JSON.
     *
     * @param json The JSON object containing the data for the degree module.
     * @return The created DegreeModule object.
     */
    public static DegreeModule createDegreeModuleFromJson(JsonObject json) {
        String type = json.get("type").getAsString();
        switch (type) {
            case "DegreeProgramme":
                return new DegreeProgramme(json);
            case "StudyModule":
                return new StudyModule(json);
            case "GroupingModule":
                return new GroupingModule(json);
            default:
                return null;
        }
    }

    /**
     * Creates a CourseUnit object from the provided JSON.
     *
     * @param json The JSON object containing the data for the course unit.
     * @return The created CourseUnit object.
     */
    public static CourseUnit createCourseUnitFromJson(JsonObject json) {
        return new CourseUnit(json);
    }

    /**
     * Builds a DegreeModule tree from the provided JSON object.
     *
     * @param rootJson The JSON object containing the data for the root DegreeProgramme.
     * @return The root DegreeProgramme object with its child modules and courses.
     */
    public static DegreeProgramme buildDegreeModuleTree(JsonObject rootJson) {
        DegreeProgramme root = (DegreeProgramme) createDegreeModuleFromJson(rootJson);
        buildDegreeModuleTreeRecursively(root, rootJson);
        return root;
    }

    /**
     * Recursively builds the DegreeModule tree.
     *
     * @param parent The parent DegreeModule object to add children to.
     * @param parentJson The JSON object containing the data for the parent module.
     */
    private static void buildDegreeModuleTreeRecursively(DegreeModule parent, JsonObject parentJson) {
        
        if (parentJson.has("rule")) {
            JsonObject rule = parentJson.getAsJsonObject("rule");
            processRule(parent, rule);
        }
    }

    /**
     * Prosesses all the different possibilities of rule and rules combinations
     * and calls buildDegreeModuleTreeRecursively to build the whole structure.
     *
     * @param parent The parent DegreeModule object to add children to.
     * @param rule The JSON object containing the data for a part of the parent.
     */
    private static void processRule(DegreeModule parent, JsonObject rule) {
        if (rule.has("rule")) {
            JsonObject nestedRule = rule.getAsJsonObject("rule");
            processRule(parent, nestedRule);
        }

        if (rule.has("rules")) {
            JsonArray submodules = rule.getAsJsonArray("rules");
            for (JsonElement submoduleElement : submodules) {
                JsonObject submoduleJson = submoduleElement.getAsJsonObject();
                processRule(parent, submoduleJson);
            }
        } else {
            if (rule.get("type").getAsString().equals("ModuleRule")) {
                String groupId = rule.get("moduleGroupId").getAsString();
                JsonObject moduleJson = fetchModuleJson(groupId, false);
                DegreeModule submodule = createDegreeModuleFromJson(moduleJson);
                if (submodule != null) {
                    allModules.add(submodule);
                    parent.addChildModule(submodule);
                    try {
                        moduleJson.get("moduleGroupId").getAsString();
                    }
                    catch (Exception e) {
                        moduleJson.get("type").getAsString();
                    }
                    buildDegreeModuleTreeRecursively(submodule, moduleJson);
                }
            } else if (rule.get("type").getAsString().equals("CourseUnitRule")) {
                String groupId = rule.get("courseUnitGroupId").getAsString();
                JsonObject courseJson = fetchModuleJson(groupId, true);
                CourseUnit course = createCourseUnitFromJson(courseJson);
                if (course != null) {
                    allCourses.add(course);
                    parent.addChildCourse(course);
                }
            }
        }
    }

    /**
     * Calls the SisuAPI with a url.
     *
     * @param groupId for url.
     * @param isCourse for url option.
     * @return The recieved json object from the given url.
     */
    public static JsonObject fetchModuleJson(String groupId, boolean isCourse) {
        String apiUrl = "https://sis-tuni.funidata.fi/kori/api/";
        String endpoint = isCourse ? "course-units/by-group-id" : "modules/by-group-id";
        String urlStr = apiUrl + endpoint + "?groupId=" + groupId + "&universityId=tuni-university-root-id";

        SisuAPI api = new SisuAPI();
        JsonObject json = api.getJsonObjectFromApi(urlStr);
        return json;
    }

    /**
     * Returns CourseUnit class with the right name.
     *
     * @param name of the course to find.
     * @return CourseUnit object of the course.
     */
    public static CourseUnit getCourseUnit(String name) {
        for (var course : allCourses){
            if (name.equals(course.getName())){
                return course;
            }
        }
        
        return null;
    }

    /**
     * Returns CourseUnit list.
     *
     * @return CourseUnit objects.
     */
    public static List<CourseUnit> getCourseUnitList() {
        return allCourses;
    }

    /**
     * Returns DegreeModules as a list.
     *
     * @return all modules.
     */
    public static List<DegreeModule> getDegreeModuleList() {
        return allModules;
    }
}
