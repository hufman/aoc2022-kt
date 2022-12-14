import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
	fun parse(input: List<String>): Array<BooleanArray> {
		val grid = Array(300) {
			BooleanArray(800) {false}
		}
		input.forEach { line ->
			val endPoints = line.split(" -> ").map { segment ->
				val pieces = segment.split(',').map {
					it.toInt()
				}
				Pair(pieces[0], pieces[1])
			}
//			println("$line -> $endPoints")
			endPoints.windowed(2, 1).forEach { lineEnds ->
//				println(lineEnds)
				val startX = min(lineEnds[0].first, lineEnds[1].first)
				val endX = max(lineEnds[0].first, lineEnds[1].first)
				val startY = min(lineEnds[0].second, lineEnds[1].second)
				val endY = max(lineEnds[0].second, lineEnds[1].second)
				if (startY == endY) {
					(startX..endX).forEach { x ->
						grid[startY][x] = true
					}
				}
				if (startX == endX) {
					(startY..endY).forEach { y ->
						grid[y][startX] = true
					}
				}
			}
		}
		return grid
	}

	fun printGrid(grid: Array<BooleanArray>) {
		val endY = grid.indexOfLast { row -> row.any{it} } + 1
		val startX = grid.filter { row -> row.any{it}}.minOf { row -> row.indexOfFirst { it } } - 1
		val endX = grid.filter { row -> row.any{it}}.maxOf { row -> row.indexOfLast { it } } + 1
		grid.take(endY).forEach { row ->
			println(
				(startX .. endX).map { x ->
					if (row[x]) '#' else '.'
				}.joinToString("")
			)
		}
	}

	fun sprinkle(grid: Array<BooleanArray>, startX: Int = 500, startY: Int = 0, lastY: Int? = null): Pair<Int, Int>? {
		var x = startX
		var y = startY
		while (true) {
			if (y+1 == grid.size) {
				return null
			}
			if (y == lastY) {
				return Pair(x, y)
			}
//			println("Checking ${x}x$y: ${grid[y][x]}")
			if (!grid[y+1][x]) {
				y += 1
			} else if (!grid[y+1][x-1]) {
				y += 1
				x -= 1
			} else if (!grid[y+1][x+1]) {
				y += 1
				x += 1
			} else {
				return Pair(x, y)
			}
		}
	}

	fun part1(input: List<String>): Int {
		val grid = parse(input)
		var sand = 0
		printGrid(grid)
		while (true) {
			val sandLocation = sprinkle(grid)
			if (sandLocation != null) {
//				println("Found new sand particle at ${sandLocation.first}x${sandLocation.second}")
				grid[sandLocation.second][sandLocation.first] = true
				sand += 1
			} else {
//				println("Done adding $sand grains")
//				printGrid(grid)
				return sand
			}
		}
	}

	fun part2(input: List<String>): Int {
		val grid = parse(input)
		var sand = 0
		val maxY = grid.indexOfLast { row -> row.any{it} } + 2
		val startX = grid.filter { row -> row.any{it}}.minOf { row -> row.indexOfFirst { it } } - 0
		val endX = grid.filter { row -> row.any{it}}.maxOf { row -> row.indexOfLast { it } } + 0
		(startX .. endX).forEach { x ->
			grid[maxY][x] = true
		}
		printGrid(grid)
		while (true) {
			val sandLocation = sprinkle(grid, lastY=maxY-1)
			if (sandLocation == null) {
				printGrid(grid)
				throw AssertionError("Unexpected pouring")
			}
			grid[sandLocation.second][sandLocation.first] = true
			sand += 1
			if (sandLocation.first == 500 && sandLocation.second == 0) {
				println("Done adding $sand grains")
				printGrid(grid)
				return sand
			}
		}
	}
	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day14_test")
	check(part1(testInput) == 24)
	check(part2(testInput) == 93)

	val input = readInput("Day14")
	println(part1(input))
	println(part2(input))
}