# Student Catalogue
This program's goal is to create a database of students with information regarding the modules they are taking and their marks. The program must make use of fast, robust and memory-efficient algorithms.

## Data Structure
As the number of courses is very limited, my focus on optimization is on storing the names of the students in a memory efficient and fast data structure. The best options are three:

- A *HashMap* of *<Name, Course/Mark>* pairs, that allows insertion and retrieval in *O(1)* time. However, a dataset with a limited amount of entries (few students) would have wasted a lot of space, while reducing the range of the Hash function would have rose the risk of over-full buckets

- A B-Tree could therefore be a more efficient data structure, with most operations executable in *O(1)* time, however there would be a little waste of memory to store both the value of the tree nodes and the name of the associated students

- The best option is then to use a **Trie** data structure, a particular kind of B-Tree where nodes are *Characters* with a *HashMaps* of *<Character, Node>* pairs, in such a way that each node has a Character value and a set of children nodes. By traversing down a tree and keeping track of the encountered nodes, words are created. As the picture below shows, the Trie stores four names: Alice, Bob, Bobby and Boray. All operations on the Trie have a time complexity of *O(k)*, where k is the height of the Trie, so the length of the longest name. As names are short, it can be considered that the time complexity is *O(1)* for all insertion, update, deletion and retrieval operations. The only exception is made by the List method, that uses a sorting algorithm with two criteria to order marks and students, and has a time complexity of *O(n<sup>2</sup>)*, with n the number of students in the dataset taking the specified course.