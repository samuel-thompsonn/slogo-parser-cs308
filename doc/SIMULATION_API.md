## Simulation API

Austin Odell, James Rumsey, Cary Shindell, Sam Thompson
awo6, jpr31, css57, stt13

The following is based on Team 21's Simulation module:
#### Should Not be In API
- setType in Cell (should be protected)
- getTimeSinceEat() (only used by Predator Prey grid)
- increaseAge (only used by Predator Prey)
- resetAge (only used by Predator Prey)
- setTimeSinceEat (only used by Predator Pray)


#### Should be in External API
- checkForUpdates() in abstract Grid class
- updateCells() in abstract Grid
- getGrid() in abstract Grid
- Constructors for Cell, Grid
- getType() in Cell


#### Should be in Internal API
- findNeighbors() in abstract Grid
- wrappedLoc() in abstract Grid (finds cells based on neighbors based on edge type)
- findEmptyCells() in abstract Grid
- getX(), getY() in Cell
- setX(), setY() in Cell

### API Description

The Internal API contains the Cell class, which is not abstract or an interface because different
simulation types are implemented in the classes that extend the Grid class. If we were to implement a new Simulation,
we would create an extension of the Grid abstract class and override the checkForUpdates() method
based on the rules for the simulation. If necessary, updateCells() could be overridden if required by the Simulation type so it
is available.

The External API should be able to return a 2D array of integer states rather than Cell classes,
since our visualization should only be concerned with the states (or colors) of the cells rather than
each of them as a Cell object. It would be useful to have a method called externally by the visualization that steps through the Simulation to be
used by the visualization.

#### Using External API to Load a New Simulation

Call the appropriate constructor (for classes that extend Grid) based on the XML configuration, construct the Grid, and call 
our step method to step through and update the Grid. Then, call getGrid() so we can display the result of the updates in the visualization.

#### Using Internal API

Extend the Grid class, override and implement checkForUpdates() based on the rules of the simulation and override updateCells() if required
by the simulation type.