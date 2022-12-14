import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

fun main() {
	fun parse(input: List<String>): List<Pair<JsonArray, JsonArray>> {
		return input
			.filter { it.isNotEmpty() }
			.map { Json.decodeFromString<JsonArray>(it) }
			.chunked(2) {
				Pair(it[0], it[1])
			}
	}

	fun compare(left: JsonArray, right: JsonArray): Int {
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
					compare(l, r)
				} else if (l is JsonPrimitive && r is JsonArray) {
					compare(JsonArray(listOf(l)), r)
				} else if (l is JsonArray && r is JsonPrimitive) {
					compare(l, JsonArray(listOf(r)))
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
		val pairs = parse(input)
		return pairs
			.foldIndexed(0) { index, acc, pair: Pair<JsonArray, JsonArray> ->
				val correct = compare(pair.first, pair.second)
//				println("$correct ${pair.first} ${pair.second}")
				if (correct == -1) {
					acc + index + 1
				} else {
					acc
				}
			}
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day13_test")
	check(part1(testInput) == 13)

	val input = readInput("Day13")
	println(part1(input))
}
