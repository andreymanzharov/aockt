import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

fun main() {

    fun follow(t: Pair<Int, Int>, h: Pair<Int, Int>) =
        t.first + (h.first - t.first).sign to t.second + (h.second - t.second).sign

    fun List<String>.simulate(n: Int): Int {
        val rope = Array(n) { 0 to 0 }
        val visited = mutableSetOf(0 to 0)
        for (move in this) {
            val args = move.split(' ')
            val (dx, dy) = when (args[0]) {
                "U" -> 0 to 1
                "D" -> 0 to -1
                "L" -> -1 to 0
                "R" -> 1 to 0
                else -> error("unknown direction ${args[0]}")
            }
            repeat(args[1].toInt()) {
                rope[0] = rope[0].first + dx to rope[0].second + dy
                for (i in 1 until n) {
                    if (max(abs(rope[i - 1].first - rope[i].first), abs(rope[i - 1].second - rope[i].second)) > 1) {
                        rope[i] = follow(rope[i], rope[i - 1])
                    }
                }
                visited.add(rope.last())
            }
        }
        return visited.size
    }

    fun part1(input: List<String>): Int = input.simulate(2)

    fun part2(input: List<String>): Int = input.simulate(10)

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day09_test1")
    check(part1(testInput1) == 13)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
