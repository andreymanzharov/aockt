fun main() {
    val d = arrayOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    fun List<String>.visible(i: Int, j: Int, h: Char) =
        d.any { (di, dj) ->
            generateSequence(i + di to j + dj) { (i, j) -> i + di to j + dj }
                .takeWhile { (i, j) -> i in this.indices && j in this[i].indices }
                .all { (i, j) -> this[i][j] < h }
        }

    fun part1(input: List<String>): Int = input.withIndex()
        .flatMap { (i, row) -> row.withIndex().map { (j, h) -> Triple(i, j, h) } }
        .count { (i, j, h) -> input.visible(i, j, h) }

    fun List<String>.score(i: Int, j: Int, h: Char): Int =
        d.map { (di, dj) ->
            generateSequence(i + di to j + dj) { (i, j) -> if (this[i][j] >= h) null else i + di to j + dj }
                .takeWhile { (i, j) -> i in this.indices && j in this[i].indices }
                .count()
        }.fold(1, Int::times)

    fun part2(input: List<String>): Int = input.withIndex()
        .flatMap { (i, row) -> row.withIndex().map { (j, h) -> Triple(i, j, h) } }
        .maxOf { (i, j, h) -> input.score(i, j, h) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
