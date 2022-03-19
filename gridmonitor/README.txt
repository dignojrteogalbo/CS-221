****************
* Project Grid Monitor
* CS 221
* 21 January 2021
* Digno JR Teogalbo
**************** 

OVERVIEW:

 Grid monitoring program.


INCLUDED FILES:

 * GridMonitor.java - source file
 * GridMonitorInterface.java - source file
 * GridMonitorTest.java - source file
 * README.txt - this file


COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac *.java

 Run the compiled test file with the command:
 $ java GridMonitorTest

 Or run the compiled source file with the command with filename arguments:
 $ java GridMonitor example.txt

 Console output will give the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 The GridMonitor constructor takes in the filename including extension. A scanner 
 will attempt to open the file with that filename from the directory of the 
 source. In the constructor, it will read the first line and then a scanner will 
 read the next two integer values. From these integer values, it will set the 
 variables height and width, these variables are the dimensions of the grid it 
 will read from. Next the constructor will read every double value and set the 
 values of a double array from the set width and height. In case the magnitude of 
 the value exceeds the maximum double size divided by four, it will throw a 
 FileNotFoundException with the message "Value magnitude too large." This is to 
 prevent an overflow to infinity. In case there is no next line, integer, or 
 double value when reading the file, the constructor will catch the 
 NoSuchElementException and throw a FileNotFoundException with the message 
 "Invalid file formatting."
 
 The GridMonitor will perform all of the calculations using the values from the 
 created baseGrid double array. The baseGrid double array will not be mutated at 
 all and it's values will be copied over when used for computation. The toString 
 method will return a string with all of the grid values from all of the methods.


TESTING:

 I tested the program with the given tests and then moved to develop the program 
 with other edge cases. I handled when there was a file that exists but had no 
 content. I handled when the magnitudes of the values were extremely large and 
 extremely small. I handled when the file had missing or misformatted content. In 
 order to test these, I created separate plain text files with the specific cases 
 in mind, and then analyzed the output from the toString method.


DISCUSSION:
 
 I had to learn about how to handle exceptions thrown by methods. Specifically 
 the next method and its related methods. By understanding how these exceptions 
 worked, I used that knowledge to throw my own exceptions with my own messages. 
 The difficult part about testing was discovering the kinds of edge cases that I 
 could encounter. I did not know how to handle them, so I terminated the process 
 whenever an exception came up and gave a message which hopefully gave insight 
 into why the program broke. I did not know whether to replace missing or large 
 values, nor did I know whether to ignore or resize extraneous content. If I were 
 to handle these cases better, I would need more specifications on how they 
 should be handled.
