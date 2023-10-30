package fi.tuni.prog3.sisu;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Class that implements iReadAndWriteToFile interface
 */

public class jsonManager implements iReadAndWriteToFile {

    private ArrayList<Student> students;

    /**
     * Constructs an initially empty students ArrayList.
     */
    public jsonManager() {
        students = new ArrayList<>();
    }

    /**
     * Implements readFromFile method from iReadAndWriteToFile interface
     * and reads a file containing student data.
     * @return true if reading was successful else false.
     */
    @Override
    public boolean readFromFile(String fileName) throws Exception {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Student.class, new StudentDeserializer())
                    .create();

            FileReader reader = new FileReader(fileName);
            students = gson.fromJson(reader, new TypeToken<ArrayList<Student>>() {}.getType());
            reader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Implements writeToFile method from iReadAndWriteToFile interface
     * and writes a file containing student data.
     * @return true if writing was succesful else false.
     */
    @Override
    public boolean writeToFile(String fileName) throws Exception {
        
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Student.class, new StudentSerializer())
                .create();

        try {
            String json = gson.toJson(students);
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(json);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return false;
    }

    /**
     * Adds a new student to a List or replaces an old object with updated one.
     * @param s Student object that was changed lastly.
     */
    public void addStudent(Student s) {
        
        if (students == null){
            students = new ArrayList<>();
            students.add(s);
            return;
        }
        boolean contains = false;
        
        for(int i = 0; i < students.size(); ++i){
            if(students.get(i).getId() == s.getId() &&
                    students.get(i).getName().equals(s.getName())){
                students.set(i, s);
                contains = true;
                break;
                }
        }
            
        if(!contains){
            students.add(s);
        }
    }
    
    /**
     * Finds a student if they have prior selections.
     * @param name
     * @param id the students id.
     * @return Student object of the asked student.
     */
    public Student findStudent(String name, int id) {
        if (students == null){
            return null;
        }
        for (var theStudent : students){
            if (theStudent.getName().equals(name) && theStudent.getId() == id){
                return theStudent;
            }
        }
        
        return null;
    }
}
