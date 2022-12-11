fun main() {

    class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val divisor: Long,
        val ifTrue: Int,
        val ifFalse: Int
    ) {
        var inspected: Long = 0
    }

    fun List<String>.parse() = this.chunked(7)
        .map { chunk ->
            val items = chunk[1].substring("  Starting items: ".length).split(", ").map { it.toLong() }.toMutableList()
            val op = chunk[2].substring("  Operation: new = old ".length).split(' ').let { args ->
                when (args[0]) {
                    "+" -> if (args[1] != "old") { arg: Long -> arg + args[1].toLong() } else { arg: Long -> arg + arg }
                    "*" -> if (args[1] != "old") { arg: Long -> arg * args[1].toLong() } else { arg: Long -> arg * arg }
                    else -> error("unsupported operation: ${chunk[2]}")
                }
            }
            val divisor = chunk[3].substring("  Test: divisible by ".length).toLong()
            val ifTrue = chunk[4].substring("    If true: throw to monkey ".length).toInt()
            val ifFalse = chunk[5].substring("    If false: throw to monkey ".length).toInt()
            Monkey(items, op, divisor, ifTrue, ifFalse)
        }
        .toTypedArray()

    fun Array<Monkey>.simulate(times: Int, transform: Monkey.(Long) -> Long): Long {
        repeat(times) {
            for (monkey in this) {
                for (item in monkey.items) {
                    val level = transform(monkey, item)
                    if (level % monkey.divisor == 0L) {
                        this[monkey.ifTrue].items.add(level)
                    } else {
                        this[monkey.ifFalse].items.add(level)
                    }
                    monkey.inspected += 1
                }
                monkey.items.clear()
            }
        }
        this.sortByDescending { it.inspected }
        return this[0].inspected * this[1].inspected
    }

    fun part1(input: List<String>): Long = input.parse().simulate(20) {
        operation(it) / 3
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.parse()
        val mod = monkeys.fold(1L) { acc, monkey -> acc * monkey.divisor }
        return monkeys.simulate(10_000) {
            operation(it) % mod
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
