fun main() {

    fun List<ByteArray>.steps(start: List<ByteArray>.() -> Sequence<Pair<Int, Int>>): Int? {
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        val visited = Array(this.size) { BooleanArray(this[it].size) }
        for ((i, j) in this.start()) {
            queue.add(Triple(i, j, 0))
            visited[i][j] = true
            this[i][j] = 'a'.code.toByte()
        }
        val (ei, ej) = this.withIndex()
            .map { (i, row) -> i to row.indexOf('E'.code.toByte()) }
            .find { (_, j) -> j != -1 }
            ?: error("location of E not found")
        this[ei][ej] = 'z'.code.toByte()
        while (!queue.isEmpty()) {
            val (i, j, k) = queue.removeFirst()
            for ((di, dj) in arrayOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)) {
                val ni = i + di
                val nj = j + dj
                if (ni in this.indices && nj in this[ni].indices && !visited[ni][nj] && this[i][j] + 1 >= this[ni][nj]) {
                    if (ni == ei && nj == ej) return k + 1
                    queue.add(Triple(ni, nj, k + 1))
                    visited[ni][nj] = true
                }
            }
        }
        return null
    }

    fun part1(input: List<String>): Int? = input.map { it.toByteArray() }.steps {
        sequence {
            for (i in this@steps.indices) {
                for (j in this@steps.indices) {
                    if (this@steps[i][j] == 'S'.code.toByte()) {
                        yield(i to j)
                    }
                }
            }
        }
    }

    fun part2(input: List<String>): Int? = input.map { it.toByteArray() }.steps {
        sequence {
            for (i in this@steps.indices) {
                for (j in this@steps.indices) {
                    if (this@steps[i][j] == 'S'.code.toByte() || this@steps[i][j] == 'a'.code.toByte()) {
                        yield(i to j)
                    }
                }
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
