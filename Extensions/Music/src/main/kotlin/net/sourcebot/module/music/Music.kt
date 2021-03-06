package net.sourcebot.module.music

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.entities.Guild
import net.sourcebot.api.module.SourceModule
import net.sourcebot.module.music.audio.AudioSubsystem
import net.sourcebot.module.music.command.*

class Music : SourceModule() {

    override fun onEnable() {
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER)
        registerCommands(
            PauseCommand(),
            ResumeCommand(),
            SkipCommand(),
            StopCommand(),
            PlayCommand(),
            VolumeCommand()
        )
    }

    override fun onDisable() {
        PLAYER_MANAGER.shutdown()
    }

    companion object {
        @JvmStatic
        val PLAYER_MANAGER = DefaultAudioPlayerManager().also {
            it.registerSourceManager(YoutubeAudioSourceManager())
        }
        @JvmStatic
        val SUBSYSTEM_CACHE = HashMap<String, AudioSubsystem>()

        @JvmStatic
        fun getSubsystem(
            guild: Guild
        ): AudioSubsystem = SUBSYSTEM_CACHE.computeIfAbsent(guild.id) {
            AudioSubsystem(PLAYER_MANAGER.createPlayer()).also { it.applyTo(guild) }
        }
    }
}