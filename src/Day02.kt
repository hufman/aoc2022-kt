
enum class Move(val points: Int) {
	ROCK(1),
	PAPER(2),
	SCISSORS(3);

	companion object {
		fun fromStr(input: String): Move {
			return when(input) {
				"A" -> ROCK
				"B" -> PAPER
				"C" -> SCISSORS
				"X" -> ROCK
				"Y" -> PAPER
				"Z" -> SCISSORS
				else -> throw IllegalArgumentException(input)
			}
		}
	}

	fun contest(other: Move): ContestResult {
		if (this == other) return ContestResult.DRAW
		if ((this == ROCK && other == PAPER) ||
			(this == PAPER && other == SCISSORS) ||
			(this == SCISSORS && other == ROCK)) return ContestResult.WIN
		return ContestResult.LOSS
	}
}

enum class ContestResult(val points: Int) {
	WIN(6),
	DRAW(3),
	LOSS(0);

	companion object {
		fun fromStr(input: String): ContestResult {
			return when(input) {
				"X" -> LOSS
				"Y" -> DRAW
				"Z" -> WIN
				else -> throw IllegalArgumentException(input)
			}
		}
	}
}

fun main() {
	fun parse_part1(input: List<String>): List<Pair<Move, Move>> {
		return input.map {
			it.split(" ", limit=2)
		}.filter { it.size == 2
		}.map {
			Pair(Move.fromStr(it[0]), Move.fromStr(it[1]))
		}
	}
	fun score_move(left: Move, right: Move): Int {
		return right.points + left.contest(right).points
	}

	fun part1(input: List<String>): Int {
		return parse_part1(input)
			.map { score_move(it.first, it.second) }
			.sum()
	}

	fun plan_move(left: Move, result: ContestResult): Move {
		if (result == ContestResult.DRAW) return left
		return if (result == ContestResult.WIN) {
			when (left) {
				Move.ROCK -> Move.PAPER
				Move.PAPER -> Move.SCISSORS
				Move.SCISSORS -> Move.ROCK
			}
		} else {        // LOSS
			when (left) {
				Move.ROCK -> Move.SCISSORS
				Move.PAPER -> Move.ROCK
				Move.SCISSORS -> Move.PAPER
			}
		}
	}

	fun parse_part2(input: List<String>): List<Pair<Move, ContestResult>> {
		return input.map {
			it.split(" ", limit=2)
		}.filter { it.size == 2
		}.map {
			Pair(Move.fromStr(it[0]), ContestResult.fromStr(it[1]))
		}
	}
	fun part2(input: List<String>): Int {
		return parse_part2(input)
			.map { score_move(it.first, plan_move(it.first, it.second)) }
			.sum()
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day02_test")
	check(part1(testInput) == 15)
	check(part2(testInput) == 12)

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}