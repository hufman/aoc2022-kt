
fun main() {

	fun part1(input: List<String>): Int {
		return input[0].asSequence().windowed(4, 1).indexOfFirst {
			it.toCharArray().toSet().size == 4
		} + 4
	}

	fun part2(input: List<String>): Int {
		return input[0].asSequence().windowed(14, 1).indexOfFirst {
			it.toCharArray().toSet().size == 14
		} + 14
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day06_test")
	check(part1(testInput) == 7)
	check(part2(testInput) == 19)

	val input = readInput("Day06")
	println(part1(input))
	println(part2(input))
}