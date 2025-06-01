package backwards.extensions

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.chatCommand
import dev.kordex.core.i18n.toKey
import dev.kordex.core.utils.respond
import dev.kordex.core.utils.waitForResponse
import kotlin.random.Random

class ScrambleExtension : Extension() {
	override val name = "scramble"

	private val words = listOf("kotlin", "discord", "bot", "scramble", "extension", "kordex")

	private var currentWord: String? = null
	private var scrambledWord: String? = null
	private var guessingChannelId: ULong? = null

	override suspend fun setup() {
		chatCommand {
			name = "scramble".toKey()
			description = "Starts a scramble guessing game".toKey()

			action {
				if (currentWord != null) {
					message.respond("A game is already running! Try to guess the word.")
					return@action
				}

				currentWord = words.random()
				scrambledWord = scramble(currentWord!!)
				guessingChannelId = message.channel.id.value

				message.respond("Guess the word: **$scrambledWord**\nType your guess in chat!")

				// Wait 30 seconds for guesses
				waitForResponse(30_000L)

				if (currentWord != null) {
					message.respond("Time's up! The word was **$currentWord**")
					currentWord = null
					scrambledWord = null
					guessingChannelId = null
				}
			}
		}

		kord.on<MessageCreateEvent> {
			val guess = message.content.lowercase()
			if (message.channelId.value.toLong().toULong() == guessingChannelId && currentWord != null) {
				if (guess == currentWord) {
					message.respond("Congrats ${message.author?.mention}, you guessed it right! The word was **$currentWord**")
					currentWord = null
					scrambledWord = null
					guessingChannelId = null
				}
			}
		}
	}

	private fun scramble(word: String): String {
		return word.toList().shuffled(Random(System.currentTimeMillis())).joinToString("")
	}
}
