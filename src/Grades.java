
package grades;

import java.util.*;


public class Grades {
    // available modules
    List<String> modules = Arrays.asList("Database", "Data Structure", "Operating System", "Mathematics", "System Design");
    // menu choices
    List<String> choices = Arrays.asList("add", "delete", "search", "update", "list", "quit");
    Scanner input = new Scanner(System.in); // declared here because it's often used
    Trie database = new Trie(); // the database of students implemented with a Trie structure
    
    public static void main(String[] args){
        // create a new object from the main class to avoid static context problems
        Grades cw2 = new Grades();
    }
    private Grades() {
        menu(); // initiates the programme
    }
    
    private void menu() {
        boolean goOn = true;    // the value of goOn determines whether to continue or stop the program
        while (goOn) {
            displayMenu();  // simple serie of System.out.println()
            String menuChoice = getMenuChoice(); // contains catching of exceptions
            goOn = processChoice(menuChoice);   // executes the user's choice
        }
    }
    private String getMenuChoice() {
        String choice = "";
        boolean valid = false;
        
        // loop as long as the user does not input a valid choice for the menu
        while (!valid) {
            // does not matter capital or small letters
            choice = input.nextLine().toLowerCase();
            
            // checks that the choice is a good one
            if (choices.contains(choice))
                valid = true;
            
            // error message
            if (!valid)
                System.out.print("\tInvalid input, please try again...\n\tChoice: ");
        }
        
        return choice;
    }
    private boolean processChoice(String choice)  {
        // simple switch case based on the user's choice
        switch(choice) {
            case "add":
                addStudent();
                return true;
            case "delete":
                removeStudent();
                return true;
            case "search":
                searchStudent();
                return true;
            case "update":
                updateStudent("");
                return true;
            case "list":
                list();
                return true;
            case "quit":
                // ask the user for confirmation before quitting
                System.out.print("Are you sure you want to quit (y/n)? ");
                // only "y" or "Y" will terminate the programme
                if (input.nextLine().toLowerCase().equals("y"))
                    return false;
                return true;
        }
                
        return true;
    }
    private void displayMenu() {
        // display the menu
        System.out.println("\n\t _______________________________________________________________________");
        System.out.println("\t|\t\t\t\t   MENU  \t\t\t\t|");
        System.out.println("\t|\t\t\t\t\t\t\t\t\t|");
        System.out.println("\t|  Add:\t\t insert a new student/ student's course in the database\t|");
        System.out.println("\t|  Delete:\t remove a student/ student's course from the database\t|");
        System.out.println("\t|  Search:\t query the database in search of a student's grades\t|");
        System.out.println("\t|  Update:\t modify a student's grades, if it's in the database\t|");
        System.out.println("\t|  List:\t print an ordered list of student-grade given a course\t|");
        System.out.println("\t|  Quit:\t exit the programme. All saved data will be lost\t|");
        System.out.println("\t|_______________________________________________________________________|");
        System.out.print("\tChoice: ");
    }
    
    
    private void addStudent() {
        // instruction message
        System.out.println
        ("\nPlease enter the student's name and then the modules with relative marks.\n"
                + "There are 5 available modules: Database, Data Structure, Operating System, Mathematics and System Design.\n"
                + "Entering as module's name a string that does not match any of he listed modules "
                + "will result in the student being added to the database with the given modules.\n"
                + "Type EXIT in the name field to return to the main menu.\n");
        
        // name input phase
        String name = getName();
        if (name.equals("EXIT"))
            return;     // exit to the menu if the user enters exit
        
        // if the student already exixsts, update it
        if (database.getNode(name) != null)
            updateStudent(name);
        // otherwise insert a new one
        else {
            HashMap<String, Integer> data = getMarks();
            database.insert(name, data);
            // success message
            System.out.println("Student "+name+" added to the database with "+data.size()+" modules.\n\n");
        }
    }
    private void removeStudent() {
        // instruction message
        System.out.println("Enter the name of the student to remove from the database or the\n"
                + "student's name and the courses to delete from the student separated by comma only (no space)\n"
                + "(eg. John,Database,Operating System)");
        
        // separate the inserted values
        String data[] = getName().split(",");
        // exit if the user types EXIT
        if (data[0].equals("EXIT"))
            return;
        
        // if there is more than just the name...
        if (data.length>1) {
            // ... then remove only certain courses...
            int removedModules = database.removeCourses(data);
            
            // database.removeCourses(data) returns the number of courses removed;
            // +int means that it was successfull, 0 means there was no removal,
            // -1 means the the student could not be found
            if (removedModules > 0)
                System.out.println(removedModules+" course(s) removed from "+data[0]);
            else if (removedModules == 0)
                System.out.println("Student does not have any of the given courses");
            else
                System.out.println("Student "+data[0]+" not found.");
        }
        // ... otherwise remove the whole student node
        else {
            if (database.removeName(data[0]))
                System.out.println("Student "+data[0]+" removed from the database.");
            // database.removeName(data[0]) returns true if the student was successfully removed
            else
                System.out.println("Student "+data[0]+" not found.");
        }
    }
    private void searchStudent() {
        // instruction message
        System.out.println("Enter the name of the student to look for in the database or the\n"
                + "student's name and the courses to look for separated by comma.\n"
                + "(eg. John,Database,Operating System)");
        
        // data collection
        String data[] = getName().split(",");
        if (data[0].equals("EXIT"))
            return;
        
        // retrieval of the student
        Node student = database.getNode(data[0]);
        
        // continue only if the student could be found
        if (student != null) {
            if (data.length==1) {
                // display a student with marks
                System.out.println(data[0]+"'s marks are:\n"+student.marksToString());
            }
            else {
                // display a student's grades
                HashMap<String, Integer> marks = student.getMarks();
                System.out.println(data[0]+"'s selected marks are:");
                for (int i=1; i<data.length; i++)
                    // checks that the requested courses are actually taken by the student
                    if (marks.containsKey(data[i]))
                        // print out the mark
                        System.out.println(data[i]+": \t"+marks.get(data[i]));
            }
        }
        else
            System.out.println("Student "+data[0]+" not found.");
    }
    private void updateStudent(String name) {
        
        if (!name.isEmpty())
            System.out.println("Student is already present in the database, therefore its modules will be updated");
       
        // enter the student's name if not passed already
        else {
            // name input phase
            name = getName();
            if (name.equals("EXIT")) {
                return;     // exit to the menu if the user enters exit
            }
        }
        
        // prepare the list of modules to update
        HashMap<String, Integer> data = getMarks();
        
        // update the values
        database.insert(name, data);
        System.out.println("Student "+name+" updated.");
    }
    private void list() {
        // instruction message
        System.out.println("Input one of Database, Data Structure, Operating System, Mathematics or System Design.");
        String course = "";
        boolean valid = false;
        
        // course input phase with validation
        while (!valid) {
            try {
                System.out.print("Course name: ");
                course = input.nextLine();
                
                if (!course.isEmpty())
                    valid = true;
                
            } catch (NoSuchElementException e) {
                // exception handling
                System.out.println("Invalid input, please try again...");
            }
        }
        
        // executes only if the chosen course actually exists
        if (modules.contains(course)) {
            // retrieve all grade-student couples ordered within the given course
            for (String line: database.getStudents(course))
                // and print them out
                System.out.println(line);
        } else
            System.out.print("Course not found.");
    }
    
