
data class Instruction(val count: Int, val src: Int, val dest: Int) {
	companion object {
		val matcher = Regex("""move (\d+) from (\d+) to (\d+)""")
		fun fromString(input: String): Instruction {
			val result = matcher.matchEntire(input)!!
			return Instruction(result.groupValues[1].toInt(), result.groupValues[2].toInt(), result.groupValues[3].toInt())
		}
	}
}
data class Puzzle(val stacks: List<MutableList<Char>>, val instructions: List<Instruction>)

fun main() {

	fun parse(rows: List<String>): Puzzle {
		val columns = ArrayList<MutableList<Char>>().apply {
			(0..9).forEach { _ -> add(ArrayList()) }
		}
		val instructions = ArrayList<Instruction>()
		rows.forEach { row ->
			if (row.contains('[')) {
				for (i in 1 until row.length step 4) {
					val columnNumber = i / 4 + 1
					if (row[i].isLetter()) {
						columns[columnNumber].add(0, row[i])
					}
				}
			} else if (row.contains("move")) {
				instructions.add(Instruction.fromString(row))
			}
		}
		return Puzzle(columns, instructions)
	}

	fun part1(input: List<String>): String {
		val puzzle = parse(input)
		puzzle.instructions.forEach {
			(0 until it.count).forEach { _ ->
				puzzle.stacks[it.dest].add(puzzle.stacks[it.src].removeLast())
			}
		}
		return puzzle.stacks.mapNotNull { it.lastOrNull() }.joinToString("")
	}

	fun part2(input: List<String>): String {
		val puzzle = parse(input)
		puzzle.instructions.forEach {
			val cut = puzzle.stacks[it.src].size - it.count
			(0 until it.count).forEach { _ ->
				puzzle.stacks[it.dest].add(puzzle.stacks[it.src].removeAt(cut))
			}
		}
		return puzzle.stacks.mapNotNull { it.lastOrNull() }.joinToString("")
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day05_test")
	check(part1(testInput) == "CMZ")
	check(part2(testInput) == "MCD")

	val input = readInput("Day05")
	println(part1(input))
	println(part2(input))
}