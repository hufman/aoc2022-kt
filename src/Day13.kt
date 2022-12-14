import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

fun main() {
	fun parse(input: List<String>): List<JsonArray> {
		return input
			.filter { it.isNotEmpty() }
			.map { Json.decodeFromString(it) }

	}

	fun comparePacket(left: JsonArray, right: JsonArray): Int {
//		println("  - Comparing $left vs $right")
		left.forEachIndexed { index, l ->
			if (index >= right.size) {
//				println("  - Left is too long, failing")
				return 1
			}
			val r = right[index]

			val result = if (l is JsonPrimitive && r is JsonPrimitive) {
				l.int.compareTo(r.int)
			} else {
				if (l is JsonArray && r is JsonArray) {
					comparePacket(l, r)
				} else if (l is JsonPrimitive && r is JsonArray) {
					comparePacket(JsonArray(listOf(l)), r)
				} else if (l is JsonArray && r is JsonPrimitive) {
					comparePacket(l, JsonArray(listOf(r)))
				} else {
					0
				}
			}
//			println("  - $result $l vs $r")
			if (result != 0) return result
		}
		return -1
	}

	fun part1(input: List<String>): Int {
		val lines = parse(input)
		return lines
			.chunked(2) {
				Pair(it[0], it[1])
			}
			.foldIndexed(0) { index, acc, pair: Pair<JsonArray, JsonArray> ->
				val correct = comparePacket(pair.first, pair.second)
//				println("$correct ${pair.first} ${pair.second}")
				if (correct == -1) {
					acc + index + 1
				} else {
					acc
				}
			}
	}
	
	fun part2(input: List<String>): Int {
		val first = JsonArray(listOf(JsonArray(listOf(JsonPrimitive(2)))))
		val second = JsonArray(listOf(JsonArray(listOf(JsonPrimitive(6)))))
		val lines = parse(input) +
				listOf(first, second)
		val sorted = lines.sortedWith(object: Comparator<JsonArray> {
			override fun compare(p0: JsonArray?, p1: JsonArray?): Int {
				p0 ?: return 0
				p1 ?: return 0
				return comparePacket(p0, p1)
			}
		})
		val firstIndex = sorted.indexOf(first)
		val secondIndex = sorted.indexOf(second)
		return (firstIndex + 1) * (secondIndex + 1)
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day13_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 140)

	val input = readInput("Day13")
	println(part1(input))
	println(part2(input))
}
