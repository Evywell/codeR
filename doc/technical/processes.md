# Core project
## Security
- **SecurityAttemptProcess**: in charge of verify all the attempts of various subjects.
  **Parameters**: The constructor takes a `SecurityBanProcess` object to perform bans if necessary
  **Registered in**: `fr.rob.core.security.attempt.SecurityAttemptProcess`
  **Examples**:
```kotlin
    val securityProcess = SecurityAttemptProcess(...)
    val context = AuthAttemptContext(session, userId, ...)

    securityProcess.execute(context)
    // The security process will ban the session and block the user if the maximum of auth fail attempts is reached 
```
# Login project
## Security
- **DevAuthenticationProcess**: handle authentication for development purposes.  
  `checkAuthentication` method always returns true. You must specified a `userId` in the message to log as this user.  
  **Extends**: AuthenticationProcess (abstract)  
  **Registered in**: `fr.rob.login.security.SecurityModule.registerAuthenticationProcess`  
  **Examples**:
 ```kotlin
    val message = AuthenticationProto.DevAuthentication.newBuilder()
      .setUserId(1234)
      .setAccountName("Evywell#0000")
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

- **AccountProcess**: Manage account actions.
  **Parameters**: The constructor takes an `AccountRepositoryInterface` object used to query the database
  **Registered in**: `fr.rob.login.LoginApplication.run`
  **Examples**:
```kotlin
    val accountRepository = ...
    val process = AccountProcess(accountRepository)
    
    val account = process.retrieveOrCreate(123, "Evywell#0000")

    ...
```

## Character
- **CharacterCreateProcess**: handle the character creation
  **Parameters**: The constructor takes a `CharacterRepositoryInterface` object used to query the database
  **Registered in**: `fr.rob.login.LoginApplication.run`
  **Examples**:
```kotlin
    val characterRepository = ...
    val process = CharacterCreateProcess(characterRepository)

    val canCreateAccount = process.canCreate(session, character)

    if (!canCreateAccount.hasError) {
        process.create(session.account.id, character)
    }
```

- **CharacterStandProcess**: handle the character stand of a session
  **Parameters**: The constructor takes a `CharacterStandRepositoryInterface` object used to query the database
  **Registered in**: `fr.rob.login.LoginApplication.run`
  **Examples**:
```kotlin
    val process = CharacterStandProcess(characterRepository)
    
    val stand = process.createStandFromSession(session)
    // stand contains all the characters of the account's session
```
## Session
- **SessionInitializerProcess**: initialize the session by setting useful properties
  **Parameters**: The constructor takes a `CharacterRepositoryInterface` object and an `AccountProcess`
  **Registered in**: `fr.rob.login.LoginApplication.run`
  **Examples**:
```kotlin
    val process = SessionInitializerProcess(characterRepository, accountProcess)

    process.execute(session)

    // New properties are stored in the session
```
