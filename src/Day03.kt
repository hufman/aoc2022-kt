import kotlin.streams.toList

fun main() {
	fun score(char: Char): Int {
		return if (('a' .. 'z').contains(char)) {
			char - 'a' + 1
		} else if (('A' .. 'Z').contains(char)) {
			char - 'A' + 27
		} else {
			throw IllegalArgumentException(char.toString())
		}
	}
	fun parse(input: List<String>): List<Pair<String, String>> {
		return input.filter {it.length > 2}.map {
			Pair(it.substring(0 until it.length/2), it.substring(it.length/2 until it.length))
		}
	}
	fun part1(input: List<String>): Int {
		val common = parse(input).map {
			it.first.toCharArray().toSet().intersect(it.second.toCharArray().toSet()).first()
		}
		return common.map {
			score(it)
		}.sum()
	}

	fun part2(input: List<String>): Int {
		val troops = input.chunked(3)
		val badges = troops.map { sacks ->
			sacks.fold(sacks[0].toCharArray().toSet()) { acc, sack ->
				acc.intersect(sack.toCharArray().toSet())
			}.first()
		}
		return badges.map {
			score(it)
		}.sum()
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day03_test")
	check(part1(testInput) == 157)
	check(part2(testInput) == 70)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}