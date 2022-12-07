data class Dir(val subdirs: MutableMap<String, Dir>, val filesizes: MutableMap<String, Long>, var size: Long = 0) {
	companion object {
		fun empty(): Dir = Dir(mutableMapOf(), mutableMapOf()
		)
	}

	fun updateSize() {
		size = subdirs.values.fold(0L) {total, dir ->
			dir.updateSize()
			total + dir.size
		} + filesizes.values.sum()
	}
}

fun main() {
	fun parse(input: List<String>): Dir {
		val path: MutableList<Dir> = listOf(Dir.empty()).toMutableList()
		input.forEach { line ->
			val parts = line.split(' ')
			if (line.startsWith("$")) {
				if (parts[1] == "cd" && parts[2] == "/")  {
					while (path.size > 1) {
						path.removeAt(1)
					}
				} else if (parts[1] == "cd" && parts[2] == "..") {
					path.removeAt(path.size-1)
				} else if (parts[1] == "cd") {
					path.add(path.last().subdirs.getOrPut(parts[2]){Dir.empty()})
				} else if (parts[1] == "dir") { }
			} else {
				if (parts[0] == "dir") {
					path.last().subdirs.getOrPut(parts[1]){Dir.empty()}
				} else {
					path.last().filesizes[parts[1]] = parts[0].toLong()
				}
			}
		}
		path[0].updateSize()
		return path[0]
	}

	fun walkSizes(root: Dir): Sequence<Dir> = sequence {
		yield(root)
		root.subdirs.values.forEach {
			yieldAll(walkSizes(it))
		}
	}
	fun part1(input: List<String>): Long {
		val dir = parse(input)
		return walkSizes(dir).filter {it.size <= 100000}.sumOf { it.size }
	}
	fun part2(input: List<String>): Long {
		val dir = parse(input)
		val max = 70000000
		val needed = 30000000
		val free = max - dir.size
		val toDelete = needed - free
		return walkSizes(dir).sortedBy { it.size }.first {it.size > toDelete}.size
	}


	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day07_test")
	check(part1(testInput) == 95437L)
	check(part2(testInput) == 24933642L)

	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}
