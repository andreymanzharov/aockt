fun main() {

    fun priority(type: Char): Int = when (type) {
        in 'a'..'z' -> type - 'a' + 1
        in 'A'..'Z' -> type - 'A' + 27
        else -> error("unknown type '$type'")
    }

    fun part1(input: List<String>): Int = input.sumOf {
        val first = it.substring(0, it.length / 2).toSet()
        val second = it.substring(it.length / 2, it.length).toSet()
        val type = (first intersect second).first()
        priority(type)
    }

    fun part2(input: List<String>): Int = input.chunked(3).sumOf {
        val type = it.map(String::toSet).reduce(Set<Char>::intersect).first()
        priority(type)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
