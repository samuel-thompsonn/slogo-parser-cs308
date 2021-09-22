parser
====

This project implements a development environment that helps users write SLogo programs.

Names: Austin Odell (awo6), James Rumsey (jpr31), Cary Shindell (css57), Sam Thompson (stt13)

### Timeline

Start Date: 2/19/20

Finish Date: 3/6/20

##### Hours Spent: 
Austin: 15 basic. 15 complete

James: 12 basic, 12 complete

Sam: 30 basic, 30 complete

Cary: 25 basic, 45 complete


### Primary Roles
Austin and Cary frontEnd and controller. Austin focused mostly on UI and preferences. 

Sam and James worked on the backend commands and parser. James focused primarily on implementation of commands, implementation of multiple turtles, and outputting changes to the model as command results.
Sam focused on making the parser, control commands, multiple turtle commands selection commands, and user-created commands, 
the file builder, and the ability to keep track of history so that undo and redo can modify
the model.


### Resources Used

Java Reflection Tutorial/Resources From 2/27 Lab on Reflection (for reflection in CommandFactory class)

Spike_parser from the 308 repo for using regex expressions to match
symbols. OODesign.com for design pattern overviews, such as the memento pattern.

### Running the Program

Main class: src/slogo/controller/Controller.java

Data files needed: All of src/slogo/frontEnd/Resources, src/resources.languages, src/slogo/backend/resources/commands.properties,
all other filesin backend/resources.

Features implemented:
- All of Basic
- Front End Complete: All of the basic extensions except for saving and loading preferences, which we 
started and are able to use for user defined commands and variables (and a few other preferences
like menu item and starting colors and image). Challenging extensions of changing
the language of commands in all displays (display text for some languages have limited translations,
but all command related text is translated as specified by the extension).
Undo/Redo is working. Animation is working. 
- Back End Complete: All additional commands, updating commands and model to allow for multiple turtles and
multiple active/inactive turtles, saving mementos of current model state, saving library of user-defined
variables and commands to an XML file.


### Notes/Assumptions

Assumptions or Simplifications: The size of a lot of UI components are fixed and thus the current application
window only fits on some screens. The window size was created assuming a 16:9 aspect ratio that is common
for computers. When using the "tell" command, adding an index creates only one new turtle, regardless of the number (i.e. tell 
[ 100 ] does not create 99 additional turtles in addition to turtle 0 and turtle 100). Any contents within brackets must be separated by 
a space from the bracket. User-defined commands are assumed to use correct syntax when saved,
but syntax errors are detected and reported when they are run.
We did not read the specification as requiring the ability for .slogo files
to be loaded and run. It is assumed that there doesn't need to be an error
message when a user fails to load or store a file, as long as the program doesn't crash.
It is not assumed that the user will be able to see printed statements in the console,
but some things may be printed for debugging purposes.

Interesting data files: 

Known Bugs: 

Extra credit:


### Impressions

I liked the assignment and the amount of freedom we had to do things our own way and decide on some 
difficult decisions. That said it definitely wasn't simple and there was some pretty complex logic /
algorithms that had to implemented for this to be successful. 

I liked thinking in terms of internal/external API design during this project because it allowed us to reduce the project into distinct components.
The front end is distinctly different from the back end and were developed independent of one another, but for the most part the two communicate well with each other.

The fact that there were still important piece sof information about how to put together
the project (MVC vs binding, etc.) after we had already planned the project meant
that it was possible to make a plan and then immediately disagree with it. This means we have
to choose between a better designed plan and sticking with the plan we submitted. We chose
to stick with MVC for this project because it better decouples the model and view.

The assignment specifications listed the ability to undo and redo in the 
front end specifications, but it's clear that it is the model's job to
keep track of state information, so it was really both a back end and front end feature.
Additionally, it was unclear what parts of file loading were 'preferences' and
which ones were in the back end 'library', so we originally had both the front end and
back end storing variables and commands into files.