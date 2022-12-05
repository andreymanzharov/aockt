typealias Stacks = Array<MutableList<Char>>

data class Move(val count: Int, val from: Int, val to: Int)
typealias Moves = List<Move>

data class Input(val stacks: Stacks, val moves: Moves) {
    companion object {
        fun parse(input: List<String>): Input {
            val at = input.indexOf("")
            val count = input[at - 1].split(Regex("\\s+")).count() - 1
            val stacks = Array(count) { mutableListOf<Char>() }
            for (line in input.subList(0, at - 1).asReversed()) {
                for ((stack, i) in stacks.zip(1 until line.length step 4)) {
                    if (line[i].isLetter()) {
                        stack.add(line[i])
                    }
                }
            }
            val moves = input.subList(at + 1, input.size)
                .map { it.split(' ') }
                .map { Move(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1) }
            return Input(stacks, moves)
        }
    }
}

fun main() {

    fun part1(input: List<String>): String {
        val (stacks, moves) = Input.parse(input)
        for ((count, fromIndex, toIndex) in moves) {
            val from = stacks[fromIndex]
            val to = stacks[toIndex]
            repeat(count) {
                to.add(from.removeLast())
            }
        }
        return buildString {
            stacks.forEach { append(it.last()) }
        }
    }

    fun part2(input: List<String>): String {
        val (stacks, moves) = Input.parse(input)
        for ((count, fromIndex, toIndex) in moves) {
            val from = stacks[fromIndex]
            val to = stacks[toIndex]
            repeat(count) { to.add(from[from.size - count + it]) }
            repeat(count) { from.removeLast() }
        }
        return buildString {
            stacks.forEach { append(it.last()) }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
