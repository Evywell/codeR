## Migrator
### Create seeder (fixtures)
e.g.
```shell
make seed-create MIGRATOR_SEED_NAME=CharacterSeeder MIGRATOR_DB=players
```
 - The parameter `MIGRATOR_SEED_NAME` is used to configure the name of the seeder class.  
 - The parameter `MIGRATOR_DB` is used to configure in which database the seed is created for (`players`, `world` or `config`)

## Nodes
### Processes
 - **DevAuthenticationProcess**: handle authentication for development purposes.  
   `checkAuthentication` method always returns true. You must specified a `userId` in the message to log as this user.  
   **Extends**: AuthenticationProcess (abstract)  
   **Registered in**: `fr.rob.login.security.SecurityModule.registerAuthenticationProcess`  
   **Examples**:
 ```kotlin
    val message = AuthenticationProto.DevAuthentication.newBuilder()
      .setUserId(1234)
      .build()

    val process = DevAuthenticationProcess()

    process.authenticate(session, message) // Always return true and set the session's userId to 1234
```
 - **JWTAuthenticationProcess**: handle authentication using JWT token validation.  
   You must specified a JWT token in the message.  
   **Parameters**: The constructor takes a `JWTDecoderInterface` object used to decode the JWT token  
   **Extends**: AuthenticationProcess (abstract)  
   **Registered in**: `fr.rob.login.security.SecurityModule.registerAuthenticationProcess`  
   **Examples**:
```kotlin
    val jwt = "..."
    val message = AuthenticationProto.JWTAuthentication.newBuilder()
      .setToken(jwt)
      .build()

    val process = JWTAuthenticationProcess(...)

    process.authenticate(session, message)
```
