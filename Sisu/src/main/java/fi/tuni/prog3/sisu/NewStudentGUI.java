package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 *
 * JAVAFX class for storing information of a student not yet found in the 
 * register. Ask the user to chose a correct degree program and call InfoGUI to
 * show the program.
 */
public class NewStudentGUI {
    
    private Student student;
    private String chosenProgram;
    
    /**
     * Starts the scene and activates the whole class.
     * @param stage is the main stage used for the whole program.
     * @param student is the student handled in the whole program.
     */
     public void start(Stage stage, Student student) {
        
        int studentId = student.getId();
        String studentName = student.getName();
        this.student = student;
        
         //Buttons
        var quitButton = getQuitButton();
        Button saveButton = new Button("Save new student");
        
        //Create an inner layout.
        var group1 = new FlowPane();
        
        //Choicebox for the Degree Program.
        var choicebox = getChoiceBox(student);
        
        //The functionality after clicking the "Save new student"-button.
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //Check that the user has chosen some degree program.
                if(student.getDegreeProgramme() == null){
                    var label1 = new Label("Select a degree program first.");
                    group1.getChildren().add(label1);
                }
                
                //Create the students degree program and call the InfoGUI.
                TreeModule tm = new TreeModule();
                tm.createProgrammeList();
                tm.startModuleCreation(student);
                
                InfoGUI InfoGUIObject = new InfoGUI();
                InfoGUIObject.addJsonManager(jm);
                InfoGUIObject.start(stage, student);
            }
        });
       
        //Labels
        Label stNro = new Label("Studentnumber: " + studentId);
        Label stName = new Label("Students name: " + studentName);
        
        //Create the top-level layout for the scene.
        BorderPane border1 = new BorderPane();
        border1.setPadding(new Insets(10, 10, 10, 10));
        
        
        group1.setPadding(new Insets(10, 0, 0, 10));
        group1.setVgap(4);
        group1.setHgap(4);
        group1.setPrefWrapLength(170);
        group1.getChildren().add(stNro);
        group1.getChildren().add(stName);

        group1.getChildren().add(saveButton);
        group1.getChildren().add(choicebox);
        border1.setTop(group1);
        
        //Adding button to the BorderPane and aligning it to the right.
        BorderPane.setMargin(quitButton, new Insets(10, 10, 0, 10));
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        border1.setBottom(quitButton);
        
        //New scene for the opening window, or main_gui.
        Scene scene1 = new Scene(border1, 800, 500);                      
        stage.setScene(scene1);
        stage.setTitle("Study register");
        
        //Starts the main_gui.
        stage.setScene(scene1);
        
        //Close the program if "Quit"-button is pressed.
        stage.setOnCloseRequest((event) -> {Platform.exit();});
        stage.show();
    }
     
     public static void main(String[] args) {
        launch( args );
    }
     
    private jsonManager jm;
    //Add JsonManager for saving the students information.
    public void addJsonManager(jsonManager jm) {
        this.jm = jm;
    }
    
     //The functioning of the quit-button.
    public Button getQuitButton() {
        //Creating a button.
        Button quitButton = new Button("Quit");
        
        //Adding an event to the button to terminate the application.
        quitButton.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        
        return quitButton;
    }
    
    /**
     * Creates the ChoiceBox for the user to chose their degree program from.
     * @param student is the student whose information is handled.
     */
    public ChoiceBox getChoiceBox(Student student) {
        
        ChoiceBox cb = new ChoiceBox();
        TreeModule tm = new TreeModule();
        tm.createProgrammeList();
        
        //Give the user a hint.
        cb.setTooltip(new Tooltip("Select degree program"));
        cb.getItems().addAll(tm.getDegreeProgrammes());
        
        //Select the program and save it to the student.
        cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chosenProgram = newValue;
                student.changeDegreeProgramme(chosenProgram);
                student.deleteAllCredits();
            }
        });
        return cb;
    } 
}