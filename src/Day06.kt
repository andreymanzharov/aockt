fun main() {

    fun String.marker(n: Int) = (n..length).firstOrNull { subSequence(it - n, it).toSet().size == n }

    fun part1(input: String): Int? = input.marker(4)

    fun part2(input: String): Int? = input.marker(14)

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
