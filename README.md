## Project profiling
![YourKit Logo](https://www.yourkit.com/images/yklogo.png)

For profiling, we are using YourKit tools.

YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of <a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a>,
<a href="https://www.yourkit.com/dotnet-profiler/">YourKit .NET Profiler</a>,
and <a href="https://www.yourkit.com/youmonitor/">YourKit YouMonitor</a>.

## Databases
1. Config
The architecture configurations (orchestrator, game nodes, ...

2. Players
All the dynamic data of a region: players info, instances, security, game tickets, ...

3. World
All the world info such as creatures, quests, maps, scripts, ...

## Migrator
### Launch migrations
```shell
make migrate
```
Will launch every pending migrations (players, config and world dbs).
### Create migration
To create a new migration, you'll need to type the following command
```shell
make migration-DB-create MigrationName
```
 - replacing `DB` by the database name (`players`, `config`, `world`) 
 - replacing `MigrationName` by the name of your migration (e.g `InitialStructure`)

### Create seeder (fixtures)
e.g.
```shell
make seed-create MIGRATOR_SEED_NAME=CharacterSeeder MIGRATOR_DB=players
```
 - The parameter `MIGRATOR_SEED_NAME` is used to configure the name of the seeder class.
 - The parameter `MIGRATOR_DB` is used to configure in which database the seed is created for (`players`, `world` or `config`)

## Sandbox
### Run a scenario
```shell
./gradlew :sandbox:run --args="SCENARIO_NAME"
```