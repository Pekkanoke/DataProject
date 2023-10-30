package fi.tuni.prog3.sisu;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * MainGUI which asks for the users student number and name.
 * The search-button will guide the 
 * program to the second scene, which will show the students degree program and progress.
 * If the student is not yet in the records, the class will call the NewStudentGUI.
 */
public class MainGUI {
    

    private jsonManager jm;
 
    /**
     * Activates the scene.
     * @param stage is given from Sisu.java (acts as the main stage for the
     * whole program).
     */
    public Student start(Stage stage) {
        
        //Textfields for gathering studentnumber and students name.
        TextField studentNumberIn = new TextField();
        studentNumberIn.setPrefWidth(150);
        TextField studentNameIn = new TextField();
        studentNameIn.setPrefWidth(150);
        
        //The top-level layout for nodes.
        var group1 = new FlowPane();
        
        //Information needed to fetch the students information from previous sessions.
        //Check that the given student number is truly an integer.
        int studentId = 0;
        String student_name = "";
        try{
            studentId = Integer.parseInt(studentNumberIn.getText());
        }
        catch (NumberFormatException ex){
        }
       
        //Create new student-object.
        Student student = new Student(studentId, student_name);

        //Buttons
        var quitButton = getQuitButton();
        Button searchButton = new Button("Search");
        
        //When the "Search"-button is pressed, the information of the student
        //is searched for from the saved records. If it is not found, the window
        //NewStudentGUI is called.
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                // Check if student number consist of only numbers and warns the
                //user if not followed.
                int studentId = 0;
                var labelStrInt = new Label("Student number must use numbers 0-9!");
                group1.getChildren().remove(labelStrInt);
                try {
                    studentId = Integer.parseInt(studentNumberIn.getText());
                } catch (Exception parseError){
                    
                    group1.getChildren().add(labelStrInt);
                    return;
                }

                // Checks if student name consists of only letters and warns the
                //user if not followed.
                String studentName = "";
                var labelStr = new Label("Name must be letters of a-z or A-Z!");
                group1.getChildren().remove(labelStr);
                if (studentNameIn.getText().matches("[a-zA-Z]+")){
                    studentName = studentNameIn.getText();
                } else {
                    group1.getChildren().add(labelStr);
                    return;
                }
                
                //Read the student records from Json-file.
                jm = new jsonManager();
                boolean isRead;
                try {
                    isRead = jm.readFromFile("StudentData.json");
                } catch (Exception error){
                    isRead = false;
                    System.out.print(error.getMessage());
                    error.printStackTrace();
                }

                if (isRead) {
                    //If the student is found in the student records, call
                    //to InfoGUI for showing the degree program and progress.
                    if (jm.findStudent(studentName, studentId) != null){
                        InfoGUI InfoGUIObject = new InfoGUI();
                        InfoGUIObject.addJsonManager(jm);
                        
                        //Create the students degree program in type treeview.
                        TreeModule tm = new TreeModule();
                        tm.createProgrammeList();
                        tm.startModuleCreation(jm.findStudent(studentName, studentId));
                        
                        InfoGUIObject.start(stage, jm.findStudent(studentName, studentId));
                        
                        //Otherwise create a new student in the records and call
                        //NewStudentGUI to chose a program.
                    } else {
                        Student chosenStudent = new Student(studentId, studentName);
                        jm.addStudent(chosenStudent);
                        NewStudentGUI newStudentGUI = new NewStudentGUI();
                        newStudentGUI.addJsonManager(jm);
                        newStudentGUI.start(stage, chosenStudent);
                    }
                }
            }
        });
       
        //Create labels and collect the nodes for the layout.
        Label stNroIn = new Label("Studentnumber: ");
        Label stNameIn = new Label("Students name: ");
        
        BorderPane border1 = new BorderPane();
        border1.setPadding(new Insets(10, 10, 10, 10));
        
        
        group1.setPadding(new Insets(10, 0, 0, 10));
        group1.setVgap(4);
        group1.setHgap(4);
        group1.setPrefWrapLength(170);
        group1.getChildren().add(stNroIn);
        group1.getChildren().add(studentNumberIn);
        group1.getChildren().add(stNameIn);
        group1.getChildren().add(studentNameIn);
        
        group1.getChildren().add(searchButton);
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
        //Close the program and stage if "Quit"-button is pressed.
        stage.setOnCloseRequest((event) -> {Platform.exit();});
        
        stage.show();
        return student;
    }
     
     public static void main(String[] args) {
        launch( args );
    }
     
    /**
     * Creates the Quit-button for closing the program.
     */
    public Button getQuitButton() {
        //Creating a button.
        Button quitButton = new Button("Quit");
        
        //Adding an event to the button to terminate the application.
        quitButton.setOnAction((ActionEvent event) -> {
            Platform.exit();
        }); 
        return quitButton;
    }
}