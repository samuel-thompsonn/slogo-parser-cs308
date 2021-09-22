## Java Collections API
Austin Odell (awo6@duke.edu)
James Rumsey (jpr31@duke.edu)
Cary Shindell (css57@duke.edu)
Sam Thompson (stt13@duke.edu)


### Purpose
Allows for you to write your code without having to specify a specific data structure instead you can
just pass around higher-level data structures.
Also this allows for commonalities across the different types of implementations. 

####Mistakes
Mistakes are generally easy to avoid. Some possible mistakes can arise from not understanding the core 
data structures that these implement.

####Interfaces 
Linkedlist implements Deque, and List
It implements list so that you can index it. 
It implements Deque because it allows for easy insertion, more efficiently than a list. 

#### Implementation for Collection Class
Set has 9 different implementations.
All of the 9 classes implement a lot of shared methods. Also using interfaces help reduce duplicated 
code and increase flexibility. 

#### HashSet
HashSet has 3 levels of superclasses 
The first level is object, because everything is an object!
The second level is 
AbstractSet 

#### Utility Classes
It allows you to know methods will exist on a pretty generic type without having to make sure to implement
the method in each implementing class. 
Example Collections.sort and ArrayList.sort 
Using the more generic one increases re-usability    
