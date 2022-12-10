
sealed class CPUOpcode(val cycles: Int) {
    companion object {
        fun fromLine(line: String): CPUOpcode? {
            val parts = line.split(' ')
            return when (parts[0]) {
                "noop" -> NOOP()
                "addx" -> ADDX(parts[1].toInt())
                else -> null
            }
        }
    }

    class NOOP(): CPUOpcode(1)
    class ADDX(val operand: Int): CPUOpcode(2) {
        override fun run(current: CPUState) = current.copy(X = current.X + operand)
    }

    open fun run(current: CPUState) = current
    override fun toString(): String {
        return when (this) {
            is NOOP -> "NOOP()"
            is ADDX -> "ADDX(${this.operand})"
        }
    }

}

data class CPUState(val X: Int = 1)

fun main() {
    fun parse(input: List<String>): List<CPUOpcode> {
        return input.mapNotNull { CPUOpcode.fromLine(it) }
    }

    fun execute(commands: Iterable<CPUOpcode>) = sequence<CPUState> {
        var state = CPUState()
        commands.forEach { command ->
//            println(command)
            (1 until command.cycles).forEach { _ ->
                yield(state)
            }
            state = command.run(state)
            yield(state)
        }
    }

    fun part1(input: List<String>): Int {
        return execute(parse(input))
//            .filterIndexed { pc, cpuState ->
//                println("${pc+1} -> $cpuState"); true
//            }
            .filterIndexed { pc, cpuState -> (pc + 2 >= 20) && (pc + 2 - 20) % 40 == 0 }
            .mapIndexed { pc_40, cpuState -> (pc_40 * 40 + 20) * cpuState.X }
//            .filter { println(it); true}
            .take(6)
            .sum()
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
}