    private String getName() {
        String name = "";
        boolean valid = false;
        
        // name input phase with validation
        while (!valid) {
            try {
                System.out.print("Student's name: ");
                name = input.nextLine();
                
                if (!name.isEmpty())
                    valid = true;
                
            } catch (NoSuchElementException e) {
                System.out.println("Invalid input, please try again...");
            }
        }
        
        return name;
    }
    private HashMap<String, Integer> getMarks() {
        // marks are stored as Course-Mark couples in a HashMap
        HashMap<String, Integer> data = new HashMap<>();
        
        // needed variables
        String module = "";
        boolean valid = false;
        Integer mark = 0;
        
        // marks input phase
        // loop for all the available modules (there are 5 only)
        for (int i=0; i<5; i++) {
            try {
                // module input
                System.out.print("Student's module "+(i+1)+": ");
                module = input.nextLine();
                
                // module check: 1-in the list    2-not already entered
                if (!modules.contains(module) || data.keySet().contains(module))
                    break;
                
                // mark input
                while (!valid) {
                    try {
                        System.out.print("Student's "+module+" mark: ");
                        mark = Integer.parseInt(input.nextLine());
                        
                        if (mark<=100 && mark>=0)
                            valid = true;
                        else
                            System.out.println("Invalid grade, please try again...");
                        
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please try again...");
                    }
                }
                valid = false;
                
            } catch (NoSuchElementException e) {
                System.out.println("Invalid input, please try again...");
            }
            
            // once here, the mark and module are to be added to the student's data
            data.put(module, mark);
        }
        
        return data;
    }
    
}