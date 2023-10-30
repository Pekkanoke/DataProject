package fi.tuni.prog3.sisu;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Class for getting all DergreeProgrammes using API reguest.
 */
public class TreeModule {
    
    private ArrayList<String> programmes;
    
    /**
     * Constructs an initially empty programmes ArrayList.
     */
    public TreeModule() {
        programmes = new ArrayList<>();
    }

    /**
     * Makes an API reguest and storest all programmes into a List.
    */
    public void createProgrammeList() {

        SisuAPI api = new SisuAPI();
        JsonObject progsJson = api.getJsonObjectFromApi("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
        JsonArray progs = progsJson.getAsJsonArray("searchResults");

        for (var prog : progs) {
            JsonObject progJson = prog.getAsJsonObject();
            String name = progJson.get("name").getAsString();
            programmes.add(name);
        }
    }

    /**
     * Starts the module Creation with the given student object.
     * 
     * @param student object for recieving the degree programme.
     */
    public void startModuleCreation(Student student) {

        SisuAPI api = new SisuAPI();
        JsonObject progsJson = api.getJsonObjectFromApi("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
        JsonArray progs = progsJson.getAsJsonArray("searchResults");
        
        for (var prog : progs) {
            JsonObject progJson = prog.getAsJsonObject();
            if (progJson.get("name").getAsString().
            equals(student.getDegreeProgramme())) {
                String groupId = progJson.get("groupId").getAsString();
                JsonObject module = DegreeModuleFactory.fetchModuleJson(groupId, false);
                DegreeProgramme p = DegreeModuleFactory.buildDegreeModuleTree(module);
                student.changeDegreeProgramme(p);
            }
        }
    }
    
    /**
     * Returns the list containing all DegreeProgrammes.
     * 
     * @return all DegreeProgrammes in programmes list.
     */
    public ArrayList<String> getDegreeProgrammes() {
        return this.programmes;
    }
}
