## Migrator
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
