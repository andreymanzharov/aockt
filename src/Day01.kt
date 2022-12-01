fun main() {
    fun calories(input: List<String>): Sequence<Int> {
        val it = input.iterator()
        return generateSequence {
            var sum: Int? = null
            while (it.hasNext()) {
                val next = it.next().takeUnless(String::isEmpty)?.toInt() ?: break
                sum = (sum ?: 0) + next
            }
            sum
        }
    }

    fun part1(input: List<String>): Int = calories(input).max()

    fun part2(input: List<String>): Int = calories(input).toList().sorted().takeLast(3).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
