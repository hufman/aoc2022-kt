class MonkeyBrains(val operation: (Long) -> Long,
                   val modulus: Int,
                   val trueDest: Int,
                   val falseDest: Int,
                   var state: State
) {
    class Builder() {
        var items: MutableList<Long> = ArrayList()
        var operation: ((Long) -> Long)? = null
        var modulus = 0
        var trueDest = 0
        var falseDest = 0

        fun build(): MonkeyBrains {
            return MonkeyBrains(operation!!, modulus, trueDest, falseDest, State(0, items))
        }
    }
    data class State(val inspections: Int, val items: MutableList<Long>)
}

fun main() {
    fun parse(input: List<String>): List<MonkeyBrains> {
        val monkeys = ArrayList<MonkeyBrains>()

        val listMatcher = Regex("""Starting items: ((?:\d+(?:, )?)+)""")
        val operation = Regex("""Operation: new = (\w+) (.) (\w+)""")
        val testMatcher = Regex("""Test: divisible by (\d+)""")
        val trueDest = Regex("""If true: throw to monkey (\d+)""")
        val falseDest = Regex("""If false: throw to monkey (\d+)""")

        var monkey = MonkeyBrains.Builder()
        input.forEach { line ->
            listMatcher.find(line)?.also {
                monkey.items = it.groupValues[1].split(", ").map { it.toLong() }.toMutableList()
                println("Starting items ${monkey.items}")
            }
            operation.find(line)?.also {
                val (_, op, right) = it.destructured
                monkey.operation = { old ->
                    val factor = if (right == "old") old else right.toLong()
                    if (op == "+") old + factor else old * factor
                }
            }
            testMatcher.find(line)?.also {
                monkey.modulus = it.groupValues[1].toInt()
            }
            trueDest.find(line)?.also {
                monkey.trueDest = it.groupValues[1].toInt()
            }
            falseDest.find(line)?.also {
                monkey.falseDest = it.groupValues[1].toInt()
            }
            if (line == "") {
                monkeys.add(monkey.build())
                monkey = MonkeyBrains.Builder()
            }
        }
        monkeys.add(monkey.build())
        return monkeys
    }

    fun runBusiness(monkeys: List<MonkeyBrains>): Sequence<List<MonkeyBrains.State>> = sequence {
        while (true) {
            monkeys.forEach { monkey ->
                monkey.state.items.forEach { current ->
                    val next = monkey.operation(current) / 3
//                    println("Converted $current to $next")
                    if (next % monkey.modulus == 0L) {
                        monkeys[monkey.trueDest].state.items.add(next)
                    } else {
                        monkeys[monkey.falseDest].state.items.add(next)
                    }
                }
                monkey.state = MonkeyBrains.State(monkey.state.inspections + monkey.state.items.size, ArrayList())
            }
            yield(monkeys.mapIndexed { i, monkey ->
                println("$i: ${monkey.state}")
                monkey.state
            })
        }
    }

    fun part1(input: List<String>): Int {
        return runBusiness(parse(input))
            .take(20).last()    // 20 rounds
            .sortedByDescending { monkey -> monkey.inspections }.take(2)    // top monkeys
            .fold(1) {acc, next -> acc * next.inspections}  // multiply together
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    check(part1(testInput) == 10605)

    val input = readInput("Day11")
    println(part1(input))
}