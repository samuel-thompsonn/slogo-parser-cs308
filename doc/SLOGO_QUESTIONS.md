## Names
Samuel Thompson stt13
Austin Odell awo6
James Rumsey jpr31
Cary Shindell css57
## Reviewing planning questions:

1. What methods should the turtle have for "fd 50"?

	moveForward(int distance) that changes its internally stored coords
	() that returns the coordinates, this can be on or more getters
	turn(degrees) for various commands
	
2. What behaviors does the result of a command need to have to be used by the front end? *
	
	It needs to keep track of paths so that the visualizer can draw the paths for which the pen is down, it needs to keep track of if a command failed entirely, it needs to return the new position where the turtle will be drawn. Something needs to keep track of whether the pen was up or down.

3. How is the GUI updated after a command completes execution?
	The command is copied to a 'history' section away from where the user just typed. The graph needs to update to show the new paths and turtle location. Needs to say whether the command actually makes sense and/or worked

## Additional Questions
1. When does parsing need to take place and what does it need to start properly?

	All it needs is the String that is the user input. It takes place after the command is given and entered/submitted. The parser also needs to know how the command interacts with current values of expressions.