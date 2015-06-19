In order to use our replication of the k-reach index modification to the source code will be nescesary;
The project is a netbeans project, however, the java files should also work in any other enviroment.

To use the project, open the KReach.java file.
In the body of the main method, we first load our graph with the load method.
As an argument to this method any of the files specified at the top of KReach.java can be given.
Next, the budget b and a k value is specified.
The next step is to choose an index type.
KReachIndexBasic is the primitive k-reach
KReachIndexTwoLevel is the 2 level relaxed index
KReachIndexFinal is the final KReach index, that makes a choice between the other 2 indices

Upon object creation, the index is immediatly computed.

The code present after the index choice is an example of an experiment.
For a more involved example, please see the method 'K24681012CheckBigSets()', which computes a statistic on the k values ranging from 2 to 12 on the bigger datasets

the method .printResults() that every index has, prints all interesting statitics collected by the index, e.g. construction times, amount of queries etc.

Unfortunately, due to time constraints the source code is not documented/commented properly.