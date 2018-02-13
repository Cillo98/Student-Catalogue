
package grades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Trie Data structure for a database of students.
 * This class can be instantiated to create a Trie of 
 * students. It contains methods to insert new students, 
 * update the existing ones, delete whole entries or part
 * of them and it provides methods to display lists of entries.
 * Students are inserted in the Trie as nodes with a character 
 * value in such a way that the path to traverse the Trie and stop at
 * a node that contains grade/mark pairs will give a student's name.
 * Most operations have time complexity of O(k), where k is the length
 * of the longest student's name. As names are all relatively short,
 * operations can be considered O(1). The only exception is the method
 * to list student/grade pair, which takes longer due to the sorting
 * of its values.
 * 
 * @author Thomas Cilloni
 */
public class Trie {
    // the Trie has a starting node called root with no value
    private Node root;
    
    /**
     * Initialize the Trie.
     * The constructor does not require any parameter.
     * It simply instantiates a new empty node, the root
     * of the Trie.
     */
    public Trie() {
        // do not assign any value to the node
        root = new Node((char) 0);
    }
    
    /**
     * Add a new student node to the tree.
     * This method inserts a new entry in the tree or updates it
     * if it exists already.
     * 
     * The time complexity of adding a student is O(1), as the Trie
     * needs to be traversed by the student's name with only k passes.
     * 
     * @param name name of the student to add/update
     * @param marks HashMap of course-grade pairs
     */
    public void insert(String name, HashMap<String, Integer> marks) {
        // start from the root
        Node node = root;
        
        // and navigate down the tree following the name as path
        for (int i=0; i<name.length(); i++) {
            // get all nodes connected to the current node
            HashMap<Character, Node> child = node.getChildren();
            char character = name.charAt(i);
            
            // if the node already exists...
            if (child.containsKey(character)) {
                // make sure that when creating a new path from a leaf
                // the leaf is no more leaf
                if (node.isLeaf())
                    node.setLeaf(false);
                
                // get the next node following the name as path
                node = child.get(character);
            }
            // otherwise make a new one for every letter of the name
            // not already included in the database
            else {
                // construction and connection of a new node
                Node temp = new Node(character);
                child.put(character, temp);
                
                if (node.isLeaf())
                    node.setLeaf(false);
                
                node = temp;
            }
        }
        
        // at the end, give the node containing the last letter of the name
        // the marks of the student and set it as leaf (possibly)
        node.setMarks(marks);
        node.setIsName(true);
        if (node.getChildren().isEmpty())
            node.setLeaf(true);
    }
    
    /**
     * Get the node containing the data of the requested student.
     * This method returns the node corresponding to the last character of the
     * student's name, that is the one containing the student's courses-marks.
     * 
     * The time complexity is O(1) as the Trie is simply traversed once, with
     * a number of operations equal to the height of the Trie
     * 
     * @param name of the student to look for
     * @return a node corresponding to the requested student, if exists, null otherwise
     */
    public Node getNode(String name) {
        // as the Trie is traversed, the path is stored in matched
        String matched = "";
        // start from the root
        Node node = root;
        
        // navigate down the Trie. Implementation similar as above
        for (int i=0; i<name.length(); i++) {
            char ch = name.charAt(i);
            HashMap<Character, Node> children = node.getChildren();
            
            if(children.containsKey(ch)) {
                // navigate down
                matched += ch;
                node = children.get(ch);
            }
        }
        
        // if the traversed path corresponds to the requested student
        // (it is not too short) and the node actually corresponds to 
        // a student, return the node
        if (node.isName() && matched.equals(name))
            return node;
        
        // at this point the student's name was not found, return null
        return null;
    }
    
