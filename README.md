# marketo-challenge
Simple application for parsing json and resolving duplicate entries.

##Dependencies:

Java 8
GSON 2.8.5
JUNIT >= 5

##Use:

	Run from the command line with your input file path as the first argument. Pared down JSON will be placed in the working directory as "output.json".

##Notes:

	Records are compared in load order and compared against stored Id and Email entries. If a duplicate is detected then the application chooses which values to keep based on which has a more recent date, or which was loaded last if the dates are the same. All changes are logged to the console. Finally the reduced set of Records is serialized to a json and written to ouput.json in the working directory.