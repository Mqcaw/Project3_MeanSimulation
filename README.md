# Project3_MeanSimulation
This project was from a summer class I took*. I do not have the original task, but we had to create something simple with a derby database.

- [Apche derby](https://db.apache.org/derby/) database
- [JavaFX](https://openjfx.io/) for GUI
- Compiled with [Maven](https://maven.apache.org/)

Currently uncommented as that was not a requirement for the project.

## Explination

THe Main.java file starts with an init() function. This function establishes or creates a connection with the database then creates a new table. When the database is delt with it will add coulumns to JavaFX table. Finnally updates the table updateTable() function.

The updateTable() function clears the JavaFX table, query the database for stored table info. Updates the JavaFX table and add the Game Object to an observable list.

Other misc functions include errorEmptyField() and errorIntFormat() for errors in the input fields.

Quick desctrion of the Game object:
  My goal with the Game object was a simple thing to run that would give data to display. The Game object takes a trial name, a number for iterations, a max value for random numbers and an option for mean and accuracy. The objects run() function is only used in the constructor without mean or accuray since the mean and accuracy is only used for adding objects to the JavaFX table. When run a list of random integers will be created and the mean of that list is calculated and stored, then the accuracy of the measured mean is calculated based on the expected mean.

Ok the biggest chunk in this program, the start() function. The start function sets up the GUI objects and styles. then sets up events for the buttons: 
  The esiest first, the highestAccuracy button. Sets up connection to the database, querys the objects in decending order. Then loops throught the first 10 of the result and adds some shortened info to a JavaFX information alert.
  The resetButton, 




*I don't condone plagiarism and shall not be held accountable for any punishment for doing so. I purposely did not state which class this is for. This is purely for educational purposes and personal organization.
