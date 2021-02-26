# Android_island3D
###### *This game is really far from a release and no longer updated.*

### Concepts
This is a simple strategy game based on a famous boargame about island exploration and search for treasures by 2-4 players each represented as a pirate team.
The island consists of a set of tiles (N*N size; 4<N<20), each tile has it's own unique features (traps, treasures, blank tiles, fortresses etc.) Each team has 3 players and 1 ship. Ship
is a base for players, also is a place where to bring all of the treasures to. Each treasure brought to the ship counts as 1 score point. The game ends when no treasures are left on
the island. The winnder is defined by the highest score (that one, who brought the biggest amount of gold to his base). 
At first, evey chunk of the island is unknown. To uncover a chunk players have to step on it (and probably get trapped). Players can move between the tiles like in chess (8 directions 1 tile length).
Also players can kill eachother, if the enemy player is located on the neighbour tile. (or more than 1 enemy player, then all of them will be killed at once). Killed players respawn on their ship.
Ship can't be destroyed, all of the enemy players nearing the ship in 1 tile length will be permanently killed (without respawn). 

### Structure
MainActivity is found in com/test package. The code is split into logical modules: RenderEngine module - in charge of rendering surprisingly, Game module - in charge of all game logic,
Menus module - in charge of GUI **building**, not rendering. Shaders are located in assets as well as game resources.

### Possible **TODO** list
Add sounds, fix tiles, add more tile types and mechanics, add animations, add new player and ship models, add better GUIs, fix stupid freeze bug on exit from pause menu.

### Development
This game is written with **OpenGL ES**.

Development is discontinued.
