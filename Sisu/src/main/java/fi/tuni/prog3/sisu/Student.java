package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * Class for representing a student.
 */
public class Student {
    
    private int id;
    private String name;
    private ArrayList<String> credits;
    private String currentDegreeProgramme;
    private DegreeProgramme programme;
    private int TotalCredits;

    /**
     * Constructor for initialing attributes id, name and credits.
     * @param id is an Integer containing student number.
     * @param name is students name.
     */
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        credits = new ArrayList<>();
        
        this.TotalCredits = 0;
    }

    /**
     * Adds new credit into a List.
     * @param credit is name of the course that student has completed lastly.
     */
    public void addCredit(String credit) {
        
        if (!credits.contains(credit)) {
            credits.add(credit);
        }
    }
    
    /**
     * Deletes credit from a List.
     * @param credit is name of the course that student has completed lastly.
     */
    public void deleteCredit(String credit) {
        
        if (credits.contains(credit)) {
            credits.remove(credit);
        }
    }

    /**
     * Changes the current DegreeProgramme.
     * @param prog is a new given DegreeProgramme.
     */
    public void changeDegreeProgramme(String prog) {
        this.currentDegreeProgramme = prog;
    }

    public void changeDegreeProgramme(DegreeProgramme programme) {
        this.programme = programme;
    }

    /**
     * Returns names of all the courses that student has completed.
     * @return List containing all the student's credits.
     */
    public ArrayList<String> getCredits() {
        return this.credits;
    }

    /**
     * Returns student's id.
     * @return id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns student's name.
     * @return name.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Reduce the students study points.
     */
    public void deleteTotalCredits(int credit) {
        this.TotalCredits -= credit;
        if(TotalCredits < 0){
            TotalCredits = 0;
        }
    }
    /**
     * Delete the students study points completely.
     */
    public void deleteAllCredits() {
        this.TotalCredits = 0;
        }
    
    /**
     * Increase the students study points.
     */
    public void addTotalCredits(int credit) {
        this.TotalCredits += credit;
    }
    
    /**
     * Returns student's total credits.
     * @return totalcredits.
     */
    public int getTotalCredits() {
        return this.TotalCredits;
    }

    /**
     * Returns the current DegreeProgramme as a string.
     * @return DegreeProgramme string.
     */
    public String getDegreeProgramme() {
        return this.currentDegreeProgramme;
    }

    /**
     * Returns the current DegreeProgramme as an object.
     * @return DegreeProgramme as an object.
     */
    public DegreeProgramme getProgramme() {
        return this.programme;
    }
}
