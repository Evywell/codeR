# How instances work
## Definition
An instance is a single occurrence of a dimension. It is characterized by a map identifier and one or more zones.
It means that a same map can be loaded multiple times (in multiple instances) and an entity interacting with a specific instance
is not interacting with another.

Example: If I load a map `M` in two instances `A` and `B`. Every entity in `A` and `B` are on the map `M` but not in the same dimension. 
Entities in `A` interact with each other but not with entities in instance `B`. For `A`, `B` does not exist and vice versa.

## Database
To represent an instance in database, we'll work with these tables: 
 - `players.instances`
 - `placers.instance_zones`
 - `config.default_instances`

As a reminder, the `players` database is used to store all the volatile data. Data specific to a game session.
We store the default instances in `config` database because it is global to every world creation.

### Instances

## Orchestrator & Agent
