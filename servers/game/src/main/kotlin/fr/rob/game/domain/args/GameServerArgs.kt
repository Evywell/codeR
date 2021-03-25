package fr.rob.game.domain.args

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import fr.rob.core.args.BaseArgs

class GameServerArgs(parser: ArgParser) : BaseArgs(parser) {

    val config = parser.storing(
        "-c",
        "--config",
        help = "use a specific config file"
    ).default<String?>(null)
}
