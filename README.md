# MinigamePlugin

The goal of this plugin is to create playable minigames in Minecraft\
Build the project with maven

## TODO: Coming soon
Feel free to send me suggestions for new features\
\
Planned features:\
More minigames:\
Party games\
TNT Run\
SkyWars\
Zombies\
\
Improve Knockout\
Fix bugs\
Add party system\

## Installation

Place built ```.jar``` file in the ```plugins``` folder of your ```spigot``` (or any spigot fork) Minecraft server.

## Usage
Arena data is stored in config.

```yaml
welcome-message: 'Welcome to the server! :)'
current-season: 1
required-players: 2
maximum-players: 8
countdown-seconds: 120
lobby-spawn:
  world: 'world'
  x: 0.5
  y: 64.0
  z: 0.5
  yaw: 180
  pitch: 0
arenas:
0:
    reset: false
    game: 'PVP'
    world: 'Map0'
    game-subtype: default
    spawns:
      0:
        x: 0.5
        y: 64.0
        z: 0.5
        yaw: 0
        pitch: 0
    respawn:
      x: 0
      y: 64
      z: 0
      yaw: 180
      pitch: 0
```

the ```welcome-message``` will be show to anyone joining the server.\
```current-season``` per player statistics data is stored seperately for each season\
```required-players``` and ```max-players``` are the minimum and maximum amount of players, who can play a mini-game\
```countdown-seconds``` default countdown seconds for a mini-game. Get reduced when the game is full or most/all players on the server are playing\
```lobby-spawn``` the spawn players are teleported to when joining or leaving a game\
```arenas``` a list of arenas

to view your current coordinates in minecraft press ```F3``` or ```Fn + F3```

### Arena configuration
```reset``` currently mostly unused - set to false\
```game``` the type of game, there are 4:\
```PVP```
```SHOVELSPLEEF```
```KNOCKOUT```
```BEDWARS```\
```game-subtype``` set to default
\
```PVP``` is a normal Free-for-all battle with kits and no respawning\
setup: just an arena\
```SHOVELSPLEEF``` is Spleef, where you mine under other players to knock them in lava.\
setup: ~~really hard to set up. Run the game once, and build the arena to the shape of the snow blocks. Add lava under them.~~ the setup is different than most other minigames. I have specified it below, under ```Spleef file configuration```\
```KNOCKOUT``` in knockout you hit other players with a stick to knock them in lava\
setup: a floating ring over lava, a bit like in Sumo duels. Can be made of ice.\
```BEDWARS``` requires a very specific config setup. Game setup will be explained later.\
\
```world``` The world in which the arena is located\
```spawns``` Possible spawn locations. Selected randomly for each player. Spawning logic will be rewritten.\
```respawn``` Respawn location. Player is teleported there after death\


#### WARNING!
```SHOVELSPLEEF``` ```spleef_old``` game-subtype may damage terrain around the arena, because before every game it places snow in a large rectangle. Coordinates cannot yet be changed.\
I reccomend using only 1 spawn with ```SHOVELSPLEEF``` when using this subtype.\
Otherwise you may run into more bugs. Using the ```default``` subtype allows changing the coordinates of the area\

### Spleef file configuration
```2:
    reset: false
    game: 'SHOVELSPLEEF'
    world: 'Map1'
    game-subtype: default
    spawns:
      0:
        x: 1000.5
        y: 115.0
        z: 1000.5
        yaw: 0
        pitch: 0
    corners:
      1:
        x: 1025
        y: 114
        z: 1025
      0:
        x: 975
        y: 120
        z: 975
    respawn:
      x: 1000
      y: 150
      z: 1000
      yaw: 180
      pitch: 0
```

```corners``` The corners of the arena. The lowest ```Y``` layer will be replaced by snow. Players cannot place blocks outside this area

### BedWars file configuration
```yaml
welcome-message: 'Welcome to the server! :)'
required-players: 2
maximum-players: 8
countdown-seconds: 120
lobby-spawn:
  world: 'world'
  x: 0.5
  y: 64.0
  z: 0.5
  yaw: 180
  pitch: 0
arenas:
  4:
    reset: false
    game: 'BEDWARS'
    world: 'Map4'
    x: -186.5
    y: 62.00
    z: -392.5
    yaw: 90
    pitch: 0
    generators:
      0:
        type: base
        x: 0.5
        y: 99.75
        z: -92.5
      1:
        type: base
        x: 0.5
        y: 99.75
        z: 93.5
      2:
        type: emerald
        x: 0.0
        y: 100.5
        z: 0.0
      3:
        type: diamond
        x: 32.0
        y: 100.5
        z: 0.0
    corners:
      1:
        x: 124
        y: 85
        z: 124
      0:
        x: -124
        y: 145
        z: -124
    beds:
      red:
        x: 0
        y: 100
        z: 74
        facing: south
      blue:
        x: 0
        y: 100
        z: -74
        facing: north
    spawns:
      red:
        x: 0
        y: 100
        z: 90.5
        yaw: 180
        pitch: 0
      blue:
        x: 0
        y: 100
        z: -90.5
        yaw: 0
        pitch: 0
```
\
```generators``` A list of the BedWars item generators.\
There are 3 types: ```base```, ```diamond``` and ```emerald```\
```corners``` corners of the map. During the game, blocks cannot be placed outside of it by players. Game blocks (Red wool, blue wool, end stone, oak planks etc.) will be removed after the game ends.\
```spawns``` spawns for members of each team. Currently there are only 2 teams: ```red``` and ```blue```\
```beds``` locations of each team bed. Options for ```facing```: ```north```, ```south```, ```east``` and ```west```

#### BedWars item shop
Currently this plugin does not yet spawn an npc. To create the item shop use a plugin like [```Citizens```](https://github.com/CitizensDev/Citizens2/) or [```zNpcs```](https://github.com/gonalez/znpcs) to create an npc. The item shop can be opened with the command: ```/shop bw```. This command can only be run by people with operator permissions in a BedWars arena\
Attach the command to an npc and make it run as the player that clicked it with an override to allow operator permissions.

## Usage and commands
In game, use ```/arena``` to view arena command help.\
```/arena list``` show a list of available arenas\
```/arena join <id>``` can be used to join an arena\
```/arena leave``` leaves the current arena\
```/arena shop``` opens the (unfinished and not fully working) arena coin shop\
```/arena team``` allows you to select your team in team games (like bedwars). If you do not select your team, it will be done for you when the game starts\
```/arena kit``` allows you to change your kit in ```PVP``` and ```SPLEEF```\
```/lobby``` teleports you back to the lobby

## License

[GPL3](https://choosealicense.com/licenses/gpl-3.0/)
