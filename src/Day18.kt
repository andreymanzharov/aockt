fun main() {

    fun List<String>.parse() = this
        .map { it.split(',').map { it.toInt() } }
        .map { (x, y, z) -> Triple(x, y, z) }

    fun part1(cubes: List<Triple<Int, Int, Int>>): Int {
        val visited = mutableSetOf<Triple<Int, Int, Int>>()

        fun scan(cube: Triple<Int, Int, Int>): Int {
            visited += cube

            fun go(cube: Triple<Int, Int, Int>): Int = when (cube) {
                in visited -> 0
                in cubes -> scan(cube)
                else -> 1
            }

            val (x, y, z) = cube
            return 0 +
                    go(Triple(x + 1, y, z)) +
                    go(Triple(x - 1, y, z)) +
                    go(Triple(x, y + 1, z)) +
                    go(Triple(x, y - 1, z)) +
                    go(Triple(x, y, z + 1)) +
                    go(Triple(x, y, z - 1))
        }

        return cubes
            .asSequence()
            .filter { it !in visited }
            .sumOf { scan(it) }
    }

    fun part2(cubes: List<Triple<Int, Int, Int>>): Int {
        var area = part1(cubes)

        val minX = cubes.minOf { it.first }
        val maxX = cubes.maxOf { it.first }
        val minY = cubes.minOf { it.second }
        val maxY = cubes.maxOf { it.second }
        val minZ = cubes.minOf { it.third }
        val maxZ = cubes.maxOf { it.third }

        val visited = mutableSetOf<Triple<Int, Int, Int>>()
        val outer = mutableSetOf<Triple<Int, Int, Int>>()

        fun scanInner(cube: Triple<Int, Int, Int>): Pair<Boolean, Int> {
            val (x, y, z) = cube
            if (x !in minX..maxX || y !in minY..maxY || z !in minZ..maxZ) return false to 0
            visited += cube

            fun go(cube: Triple<Int, Int, Int>): Pair<Boolean, Int> = when (cube) {
                in cubes -> true to 1
                in outer -> false to 0
                in visited -> true to 0
                else -> scanInner(cube)
            }

            var result = 0
            for ((dx, dy, dz) in arrayOf(
                Triple(-1, 0, 0),
                Triple(1, 0, 0),
                Triple(0, -1, 0),
                Triple(0, 1, 0),
                Triple(0, 0, -1),
                Triple(0, 0, 1)
            )) {
                val (inner, innerArea) = go(Triple(x + dx, y + dy, z + dz))
                if (inner) result += innerArea else return false to 0
            }
            return true to result
        }
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val cube = Triple(x, y, z)
                    if (cube !in cubes && cube !in outer && cube !in visited) {
                        val (inner, innerArea) = scanInner(cube)
                        if (inner)
                            area -= innerArea
                        else {
                            outer += visited
                            visited.clear()
                        }
                    }
                }
            }
        }

        return area
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test").parse()
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18").parse()
    println(part1(input))
    println(part2(input))
}
