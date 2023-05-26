# MinigamePlugin

The minigame plugins goal is to create playable minigames in Minecraft

## Installation

Place in the ```plugins``` folder of your ```spigot``` (or any spigot fork) server.

## Usage
Arena data is stored in config.

```
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
  0:
    reset: false
    game: 'PVP'
    world: 'Map0'
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
```required-players``` and ```max-players``` are the minimum and maximum amount of players, who can play a mini-game\
```countdown-seconds``` default countdown seconds for a mini-game. Get reduced when the game is full or most/all players on the server are playing\
```lobby-spawn``` the spawn players are teleported to when joining or leaving a game\
```arenas``` a list of arenas

### Arena configuration
```reset``` currently mostly unused - set to false\
```game``` the type of game, there are 4:\
```PVP```
```SHOVELSPLEEF```
```KNOCKOUT```
```BEDWARS```\
\
```PVP``` is a normal Free-for-all battle with kits and no respawning\
setup: just an arena\
```SHOVELSPLEEF``` is Spleef, where you mine under other players to knock them in lava.\
setup: really hard to set up. Run the game once, and build the arena to the shape of the snow blocks. Add lava under them.\
```KNOCKOUT``` in knockout you hit other players with a stick to knock them in lava\
setup: a floating ring over lava, a bit like in Sumo duels. Can be made of ice.\
```BEDWARS``` requires a very specific config setup. Game setup will be explained later.\
\
```world``` The world in which the arena is located\
```spawns``` Possible spawn locations. Selected randomly.\
```respawn``` Respawn location. Player is teleported there after death\
\
WARNING!\
```SHOVELSPLEEF``` may damage terrain around it as before every game it places snow in a large rectangle. Coordinates cannot yet be changed.\

### BedWars file configuration
```Coming soon```

## License

[GPL3](https://choosealicense.com/licenses/gpl-3.0/)
