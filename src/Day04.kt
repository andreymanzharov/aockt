fun main() {

    fun part1(input: List<String>): Int =
        input.map { it.split(',', '-').map(String::toInt) }
            .count { (p1, q1, p2, q2) -> p1 <= p2 && q2 <= q1 || p2 <= p1 && q1 <= q2 }

    fun part2(input: List<String>): Int =
        input.map { it.split(',', '-').map(String::toInt) }
            .count { (p1, q1, p2, q2) -> q1 >= p2 && q2 >= p1 }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
