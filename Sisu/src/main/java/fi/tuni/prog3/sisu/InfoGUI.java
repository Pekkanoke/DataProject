package fi.tuni.prog3.sisu;

import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.cell.CheckBoxTreeCell;

/**
 *
 * JavaFX Sisu class, which shows the information the user searched for. Runs
 * the GUI and searches for the information of the student from file by JsonManager.
 * Also requires information from DegreeModule and DegreeProgramme.
 * Uses the information of the chosen student, changes it and saved before closing
 * the program.
 */
public class InfoGUI {
   
    private Stage m_stage;
    private Student m_student;
    private final VBox rightVBox;
    private final VBox leftVBox;
    private final HBox centerHBox;
    private final VBox topVBox;

    public InfoGUI() {
        this.rightVBox = new VBox();
        this.leftVBox = new VBox();
        this.centerHBox = new HBox(10);
        this.topVBox = new VBox(0);
    }

    /**
     * Collects all the nodes to a single HBox.
     */
    private void CenterHBox() {
        centerHBox.getChildren().addAll(leftVBox, rightVBox,topVBox);
    }
   
    /**
     * Depicts the left side of the scene.
     * @param student is the student, whose degree program and progress is shown.
     */
    private void LeftVBox(Student student) {
        //Creating a VBox for the left side.
        
        leftVBox.setPrefWidth(380);
        leftVBox.setStyle("-fx-background-color: #8fc6fd;");
        
        leftVBox.getChildren().add(new Label("Degree Information"));

        //Create the treeview of the degree progam.
        ShowContents test = new ShowContents(student);

        TreeItem<String> contents = test.getTreeItem();
        TreeView<String> treeView = new TreeView<String>(contents);

        leftVBox.getChildren().add(treeView);
        
        //Prepare an event handler for when the user expands a branch.
        contents.addEventHandler(TreeItem.<String> branchExpandedEvent(), 
                new EventHandler<TreeItem.TreeModificationEvent<String>>() {
        @Override
        public void handle(TreeModificationEvent<String> event) {
            TreeItem<String> item = event.getTreeItem();
            
            //The top-most level is ignored in the next function call.
            if(!item.getValue().equals("Opintojen rakenne")){
                var parent = item.getParent().getValue();   
            
            var children = item.getChildren();
            int i = children.size() - 1;
                if(children.get(i).isLeaf()){
                    var moduleName = item.getValue();
                    //Call for the rightVBox to show the progress of the student.
                    RightVBoxPrint(moduleName, parent);
                }  
            }
        }
        });
    }

    /**
     * Creates the right view of the scene in the beginning. This node is later 
     * replaced by another function.
     */
    private void RightVBox(){
        rightVBox.setPrefWidth(380);
        rightVBox.setStyle("-fx-background-color: #b1c2d4;");
        rightVBox.getChildren().add(new Label("Attainments"));
    }
    
