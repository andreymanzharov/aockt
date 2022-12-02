fun main() {

    fun part1(input: List<String>): Int = input.sumOf { (it[2] - it[0] - ('X' - 'A') + 4) % 3 * 3 + (it[2] - 'X' + 1) }

    fun part2(input: List<String>): Int = input.sumOf {
        (it[2] - 'X') * 3 + (it[0] - 'A' + (it[2] - 'X' - 1) + 3) % 3 + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
