package backwards.extensions

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.defaultingString
import dev.kordex.core.commands.converters.impl.string
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.chatCommand
import dev.kordex.core.i18n.toKey
import dev.kordex.core.utils.respond

class ReverseExtension : Extension() {
	override val name = "reverse"

	override suspend fun setup() {
		chatCommand(::LReverseArgs) {
			name = "lreverse".toKey()
			description = "Reverse letters in a single word".toKey()

			action {
				val input = arguments.word
				val reversed = input.reversed()
				message.respond("Reversed word: $reversed")
			}
		}

		chatCommand(::WReverseArgs) {
			name = "wreverse".toKey()
			description = "Reverse the order of words in a phrase".toKey()

			action {
				val phrase = arguments.phrase
				val reversedWords = phrase.split(" ").reversed().joinToString(" ")
				message.respond("Reversed words order: $reversedWords")
			}
		}

		// The scramble game we'll add later
	}

	inner class LReverseArgs : Arguments() {
		val word by string {
			name = "word".toKey()
			description = "The word to reverse".toKey()
		}
	}

	inner class WReverseArgs : Arguments() {
		val phrase by defaultingString {
			name = "phrase".toKey()
			description = "The phrase with words to reverse order".toKey()
			defaultValue = ""
		}
	}
}
