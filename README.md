# oxutils

OXUtils is a collection of some useful utilities used in proprietary JAVA projects made by the OXANDE SARL
company. It includes an hosting service for WordPress and some other side projects.

Consider the library as a collection of helpers mainly to be used as static methods.

IMPORTANT NOTE: THIS LIBRARY IS STILL IN DEFINITION AND CAN BE MODIFIED FROM A VERSION TO ANOTHER.

## PowerStream

This is a class to process a create a java.io.Stream that you can consume. The basic idea behind the implementation
is to have a producer created in a sperated thread which push values into the stream and a consumer which inherits of
the stream.

This is quite interesting when you want to process data in the background. One of its usage is to read a CSV file and
to push maps in the stream for the processing. Then the time the processing can be done during the reading of the file.
See the documentation of the class for an effective usage.

## ProgressWorker

It is a very basic class to track the progress of a long task. Each time you process a line, call the `progress` method
wich provide the number of times you called the method. This is useful to show how many lines you have processed so far.
Note the consumer should NOT have a side effect because it is called only every 15 seconds (or at the delay you specified).

## Safe

Is a useful class to overcome the "null" values. See documentation of the methods provided.