    /**
     * Remove a student from the database given its name.
     * This method removes a student from the database given its name.
     * First, the database is checked against the given name then, if the
     * student was found, nodes are deleted from bottom to top corresponding
     * to the student's name from right to left, only if those nodes do not
     * contain data of other students or have paths that lead to other students.
     * The method uses a recursive method to traverse the tree and delete nodes
     * in a bottom-up fashion.
     * 
     * The time complexity is O(1) to check if the student is in the database
     * and is again O(1) to remove the nodes. In total, the method has a time
     * complexity of O(1).
     * 
     * @param name of the student to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeName(String name) {
        if (getNode(name) != null) {
            remove(root.getChildren().get(name.charAt(0)), name, 1);
            return true;
        }
        return false;
    }
    private boolean remove(Node node, String name, int pos) {
        char c = name.charAt(pos);
        
        // check the entire student: if there are children, do not delete!
        if (pos==name.length()-1) {
            Node nodeEDIT = node.getChildren().get(c);
            nodeEDIT.setMarks(new HashMap<>()); // reset the student's marks
            nodeEDIT.setIsName(false);  // mark it as a normal node without student data
            
            if (nodeEDIT.getChildren().isEmpty()) 
                // if the last node does lead to other paths (is a leaf),
                // then it can be deleted (return true)
                return true;
            else
                return false;
        }

        // if there are no other paths, maybe delete node
        // if the analyzed node is not the second last, navigate down
        if (pos < name.length()-1) {
            pos++;
            if (remove(node.getChildren().get(c), name, pos)) {
                Node nodeREM = node.getChildren().get(c);
                // if the node's child is not a student and it does not lead
                // to other paths, delete it
                if (nodeREM.getChildren().size()<2 && !nodeREM.isName()) {
                    node.deleteChild(c);
                    // and keep on removing nodes
                    return true;
                }
                else
                    // if the call to remove(...) returned false, keep on returning false
                    return false;
            }
        }

        // if there are other paths, do not delete the node (return false)
        return false;
    }
    
    /**
     * Remove the requested courses from a student.
     * This method accepts a data string array containing the name of
     * the student to whom remove grade-course pairs and the names of the
     * courses to remove.
     * 
     * Its time complexity is O(1): the student's node is first retrieved (if exists)
     * with a complexity of O(1), then the given modules are removed with again
     * a complexity of O(1).
     * 
     * @param data must contain the student's name at index 0 and the
     * module names at the other indexes
     * @return the number of successfully removed courses from the student
     */
    public int removeCourses(String data[]) {
        Node student = getNode(data[0]);
        
        if (student != null) {
            return student.deleteMarks(data);
        }
            
        return -1;
    }
    
    /**
     * Get a list of mark-student strings following the given course.
     * This method requires the input of a module and returns a list of strings
     * containing each the mark of a student and the student's name for the
     * given module. The list is already ordered by marks (high to low) and, where
     * marks are the same, alphabetically by name. The students are retrieved
     * from the Trie with a recursive method
     * 
     * The time complexity is O(n^2): first, all students are checked to see if
     * they follow the given course (O(n), where n is the number of students in the
     * database). Then the students that have the given module are put in two arrays,
     * one containing their names, one containing their marks. The arrays are then sorted
     * together using a particular bubble sort that checks for two condition: mark value
     * and alphabetical order. The sorting has a time complexity of O(n^2) where n is
     * the number of students following the given module.
     * 
     * @param module name of the course
     * @return a list of ordered strings to print on screen
     */
    public List<String> getStudents(String module) {
        // make a hashmap of student-mark pairs for all students taking the given module
        HashMap<String, Integer> students = new HashMap<>();
        students.putAll(getNodes(root, "", module));
        
        // separate the names and the marks in two arraylists for easy ordering
        ArrayList<String> stuNames = new ArrayList<>(students.keySet());
        ArrayList<Integer> stuGrades = new ArrayList<>(students.values());
        
        // temporary variables for swapping operations
        String nameTEMP;
        Integer gradeTEMP;
        
        // loop through the list n times
        for (int i=stuGrades.size()-1; i>0; i--) {
            // loop through the yet unordered list
            for (int j=0; j<i; j++) {
                // descending order for grades
                if (stuGrades.get(j)<stuGrades.get(j+1)) {
                    // name swap
                    nameTEMP = stuNames.get(j);
                    stuNames.set(j, stuNames.get(j+1));
                    stuNames.set(j+1, nameTEMP);
                    // grade swap
                    gradeTEMP = stuGrades.get(j);
                    stuGrades.set(j, stuGrades.get(j+1));
                    stuGrades.set(j+1, gradeTEMP);
                }
                // if the grades are equal, check the names
                if (stuGrades.get(j).equals(stuGrades.get(j+1))) {
                    if (stuNames.get(j).compareTo(stuNames.get(j+1)) > 0){
                        // returns positive int if the first string is
                        // lexicographically grater than the second.
                        
                        // name swap
                        nameTEMP = stuNames.get(j);
                        stuNames.set(j, stuNames.get(j+1));
                        stuNames.set(j+1, nameTEMP);
                        // grade swap
                        gradeTEMP = stuGrades.get(j);
                        stuGrades.set(j, stuGrades.get(j+1));
                        stuGrades.set(j+1, gradeTEMP);
                    }
                }
            }
        }
        
        // display the average mark of the module
        int average = 0;
        for (Integer mark: stuGrades)
            average += mark;
        average = average/stuGrades.size();
        
        System.out.println("The average for the "+module+" course is "+average);
        
        // put the two lists together to return them
        List<String> finalList = new ArrayList<>();
        for (int i=0; i<stuGrades.size(); i++)
            finalList.add(stuGrades.get(i)+"\t"+stuNames.get(i));
        
        return finalList;
    }
    private HashMap<String, Integer> getNodes(Node node, String name, String module) {
        HashMap<String, Integer> nodes = new HashMap<>();
        
        if (!node.getChildren().isEmpty())
            for (Node child: node.getChildren().values())
                nodes.putAll(getNodes(child, name+child.getValue(), module));
        
        if (node.isName())
            if (node.getMarks().containsKey(module))
                nodes.put(name, node.getMarksOf(module));
        
        return nodes;
    }
}
