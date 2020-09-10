package net.sourcebot.impl.command

import net.dv8tion.jda.api.entities.Message
import net.sourcebot.api.command.RootCommand
import net.sourcebot.api.command.argument.Arguments
import net.sourcebot.api.response.InfoResponse
import net.sourcebot.api.response.Response
import java.time.Instant
import kotlin.math.abs

class TimingsCommand : RootCommand() {
    override val name = "timings"
    override val description = "Show bot timings."
    override val aliases = arrayOf("ping", "latency")
    override val permission = name

    override fun execute(message: Message, args: Arguments): Response {
        val sent = message.timeCreated.toInstant().toEpochMilli()
        val now = Instant.now().toEpochMilli()
        val difference = abs(sent - now)
        val gateway = message.jda.gatewayPing
        val rest = message.jda.restPing.complete()
        return InfoResponse(
            "Source Timings",
            "**Command Execution**: ${difference}ms\n" +
            "**Gateway Ping**: ${gateway}ms\n" +
            "**REST Ping**: ${rest}ms"
        )
    }
}