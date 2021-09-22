## API Changes

### Front End External

Changed the name of interpretResult to processResult since interpretResult became a private method
* minor
This was probably an uncecessary change as we should have just changed the methods in the other direction,
making processResult private and then have the public interpret result call it. 

The reason its the same is because we contorted controller to fit into this API. 

### Front End Internal 

Paths have changed to just be a list of positions that are  instead of a separate class

### Back End External

Added undo and redo 
- major change to allow for easy communication between front and back end for these complex commands


Added appliedChanges
- allows you to change the language, but is expandable to change any setting
- 

Proposed change: Give it a file with every turtle and state, user defined variables, and user commands
This will allow for the front end not to have to keep track of these saved preferences. Currently they 
are loaded form resources in both the front and back ends. 


### Back End Internal

Undo, Redo: The model keeps track of its previous states so they are tied tightly to the model.

clearVariables: not used, but could have a use

getUserCommand: split up the getters for this so you can split up a script and its arguments

The following were added so that the model can keep track of them
For everything in CommandResult that can be changed everything has a getter and a setter
Anything changeable in a command is accessible


makeCommandResults: 
there are a bunch of ways to make them for convenience
* major

addPaletteColor: is unused but we see potential for adding it, since the model might want to keep track
* minor


#### Interpreter - part of BackEndInternal

parseCommandsList : parser calls it to parse a script, 

parseForRetVal: Used only by askWith - ideally it would be removed, except that it can return a "hanging"
return value, whereas parseCommandList just gets rid of it.

setLanguage: Allows parser to know the language without the model 


### Summary

Overall we had to extend/add to the API for things we didn't know existed like that the penWidth would
need to be stored in the backEnd