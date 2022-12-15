import kotlin.math.absoluteValue
import kotlin.math.max

fun main() {

    class Sensor(val x: Int, val y: Int, val bx: Int, val by: Int) {
        val d = (x - bx).absoluteValue + (y - by).absoluteValue
    }

    fun List<String>.sensors() = map { it.split('=', ',', ':') }
        .map { Sensor(it[1].toInt(), it[3].toInt(), it[5].toInt(), it[7].toInt()) }

    fun List<Sensor>.rangesAt(y: Int): List<IntRange> = this.map {
        val x = it.d - (it.y - y).absoluteValue
        it.x - x..it.x + x
    }.filterNot(IntRange::isEmpty).sortedBy { it.first }

    fun part1(input: List<String>, y: Int): Int {
        var count = 0
        var last = Int.MIN_VALUE
        val sensors = input.sensors()
        for (range in sensors.rangesAt(y)) {
            if (last < range.last) {
                count += range.last - max(range.first, last + 1) + 1
            }
            last = max(last, range.last)
        }
        val already = sensors.filter { it.by == y }.map { it.bx }.distinct().count()
        return count - already
    }

    fun part2(input: List<String>, maxY: Int): Long {
        val sensors = input.sensors()
        for (y in 0..maxY) {
            var last = -1
            for (range in sensors.rangesAt(y)) {
                if (last + 1 < range.first) {
                    return (last + 1).toLong() * 4_000_000L + y.toLong()
                }
                last = max(last, range.last)
            }
        }
        error("position not found")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2_000_000))
    println(part2(input, 4_000_000))
}
