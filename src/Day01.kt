fun main() {
    fun parse(input: List<String>): List<List<Int>> {
        var elf = ArrayList<Int>()
        val elves = ArrayList<List<Int>>().apply { add(elf) }
        input.map { it.toIntOrNull() }.forEach {
            if (it != null) {
                elf.add(it)
            } else {
                elf = ArrayList()
                elves.add(elf)
            }
        }
        return elves
    }
    fun part1(input: List<String>): Int {
        return parse(input).map { it.sum() }.max()
    }

    fun part2(input: List<String>): Int {
        return parse(input).map { it.sum() }.sortedByDescending { it }.take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
