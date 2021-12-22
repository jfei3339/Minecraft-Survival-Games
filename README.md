# Minecraft-Survival-Games

This is a Minecraft server plugin I designed to host my own version of the gamemode "Minecraft Survival Games", which has been a very popular gamemode on Minecraft for nearly a decade. The gamemode is based off the Hunger Games trilogy by Suzanne Collins. The server is written in Java.
I hosted this server for a few months for a few hundred community members from the old MCGamer.net community, which I spent several years of my childhood playing. The color scheme and gameplay is heavily inspired
by MCGamer.net's version of Survival Games, but the code is my own.

## Gameplay Description

When the server is first started, any players who join are teleported to the lobby. Here, players can vote for which maps they want to play. 
Maps available by issuing the command "/vote" (or "/v"). To vote for a map, players type "/vote #" (or "/v #"), where '#' is the number of the map.

A minimum of 6 players is needed to start the game. The maximum number of players allowed is 24 (as in the Hunger Games, where there are 24 tributes/participants). There is a countdown clock so that everytime has a chance to vote for a map.

The players are then teleported to the map with most votes. The player spawn positions form a circle around a "cornucopia," just like the original Hunger Games. If there are multiple winning maps, the map first listed wins.

Once the game starts, the players are free to roam the map. The objective is to kill all players and be the last man standing. Players get weapons, armor, food, etc.
in chests scattered and hidden throughout the map to help them survive and kill other players.

If there are only 3 players remaining, or if a sufficiently long period of time has passed, the game transitions to "Deathmatch." In deathmatch, all players remaining (usually <= 3) are teleported to their spawn locations to kill each other. If a player tries to escape this enclosed region, lightning will strike them to death.

If a player wins, the game ends and there is a celebration with fireworks. If time runs out and there is more than one player still alive, the game also ends without celebration.

## Features

The server has a custom leveling system based on player experience. Killing a player gives one 10 experience points and winning a game gives one 200 experience points. Player statistics such as experience points, wins, kills, deaths, kill/death ratio, etc. are stored in a MySQL database.

A player can use the command "/stats" to check these stored statistics. The command "/stats <playername>" can be used to view the stats of another player.
  
Information such as the game state, the number of players remaining, player name, and time left is shown on a player scoreboard.
  
Players who join during the middle of a game become invisible spectators who cannot interact with the players still alive. 
A list of players and spectators can be seen using the "/list" command.

## Demo Video
  
Here is example gameplay footage (click the image below to see the Youtube video). The server/database were both running on my local machine, so the player stats are pretty much blank.

[![SURVIVAL GAMES DEMO](http://img.youtube.com/vi/XVueaDvkujM/0.jpg)](http://www.youtube.com/watch?v=XVueaDvkujM "Survival Games DEMO")