    /**
     * Replaces the righVBox and calls for other classes and handles information.
     * @param student is the object student, whose degree and progress are shown.
     * @param parentName is the name of the module expanded in the leftVBox.
     */
    private void RightVBoxPrint(String module_name, String parentName) {
        rightVBox.setPrefWidth(380);
        rightVBox.setStyle("-fx-background-color: #b1c2d4;");
        
        //Create treeview for the single module.
        ShowContents test = new ShowContents(m_student);
        TreeItem<String> contents = test.getTreeItem();
        
        final var treeview = new TreeView<String>();  
        treeview.setEditable(true);
        
        //Get the courses the module has.
        List<CourseUnit> childCourses = new ArrayList<>();
        childCourses = test.findSubCourses(module_name, parentName); 
        
        final List<CourseUnit> subCourses = childCourses;
        CheckBoxTreeItem<String> rootItem = 
            new CheckBoxTreeItem<String>(module_name);
        rootItem.setExpanded(true); 
        
        //Create the treeview and add a checkbox next to each item.
        treeview.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
        for (int i = 0; i < subCourses.size(); i++) {
            int index = i;
            final CheckBoxTreeItem<String> checkBoxTreeItem =
                    new CheckBoxTreeItem<String>(subCourses.get(i).getName() +
                            " - " + subCourses.get(i).getMinCredits() + " op.");
            rootItem.getChildren().add(checkBoxTreeItem);
            //If the student has credit for some course, set the current checkbox
            //selected.
            if(m_student.getCredits().contains(subCourses.get(index).getName())){
                checkBoxTreeItem.setSelected(true);
            }
            //Listen to the checkboxes and record the choices.
            checkBoxTreeItem.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov,
                    Boolean oldVal, Boolean newVal) -> {
                        subCourses.get(index).setCompleted(newVal);
                        //Add the credits to the student-objects credits.
                        //Add the study points to the student.
                        if(subCourses.get(index).isCompleted()){
                            m_student.addCredit(subCourses.get(index).getName()) ;
                            m_student.addTotalCredits(subCourses.get(index).getMinCredits());
                            
                        }
                        //Delete the credit from the students credits.
                        //Delete the study points from the student.
                        else{
                            m_student.deleteCredit(subCourses.get(index).getName());
                            m_student.deleteTotalCredits(subCourses.get(index).getMinCredits());
                        }   
                    }
            );
        }
        treeview.setRoot(rootItem);
        treeview.setShowRoot(true);
        
        //Clean the node before setting the new node, to keep the node updated.
        rightVBox.getChildren().clear();
        rightVBox.getChildren().add(treeview); 
        TopVBox();
    }
    
    /**
     * Creates the right-most node for showing the progress of the student.
     */
    private void TopVBox(){
        topVBox.setPrefWidth(120);
        topVBox.setStyle("-fx-background-color: #8FBC8F;");
        
        //Clean the node to keep it updated as the program proceeds.
        topVBox.getChildren().clear();
        //Find the needed study point counts for the program and the 
        //students current points.
        int requiredTest = m_student.getProgramme().getMinCredits();
        int completedTest = m_student.getTotalCredits();
        
        double requiredTestAsDouble = requiredTest;
        double completedTestAsDouble = completedTest;
        double currentProgress = (completedTestAsDouble / requiredTestAsDouble);
        
        String reqStr = String.valueOf(requiredTestAsDouble);
        String curStr = String.valueOf(completedTest);
        final Label label1 = new Label(curStr + "/" + reqStr);
        
        //Create the progress bar and set it right.
        final ProgressBar pb = new ProgressBar();
        pb.setProgress(currentProgress);
        
        topVBox.getChildren().addAll(label1, pb);  
    }
             
    private jsonManager jm;
    
    /**
     * Calls the JsonManager to read and save the StudentDataJson-file.
     * @param jm is the jsonManager needed.
     */
    public void addJsonManager(jsonManager jm) {
        this.jm = jm;
    }
    
    /**
     * Adds new button for changing the degree program.
     */
    public Button getChangeButton() {
        Button changeButton = new Button("Change programme");
        
        //Activate the button by click. Then call for NewStudentGUI.
        changeButton.setOnAction((ActionEvent event) -> {
            NewStudentGUI newStudentGUI = new NewStudentGUI();
            newStudentGUI.addJsonManager(jm);
 
            newStudentGUI.start(m_stage, m_student);
        });
        return changeButton;
    }
    
    /**
     * Adds new button for closing the program.
     */
    public Button getQuitButton() {
        Button quitButton = new Button("Quit");
        
        //Adding an event to the button to terminate the application.
        //Save the information of the student to the json-file.
        quitButton.setOnAction((ActionEvent event) -> {
            if (jm != null) {
                try {
                    jm.addStudent(m_student);
                    jm.writeToFile("StudentData.json");
                } catch (Exception ex) {
                }
            } else {
                System.out.println("jsonManager is null!");
            }
            Platform.exit();
        });
        
        return quitButton;
    }
    
    /**
     * Activate the graphic scene.
     * @param stage is the Stage used in the whole program.
     * @param student is the current student whose progress we want to use.
     */
    public void start(Stage stage,Student student) {
        m_student = student;
        m_stage = stage;

        var quitButton = getQuitButton();
        var changeButton = getChangeButton();

        Label stInfo = new Label("Degree program information");

        BorderPane border2 = new BorderPane();
        border2.setPadding(new Insets(10, 10, 10, 10));
        BorderPane.setMargin(quitButton, new Insets(10, 10, 0, 10));
        BorderPane.setMargin(changeButton, new Insets(10, 10, 0, 10));
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        BorderPane.setAlignment(changeButton, Pos.TOP_RIGHT);
        border2.setBottom(quitButton);
        border2.setTop(changeButton);
        CenterHBox();
        LeftVBox(m_student);
        RightVBox();
        TopVBox();
        border2.setCenter(centerHBox);

        // New scene for the second view.
        Scene scene2 = new Scene(border2, 800, 500);
        stage.setScene(scene2);
    }
    
    public static void main(String[] args) {
        launch();
    }
}
