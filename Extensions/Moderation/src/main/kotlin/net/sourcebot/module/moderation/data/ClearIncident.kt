package net.sourcebot.module.moderation.data

import net.dv8tion.jda.api.entities.*
import net.sourcebot.api.formatted
import net.sourcebot.api.response.InfoResponse

class ClearIncident(
    private val guild: Guild,
    override val id: Long,
    private val sender: Member,
    private val channel: TextChannel,
    private val amount: Int,
    override val reason: String,
) : OneshotIncident() {
    override val source: String = sender.id
    override val target: String = channel.id
    override val type = Incident.Type.CLEAR

    override fun execute() {
        val channel = guild.getTextChannelById(target)!!
        val history = MessageHistory(channel)
        val messages = ArrayList<List<Message>>()
        var toRetrieve = amount
        while (toRetrieve >= 100) {
            messages += history.retrievePast(100).complete()
            toRetrieve -= 100
        }
        if (toRetrieve != 0) messages += history.retrievePast(toRetrieve).complete()
        messages.forEach { channel.deleteMessages(it).queue() }
    }

    override fun sendLog(logChannel: TextChannel) = logChannel.sendMessage(
        InfoResponse(
            "Clear - Case #$id",
            """
                **Cleared By:** ${sender.formatted()} ($source)
                **Cleared Channel:** ${channel.name} ($target)
                **Clear Amount:** $amount
                **Reason:**: $reason
            """.trimIndent()
        ).asMessage(sender.user)
    ).queue()

    override fun asDocument() = super.asDocument().also {
        it["amount"] = amount
    }
}