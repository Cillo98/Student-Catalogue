/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grades;

import java.util.HashMap;

/**
 *
 * @author thoma
 */
public class Node {
    private final char value; // the character of this node
    private boolean isLeaf; // if it is the end of a tree branch
    private boolean isName; // if it contains student's data
    private HashMap<Character, Node> children; // set of its children
    private HashMap<String, Integer> marks; // if a student, contains its marks (in any)
            
    /**
     * Constructor of a student node.
     * This constructor simply initializes the node giving it the passed
     * character as value and setting it as non-leaf non-student and with
     * no children and no marks
     * 
     * @param character name of the node
     */
    public Node(char character) {
        value = character;
        children = new HashMap<>();
        isLeaf = false;
        isName = false;
        marks = new HashMap<>();
    }
    
    // GETTERS
    public HashMap<Character, Node> getChildren() {
        return children;
    }
    public char getValue() {
        return value;
    }
    public HashMap<String, Integer> getMarks() {
        return marks;
    }
    public boolean isLeaf() {
        return isLeaf;
    }
    public boolean isName() {
        return isName;
    }
    public String marksToString() {
        String marksString = "";
        
        // put the marks together in a nice way
        for (String module: marks.keySet())
            marksString += "\t"+module+": \t"+marks.get(module)+"\n";
        
        return marksString;
    }
    public Integer getMarksOf(String module) {
        return marks.get(module);
    }
    
    // SETTERS
    public void setLeaf(boolean value) {
        isLeaf = value;
    }
    public void setIsName(boolean value) {
        isName = value;
    }
    public void setMarks(HashMap<String, Integer> marks) {
        this.marks.putAll(marks);
    }
    
    /**
     * Delete the passed module from this node.
     * Given an array of module names, all corresponding modules
     * in this node's marks HashMap are deleted.
     * 
     * Its time complexity is obviously O(1).
     * 
     * @param modules names of the courses to delete
     * @return the number of successfully deleted module-mark pairs
     */
    public int deleteMarks(String[] modules) {
        int deletedMarks = 0;
        for (int i=0; i<modules.length; i++)
            // count how many modules/marks are successfully deleted
            if (this.marks.remove(modules[i]) != null)
                deletedMarks++;
        // return the deleted marks
        return deletedMarks;
    }
    
    /**
     * Delete on of this node's children.
     * Delete the connection between this node and the next node, provided
     * there is one.
     * 
     * Its time complexity is O(1).
     * 
     * @param c character of the child to delete
     */
    public void deleteChild(Character c) {
        this.children.remove(c);
    }
    
}