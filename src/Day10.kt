fun main() {

    fun part1(input: List<String>): Int {
        var sum = 0
        var x = 1
        var c = 20
        for (line in input) {
            val cmd = line.split(' ')
            val (dx, dc) = when (cmd[0]) {
                "noop" -> 0 to 1
                "addx" -> cmd[1].toInt() to 2
                else -> error("unknown command: $line")
            }
            if (c / 40 != (c + dc) / 40) {
                val k = (c + dc) / 40 * 40 - 20
                sum += x * k
            }
            x += dx
            c += dc
        }
        return sum
    }

    fun part2(input: List<String>): List<String> {
        val d = Array(6) { CharArray(40) { '.' } }
        var x = 1
        var c = 0
        for (line in input) {
            val cmd = line.split(' ')
            val (dx, dc) = when (cmd[0]) {
                "noop" -> 0 to 1
                "addx" -> cmd[1].toInt() to 2
                else -> error("unknown command: $line")
            }
            repeat(dc) {
                val cycle = c + it
                if (cycle % 40 in x - 1..x + 1) {
                    d[cycle / 40][cycle % 40] = '#'
                }
            }
            x += dx
            c += dc
        }
        return d.map { String(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == listOf(
            "##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######....."
        )
    )

    val input = readInput("Day10")
    println(part1(input))
    part2(input).forEach { println(it) }
}
