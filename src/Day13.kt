sealed interface Packet : Comparable<Packet> {

    @JvmInline
    value class Integer(val data: Int) : Packet {
        override fun compareTo(other: Packet): Int = when (other) {
            is Integer -> data compareTo other.data
            is List -> List(listOf(this)) compareTo other
        }
    }

    @JvmInline
    value class List(val data: kotlin.collections.List<Packet>) : Packet {
        override fun compareTo(other: Packet): Int {
            return when (other) {
                is Integer -> this compareTo List(listOf(other))
                is List -> {
                    val it = data.iterator()
                    val otherIt = other.data.iterator()
                    while (it.hasNext() && otherIt.hasNext()) {
                        val compare = it.next() compareTo otherIt.next()
                        if (compare != 0) return compare
                    }
                    if (!it.hasNext() && !otherIt.hasNext()) return 0
                    if (otherIt.hasNext()) -1 else 1
                }
            }
        }
    }

    companion object {
        class Parser(val input: String) {
            private var lookAt: Int = 0

            private fun peek(): Char = input[lookAt]
            private fun next() = if (lookAt + 1 < input.length) input[++lookAt] else '\n'
            private fun skip(char: Char) {
                check(peek() == char)
                ++lookAt
            }

            fun parse(): Packet {
                when (val current = peek()) {
                    '[' -> {
                        val data = mutableListOf<Packet>()
                        if (next() != ']') {
                            while (peek() != ']') {
                                data += parse()
                                if (peek() == ']') break
                                skip(',')
                            }
                        }
                        skip(']')
                        return List(data)
                    }

                    in '0'..'9' -> {
                        var data = current.digitToInt()
                        while (next().isDigit()) {
                            data = data * 10 + peek().digitToInt()
                        }
                        return Integer(data)
                    }

                    else -> {
                        error("unknown token at index $lookAt: $input")
                    }
                }
            }
        }


    }
}

fun main() {

    fun String.parse() = Packet.Companion.Parser(this).parse()

    fun part1(input: List<String>): Int = input.chunked(3).withIndex()
        .filter { (_, chunk) ->
            val (first, second, _) = chunk
            first.parse() < second.parse()
        }
        .sumOf { (index, _) -> index + 1 }

    fun part2(input: List<String>): Int {
        val d1 = "[[6]]".parse()
        val d2 = "[[2]]".parse()
        val signals = mutableListOf(d1, d2)
        for (line in input) {
            if (line.isNotEmpty()) {
                signals += line.parse()
            }
        }
        signals.sort()
        return (signals.indexOf(d1) + 1) * (signals.indexOf(d2) + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
