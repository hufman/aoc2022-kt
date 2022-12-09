import kotlin.math.absoluteValue
import kotlin.math.sign

enum class SnakeInstructionDirection {
	Up,
	Down,
	Left,
	Right
}

class SnakePosition(var x: Int, var y: Int) {
	fun moveDirection(direction: SnakeInstructionDirection) {
		when (direction) {
			SnakeInstructionDirection.Up -> y--
			SnakeInstructionDirection.Down -> y++
			SnakeInstructionDirection.Left -> x--
			SnakeInstructionDirection.Right -> x++
		}
	}
	fun moveToward(other: SnakePosition) {
		val startX = x
		val startY = y
		val diffX = other.x - x
		if (diffX.absoluteValue > 1) {
			x += diffX.sign
			val diffY = other.y - y
			if (diffY.absoluteValue > 0) {
				y += diffY.sign
			}
		}
		val diffY = other.y - y
		if (diffY.absoluteValue > 1) {
			y += diffY.sign
			if (diffX.absoluteValue > 0) {
				x += diffX.sign
			}
		}
		if ((other.x - x).absoluteValue > 1 ||
			(other.y - y).absoluteValue > 1) {
			println("Failed to inch closer from ${Pair(startX, startY)} to ${other.asPair()}, only got to ${this.asPair()}")
			throw AssertionError()
		}
	}
	fun asPair(): Pair<Int, Int> = Pair(x, y)
}

fun main() {
	fun parse(input: List<String>): List<Pair<SnakeInstructionDirection, Short>> {
		return input.map { it.split(' ') }
			.filter { it.size == 2 }
			.mapNotNull {
				val direction = when (it[0]) {
					"U" -> SnakeInstructionDirection.Up
					"D" -> SnakeInstructionDirection.Down
					"L" -> SnakeInstructionDirection.Left
					"R" -> SnakeInstructionDirection.Right
					else -> null
				}
				val count = it[1].toShortOrNull()
				if (direction != null && count != null) {
					Pair(direction, count)
				} else {
					null
				}
			}
	}
	fun part1(input: List<String>): Int {
		val tailVisited = HashSet<Pair<Int, Int>>()
		val head = SnakePosition(0, 0)
		val tail = SnakePosition(0, 0)
		parse(input).forEach { (direction, count) ->
			(0 until count).forEach { _ ->
				head.moveDirection(direction)
				tail.moveToward(head)
//				println("Moving head $direction, is now ${head.asPair()} lagging ${tail.asPair()}")
				tailVisited.add(tail.asPair())
			}
		}
		return tailVisited.size
	}
	fun part2(input: List<String>): Int {
		val tailVisited = HashSet<Pair<Int, Int>>()
		val pieces = Array(10) {
			SnakePosition(0, 0)
		}
		parse(input).forEach { (direction, count) ->
//			println("$direction $count")
			(0 until count).forEach { _ ->
				pieces[0].moveDirection(direction)
				(1 .. 9).forEach {
					pieces[it].moveToward(pieces[it-1])
				}
//				pieces.forEach {
//					print(it.asPair())
//				}
//				println()
				tailVisited.add(pieces[9].asPair())
			}
		}
		return tailVisited.size
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day09_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 1)
	val testInput2 = readInput("Day09_test2")
	check(part2(testInput2) == 36)

	val input = readInput("Day09")
	println(part1(input))
	println(part2(input))
}