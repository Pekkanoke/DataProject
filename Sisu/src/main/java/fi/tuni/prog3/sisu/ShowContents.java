package fi.tuni.prog3.sisu;

import javafx.scene.control.TreeItem;
import java.util.*;

public class ShowContents {

    private DegreeModule programme;
    private TreeItem<String> mainRoot;

    /**
     * Constructor
     * 
     * @param student class object.
     */
    public ShowContents(Student student) {

        this.programme = student.getProgramme();
        this.mainRoot = new TreeItem<String>(
            "Opintojen rakenne", null);
        
        buildTreeView(programme, mainRoot);

    }

    /**
     * Builds tree view and is called by the constructor.
     * 
     * @param root is the root programme.
     * @param parentItem is the tree item of the course names.
     */
    public void buildTreeView(DegreeModule root, TreeItem<String> parentItem) {
        TreeItem<String> currentItem = new TreeItem<String>(root.getName(), null);
        parentItem.getChildren().add(currentItem);
    
        for (var module : root.getChildModules()) {
            buildTreeView(module, currentItem);
        }
    
        for (var course : root.getChildCourses()) {
            TreeItem<String> courseLeaf = new TreeItem<String>(course.getName(), null);
            currentItem.getChildren().add(courseLeaf);
        }
    }

    /**
     * Method for getting the tree item of classes and modules.
     * 
     * @return the whole structure.
     */
    public TreeItem<String> getTreeItem() {
        return this.mainRoot;
    }

    /**
     * Returns and finds all the immediate submodules.
     * @param parentModule the immediate parent.
     * @return list of modulse
     */
    public List<DegreeModule> findSubModules(String parentModule){
        List<DegreeModule> modules = DegreeModuleFactory.getDegreeModuleList();
        List<CourseUnit> courses = DegreeModuleFactory.getCourseUnitList();
        List<DegreeModule> childModules = new ArrayList<>();

        for (var course : courses){
            if (course.getName().equals(parentModule)){
                return childModules;
            }
        }

        for (var module : modules){
            if (module.getName().equals(parentModule)){
                childModules = module.getChildModules();
            }
        }
        return childModules;
    }

    /**
     * Returns and finds all the immediate subcourses.
     * 
     * @param parentModule the immediate parent.
     * @param grandparent the parents parent for confirmation of right course.
     * @return list of all the immediate sub courses.
     */
    public List<CourseUnit> findSubCourses(String parentModule, String grandparent){
        List<DegreeModule> modules = DegreeModuleFactory.getDegreeModuleList();
        List<CourseUnit> childCourses = new ArrayList<>();

        for (var module : modules){
            if (module.getName().equals(grandparent)){
                List<DegreeModule> childModules = module.getChildModules();
                for (var module1 : childModules){
                    if (module1.getName().equals(parentModule)){
                        childCourses = module1.getChildCourses();
                    }
                }
            }
        }
        
        // Checks if the course is empty, because then it is of first stage and
        // does not have a grandparent.
        if(childCourses.isEmpty()){
            for (var module : modules){
                if (module.getName().equals(parentModule)){
                    childCourses = module.getChildCourses();
                }
            }
        }
        
        return childCourses;
    }
}
