fun main() {

	fun parseRange(input: String): IntRange {
		val parts = input.split('-')
			.mapNotNull { it.toIntOrNull() }
		return IntRange(parts[0], parts[1])
	}

	fun parse(input: List<String>): List<Pair<IntRange, IntRange>> {
		return input
			.map { it.split(',') }
			.filter { parts -> parts.size == 2}
			.map { parts -> Pair(parseRange(parts[0]), parseRange(parts[1])) }
	}

	fun part1(input: List<String>): Int {
		return parse(input).count { (left, right) ->
			left.all { right.contains(it) } ||
			right.all { left.contains(it) }
		}
	}
	fun part2(input: List<String>): Int {
		return parse(input).count { (left, right) ->
			left.any { right.contains(it) } ||
			right.any { left.contains(it) }
		}
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day04_test")
	check(part1(testInput) == 2)
	check(part2(testInput) == 4)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}