README.txt

**** Supporting Materials ****

 * - Example data that can be used to test the program can be found at:

     https://drive.google.com/drive/folders/0BwUgWn0t0t0rc1lVMDU5UHJFQk0

**** Warnings ****

 * - the assumption is made that this program will be tested on a mac.

 * - the FinalYearProjectFolder should be place in “/home”

 * - Everything needed to run the application can be found in the "/dist" folder

 * - input.txt should be stored in the “ConversationAnalysis” folder and
     “ConversationAnalysis/dist” folder otherwise program won’t work correctly

 * - beware if the program is being edited in a ide, if the program is clean and
     built the input.txt is removed from the list folder, if so it is in the main
     folder of the program, it must be put back or errors will occur.

**** running the program ****

To run the application:

    1 - place the FinalYearProject folder in your home directory

    1 - open the input.txt file found in the ”ConversationAnalyser/dist" 
	folder of the program

    2 - make sure that all of the data in there is correct especially the path
	to the classifier models folder. 
		
		* manipulate the emotion names and graph min and max to change the
		  data displayed in the program.

    3 - go to the "dist" folder and double click on ConversationAnalyser.jar file

    4 - the program is loaded to the home page, use the browse buttons to load 	
 	 files to be analysed.
        	* - example files can be found under "/dist/example" 

    5 - load the files and click submit.

    6 - the main page is loaded, you can now use the program.

**** text file format ****

The format of the input.txt file should be as follows:

Emotions: happy,anger,fear,sad,surprise,disgust
Graph-Max: 1.0
Graph-Min: 0.0
Classifier: SVMClassifier
Classifier-models: /Users/Rhys/NetBeansProjects/ConversationAnalyser/models