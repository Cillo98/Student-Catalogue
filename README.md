# Student Catalogue
This program's goal is to create a database of students with information regarding the modules they are taking and their marks. The program must make use of fast, robust and memory-efficient algorithms.

## Data Structure
As the number of courses is very limited, my focus on optimization is on storing the names of the students in a memory efficient and fast data structure. The best options are three:

- A *HashMap* of *<Name, Course/Mark>* pairs, that allows insertion and retrieval in *O(1)* time. However, a dataset with a limited amount of entries (few students) would have wasted a lot of space, while reducing the range of the Hash function would have rose the risk of over-full buckets

- A B-Tree could therefore be a more efficient data structure, with most operations executable in *O(1)* time, however there would be a little waste of memory to store both the value of the tree nodes and the name of the associated students

- The best option is then to use a **Trie** data structure, a particular kind of B-Tree where nodes are *Characters* with a *HashMaps* of *<Character, Node>* pairs, in such a way that each node has a Character value and a set of children nodes. By traversing down a tree and keeping track of the encountered nodes, words are created. As the picture below shows, the Trie stores four names: Alice, Bob, Bobby and Boray. All operations on the Trie have a time complexity of *O(k)*, where k is the height of the Trie, so the length of the longest name. As names are short, it can be considered that the time complexity is *O(1)* for all insertion, update, deletion and retrieval operations. The only exception is made by the List method, that uses a sorting algorithm with two criteria to order marks and students, and has a time complexity of *O(n<sup>2</sup>)*, with n the number of students in the dataset taking the specified course.

![Data Structure visualization](/res/trie.png?raw=true "Structure of the Trie of student Nodes")

## Efficiency
The Trie allows a very efficient use of space, as datasets with few entries are stored in a little space, and datasets with big amounts of entries are stored very efficiently in a tree, without wasted space for the names of the students. In a Trie with over 450.000 entries a specific node can be retrieved in as few as 4 operations. In fact, in the best-case scenario values are retrieved in log<sub>26</sub>(n) operations, where n is the number of entries in the dataset. In the worst-case scenario, values are retrieved in k operations, where k is the height of the Trie. In the graph below, it is assumed that the longest name in the database is made of 20 letters (it’s a very long name!).

In the following graphs, the red line is the worst-case scenario, the blue line is the best-case scenario. On the x-axis there is the number of entries in the database, on the y-axis the number of operations to insert, update and delete a student.

![First Graph](/res/graph_1.png?raw=true "Time complexity for small datasets")

For smaller datasets (up to 60 entries) it can be seen that the worst-case scenario is fixed at 20 (20 is considered the maximum name length), while the best-case scenario does not go over 2 operations. The data structure is both time and space efficient.

![Second Graph](/res/graph_2.png?raw=true "Time complexity for large datasets")

For bigger databases the situation does not change: the worst access time is still 20, the best access time is 4 for a dataset with over 280.000 entries. The dataset is time and space efficient even for huge datasets.

## Errors Handling
Errors are handled easily: try-catch structures are used to avoid crashes caused by the user’s misunderstanding of the parameters of functions and null values are supported throughout the programme in case the user enters non-existing values. In most cases values’ existence is checked during the input phase and the user is asked to enter values again if necessary. 

## Methods Overview
Following is the pseudo code of the most important methods in the program, those that determine the overall speed and efficiency of the program.

### Insertion
```
METHOD insert(name, pairs<Course, Mark>):
    node <- root

    FOR every char IN name
        children <- node.children

        IF char NOT IN children
            children <- char
            node.is_leaf <- FALSE

        node <- node.children.get(char)

    node.is_student <- TRUE
    node.marks <- marks

    IF node.children IS empty
        node.is_leaf <- TRUE
END.
```

### Update
```
METHOD update(name, pair<Course, Mark>[]):
    node <- get_node(name)
    node.marks <- marks
END.
```

### Deletion
```
METHOD delete(name, index, node):
    node <- node.children.get(name.charAt(index))

    IF (node IS NOT null) AND (index < length(name))
    	IF delete(name, index++, node)
            node.children.delete(node)
    ELSE
    	IF node HAS NOT children
	    	RETURN true
        ELSE
            IF index = name.length
            	Delete node’s marks
            RETURN false
END.
```

### Search
```
METHOD search(name):
    node <- root

    FOR every character char IN name
        IF char IN node.children
            node <- node.children.get(char)
        ELSE
            RETURN null
    
    RETURN node.marks, node.courses
END.
```

### List
```
METHOD list(course):
    FOR every node in Trie
        IF course IN node.courses
            names <- node_name
            courses <- course

    sort the two lists concurrently
    calculate the average marks per student

    FOR i IN 0..names.lenght
        PRINT "names[i]: marks[i]"
END.
```