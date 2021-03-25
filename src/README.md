# Movie Theater Seating Challenge - 2020

### Building and Executing the Program

This program was developed in Java and designed to be compiled into a single executable jar file.

Steps:
1. Download the src folder of the project and navigate to it in your terminal.
2. Compile all the Java files:
```shell
  javac @sources.txt
```
3. Package the compiled classes into an executable Jar file:
```shell
  jar cmvf META-INF/MANIFEST.MF theater.jar @classes.txt
```
4. Run the program and its tests via the jar file by specifying an input file:
```shell
  java -jar theater.jar "input.txt"
```

### Assumptions / Algorithm Trade-Offs
1. Assumption: Middle-row seats are preferable to back-row seats, and back-row seats are preferable to front-row seats 
   (at least in my experience :) )
   Trade-off: Seats in the middle of the row are better than seats at the edges of the row, but in order to maximize the
   number of customers in the theater, customers have to be placed the edges first to reduce the amount of buffer seats.
2. Assumption: Customers would prefer to have their groups placed together in the same row, whenever possible.
   Trade-off: Keeping groups of customers together in the same row is sometimes less efficient than breaking them up. 
3. Assumption: Groups earliest in the reservation list get the better seats and tend to get placed in the same row together.
   Trade-off: The priority given to the first groups in the reservation list may not be as efficient as placing
   the largest groups of customers first.
4. Assumption: When the theater is nearing capacity, groups of customers would prefer to be placed separately, wherever 
   there is a seat available, as opposed to catching a different showing. 
   Trade-off: More customers can be fit into the theater, but customers will be less happy with being split up.
   
