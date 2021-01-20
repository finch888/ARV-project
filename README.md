# ARV-project

ARV Desktop Application
-----------------------

This tool has been developed to help conduct an ARV session.
In case you are unfamiliar with ARV, it is an acronym for Associative Remote Viewing.
ARV is a method used to determine the outcome of binary events.
During an ARV session the two possible outcomes will be replaced by a substitute (color, shape, image, etc.)
and you are required to guess the substitute of the correct outcome. This procedure helps you to mitigate
any bias you might have towards either of the outcomes.
For more information on ARV please visit:
http://www.remoteviewing.hu/
https://rviewer.com/associative-remote-viewing-arv/


System requirements/Setup
-------------------------
The program requires JRE 8 to run.
In order to utilise all features SQLite also needs to be set up on the host system.
Although the app has only been tested on a Windows system, it has been developed with portability in mind.
There is no installation necessary in order to start using the application.

At the first start of the program a folder called "ARV" will be created in the user home directory.
It will contain all the necessary folder structure and files, including the SQLite database, if SQLite is set up.
The files that will be created during the previous process will be empty and the user is required the write their
prefered substitutes in them. Each entry needs to be followed by a semicolon.

These files can be found on the following paths:
 user home directory\ARV\Lottery\Config,
 user home directory\ARV\Sport\Colors\Config.
In the case of the Lottery files there are only two options: colors and shapes.
In the Colors directory, contrary to its name, there are music and people options available as well.
The "music.txt" file was meant to be for song titles, the "people.txt" is for names of famous people.

For some the above mentioned substitutes could work, they are certainly worth a try, but the most common
way of conducting an ARV session is with the use of images. The following folder is where the images
need to be saved by the user, the more the better:
 user home directory\ARV\Sport\Pictures\Config\Pics.
The supported formats are: bmp, gif, jpeg and png.


Features/Manual
---------------
The GUI of the application is based on a TabPane. It contains four tabs.

The first one is called "Lottery".
On this pane there are two ChoiceBoxes. The top one is for selecting the lottery type and the one below
is for selecting the substitute type.
The "shapes and colors" choice will associate the numbers to a combination of a shape and a color, 
for example "blue arrow". The other option is "colors only". This will give the numbers a substitute
consisting of a pair of colors. By pressing the "Generate Associations" button a file called "associations.txt"
will be saved in the following directory:
 user home directory\ARV\Lottery.
This will contain all the possible numbers with their randomly selected substitutes.

The next pane is "Sport".
This is where the details of a sporting event can be entered manually and saved.
The ChoiceBox allows the user to choose the substitute type. In addition to the two options mentioned
at the lottery section, here it is also possible to choose from "single color", "music" and "people"
categories if the relevant files contain the required data (see Setup).
The "Generate Associations" button can be found here as well, and has the same function as the lottery
one. However the directory where the "associations.txt" file will be saved, is different:
 user home directory\ARV\Sport\Colors.
Simultaneously the question, which the ARV session aims to answer, will be generated and displayed in
the TextArea.
The "Save Event" button will save the event details for future reference or for the next pane in a file
called "event details.txt". The file will be located in
 user home directory\ARV\Sport.

The pane, where only pictures can be used as substitutes is "Sport Pics".
The pictures will be selected from the directory mentioned in the Setup section.
The event details will be read from the "event details.txt" file.
The "Generate Associations" button will generate and display the question in the TextArea, randomly
select one image for each possible outcome, and save the associations in a file with the name of
"saved session.txt". It will be placed in the following folder:
 user home directory\ARV\Sport\Pictures.
The "Retrieve Last Session" button will read this file and reset the last session.
The two pictures with the outcomes associated with them can be viewed by pressing the "Show Image"
buttons.

The last pane is called "Select Events".
As mentioned before, the details of the sporting event can be entered manually, however it is more
convenient to get the details at this tab.
The "Download Events" button will download the upcoming events and display them in the TableView.
It uses the API behind the betting site: "tippmixpro.hu" to fetch the events in JSON data type, and
parses the response with the Jackson library.
In this version of the application only table tennis matches can be downloaded, as the whole Sport
section is geared towards this sport.
When the event details appear in the TableView, the names of the participants will not be displayed.
The reason for that is to avoid any bias toward one or the other.
Once an event is selected the "Save Event" button will be enabled. Once it is pressed the
"event details.txt" file will be generated again and saved in the same directory as mentioned
previously. Saving the details this way will also enable the CheckBoxes called "Use Event ID" on
the previous two panes. Checking these boxes will result in the question being generated only
with the use of the ID of the event and no further details.



Thank you for taking a look at my application!
