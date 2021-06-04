package fr.rob.core.test.cucumber.service.checker

import fr.rob.core.test.cucumber.service.Message

interface CheckerInterface {

    fun resolve(message: Message): Boolean
}
