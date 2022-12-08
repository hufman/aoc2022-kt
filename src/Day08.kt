import kotlin.streams.asSequence

open class ColumnView<E>(open val rows: List<List<E>>, val columnIndex: Int): AbstractMutableList<E>() {
	override fun add(index: Int, element: E) { TODO("Not yet implemented") }

	override fun removeAt(index: Int): E { TODO("Not yet implemented") }

	override fun set(index: Int, element: E): E { TODO("Not yet implemented") }

	override val size: Int
		get() = rows.size

	override fun get(index: Int): E {
		return rows[index][columnIndex]
	}
}
class MutableColumnView<E>(override val rows: List<MutableList<E>>, columnIndex: Int): ColumnView<E>(rows, columnIndex) {
	override fun set(index: Int, element: E): E {
		rows[index][columnIndex] = element
		return element
	}
}
fun <E> List<E>.takeUntilFound(filter: (element: E) -> Boolean): List<E> {
	val output = ArrayList<E>()
	this.forEach {
		output.add(it)
		val found = filter(it)
		if (found) {
			return output
		}
	}
	return output
}

fun main() {
	fun parse(input: List<String>): List<List<Short>> {
		return input.map {line ->
			line.chars().asSequence().map {
				it.toShort()
			}.toList()
		}
	}

	fun checkVisibility(treeRow: Iterable<Short>, visibility: MutableList<Short>) {
		var maxHeight: Short = 0
		treeRow.forEachIndexed { i, height ->
			if (height > maxHeight) {
				visibility[i] = 1
				maxHeight = height
			}
			if (maxHeight == 9.toShort()) {
				return
			}
		}
	}

	fun part1(input: List<String>): Int {
		val trees = parse(input)
		val visibility = Array(trees.size) { row ->
			ShortArray(trees[row].size).toMutableList()
		}.toMutableList()

		trees.zip(visibility).forEach { (row, visibles) ->
			checkVisibility(row, visibles)
			checkVisibility(row.asReversed(), visibles.asReversed())
		}
		(0 until trees[0].size).forEach { columnIndex ->
			val treeColumn = ColumnView(trees, columnIndex)
			val visibles = MutableColumnView(visibility, columnIndex)
			checkVisibility(treeColumn, visibles)
			checkVisibility(treeColumn.asReversed(), visibles.asReversed())
		}
		return visibility.sumOf {
			it.sum()
		}
	}

	fun scenicScore(trees: List<List<Short>>, rowIndex: Int, colIndex: Int): Int {
		val maxHeight = trees[rowIndex][colIndex]
		val east = trees[rowIndex].subList(colIndex + 1, trees[rowIndex].size).takeUntilFound { it >= maxHeight }.count()
		val west = trees[rowIndex].subList(0, colIndex).asReversed().takeUntilFound { it >= maxHeight }.count()
		val column = ColumnView(trees, colIndex)
		val north = column.subList(0, rowIndex).asReversed().takeUntilFound { it >= maxHeight }.count()
		val south = column.subList(rowIndex + 1, trees.size).takeUntilFound { it >= maxHeight }.count()
//		println("${colIndex}x${rowIndex} (${trees[rowIndex][colIndex]}) -> $north * $west * $south * $east")
		return east * west * north * south
	}

	fun part2(input: List<String>): Int {
		val trees = parse(input)
		return trees.subList(1, trees.size - 1).mapIndexed { rowIndex, row ->
			row.subList(1, row.size - 1).mapIndexed { colIndex, _ ->
				scenicScore(trees, rowIndex + 1, colIndex + 1)
			}.max()
		}.max()
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day08_test")
	check(part1(testInput) == 21)
	check(part2(testInput) == 8)

	val input = readInput("Day08")
	println(part1(input))
	println(part2(input))
}