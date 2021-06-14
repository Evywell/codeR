package fr.rob.cli.security.auth

import com.google.protobuf.Message

interface CredentialsHolderInterface {

    fun getCredentials(): Message?
}
