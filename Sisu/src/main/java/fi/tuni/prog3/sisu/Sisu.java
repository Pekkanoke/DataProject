package fi.tuni.prog3.sisu;

/**
 * JavaFX Sisu
 */

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 * Starts the main window.
 */
public class Sisu extends Application {

    /**
     * Starts the main window.
     */
    @Override
    public void start(Stage stage){
        MainGUI MainGUIObject = new MainGUI();
        Student student = MainGUIObject.start(stage);
        }
    
    }

