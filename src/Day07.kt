fun main() {

    fun List<String>.parse(): Map<String, Int> {
        val cwd = mutableListOf<String>()
        val result = hashMapOf<String, Int>()
        for (line in this) {
            val args = line.split(' ')
            when (args[0]) {
                "$" -> when (args[1]) {
                    "cd" -> when (args[2]) {
                        "/" -> cwd.clear()
                        ".." -> cwd.removeLast()
                        else -> cwd.add(args[2])
                    }
                    "ls" -> {}
                }
                "dir" -> {}
                else -> {
                    result.compute("/") { _, size -> (size ?: 0) + args[0].toInt() }
                    var path = ""
                    for (dir in cwd) {
                        path += "/$dir"
                        result.compute(path) { _, size -> (size ?: 0) + args[0].toInt() }
                    }
                }
            }
        }
        return result
    }

    fun part1(input: Map<String, Int>): Int = input.values.filter { it <= 100000 }.sum()

    fun part2(input: Map<String, Int>): Int {
        val unused = 70000000 - input["/"]!!
        return input.values.filter { it + unused >= 30000000 }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test").parse()
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07").parse()
    println(part1(input))
    println(part2(input))
}
