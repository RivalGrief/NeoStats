# NeoStats 📊

Advanced player statistics tracker for Minecraft 1.16.5+

## Features
- 📈 Track player statistics (kills, deaths, blocks, etc.)
- 🏆 Top players leaderboard
- ⚡ Auto-save system
- 🔧 Configurable settings
- 🎯 Easy-to-use API for developers

## Commands
- `/stats [player]` - View player statistics
- `/topstats <kills|blocks|time|kd>` - View top players
- `/neostats reload` - Reload configuration (OP)

## Permissions
- `neostats.view` - View own statistics
- `neostats.others` - View other players' statistics
- `neostats.top` - Use top commands
- `neostats.reload` - Reload plugin configuration

## Installation
1. Download the latest release
2. Place the jar file in your `plugins` folder
3. Restart your server

## For Developers
```java
NeoStatsAPI api = NeoStats.getAPI();
int kills = api.getPlayerKills(player);