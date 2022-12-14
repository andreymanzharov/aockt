import kotlin.math.max
import kotlin.math.min

fun main() {

    class Rocks(input: List<String>) {
        private val horizontal: Map<Int, List<IntRange>>
        private val vertical: Map<Int, List<IntRange>>
        val maxY: Int

        init {
            val h = mutableMapOf<Int, MutableList<IntRange>>()
            val v = mutableMapOf<Int, MutableList<IntRange>>()

            var maxY = 0
            for (line in input) {
                for ((p1, p2) in line.split(",", " -> ").map { it.toInt() }.chunked(2).windowed(2)) {
                    val (x1, y1) = p1
                    val (x2, y2) = p2
                    if (y1 == y2) {
                        h.computeIfAbsent(y1) { mutableListOf() } += min(x1, x2)..max(x1, x2)
                    } else if (x1 == x2) {
                        v.computeIfAbsent(x1) { mutableListOf() } += min(y1, y2)..max(y1, y2)
                    }
                    maxY = max(maxY, y1)
                    maxY = max(maxY, y2)
                }
            }
            h.put(maxY + 2, mutableListOf(Int.MIN_VALUE..Int.MAX_VALUE))
            this.horizontal = h
            this.vertical = v
            this.maxY = maxY
        }

        private val sands = mutableSetOf<Pair<Int, Int>>()

        fun isEmpty(x: Int, y: Int): Boolean = x to y !in sands
                && horizontal.getOrDefault(y, emptyList()).none { x in it }
                && vertical.getOrDefault(x, emptyList()).none { y in it }

        private fun addSand(x: Int, y: Int) {
            sands += x to y
        }

        fun simulate(condition: Rocks.(Int, Int) -> Boolean): Int {
            var count = 0
            while (true) {
                var (x, y) = 500 to 0
                while (condition(x, y)) {
                    when {
                        isEmpty(x, y + 1) -> y += 1
                        isEmpty(x - 1, y + 1) -> {
                            x -= 1; y += 1
                        }

                        isEmpty(x + 1, y + 1) -> {
                            x += 1; y += 1
                        }

                        else -> {
                            addSand(x, y)
                            break
                        }
                    }
                }
                if (!condition(x, y)) return count
                count += 1
            }
        }
    }

    fun part1(input: List<String>): Int = Rocks(input).simulate { _, y -> y < maxY }

    fun part2(input: List<String>): Int = Rocks(input).simulate { _, _ -> isEmpty(500, 0) } + 1

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
