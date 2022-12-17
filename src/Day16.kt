import kotlin.math.max

fun main() {

    fun part1(input: List<String>): Int {
        val tunnels = mutableMapOf<String, MutableList<String>>()
        val rates = mutableMapOf<String, Int>()
        val ids = mutableMapOf<String, Int>()
        for ((id, line) in input.withIndex()) {
            val t = line.split(" ", "=", "; ", ", ")
            t.subList(10, t.size).toCollection(
                tunnels.computeIfAbsent(t[1]) { mutableListOf() }
            )
            ids[t[1]] = id
            rates.put(t[1], t[5].toInt())
        }

        data class S(val v: String, val opened: Long)

        val d = Array(30 + 1) { HashMap<S, Int>() }

        d[0].put(S("AA", 0), 0)

        fun put(time: Int, valve: String, opened: Long, pressure: Int) {
            val s = S(valve, opened)
            d[time].compute(s) { _, current ->
                if (current == null) pressure
                else max(current, pressure)
            }
        }

        for (t in 0 until 30) {
            for ((s, r) in d[t]) {
                val (v, m) = s
                val rate = rates[v]!!
                if (rate > 0 && m and (1L shl ids[v]!!) == 0L) {
                    put(t + 1, v, m + (1L shl ids[v]!!), r + (30 - t - 1) * rate)
                }
                for (u in tunnels.getOrDefault(v, emptyList())) {
                    put(t + 1, u, m, r)
                }
            }
        }

        return d[30].maxOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val tunnels = mutableMapOf<String, MutableList<String>>()
        val rates = mutableMapOf<String, Int>()
        val ids = mutableMapOf<String, Int>()
        for ((id, line) in input.withIndex()) {
            val t = line.split(" ", "=", "; ", ", ")
            t.subList(10, t.size).toCollection(
                tunnels.computeIfAbsent(t[1]) { mutableListOf() }
            )
            ids[t[1]] = id
            rates.put(t[1], t[5].toInt())
        }

        data class S(val v: String, val e: String, val opened: Long)

        val d = Array(26 + 1) { HashMap<S, Int>() }

        d[0].put(S("AA", "AA", 0), 0)

        var mx = 0

        fun put(time: Int, myValve: String, elValve: String, opened: Long, pressure: Int) {
            val s = S(myValve, elValve, opened)
            val x = d[time].compute(s) { _, current ->
                if (current == null) pressure
                else max(current, pressure)
            }
            if (x!! > mx) {
                mx = x
            }
        }

        val all = rates.entries.fold(0L) { acc, entry -> if (entry.value > 0) acc + (1L shl ids[entry.key]!!) else acc }

        for (t in 0 until 26) {
            for ((s, r) in d[t]) {
                val (v, e, m) = s
                if (m == all) continue
                val rv = rates[v]!!
                val re = rates[e]!!
                if (v == e) {
                    if (rv > 0 && m and (1L shl ids[v]!!) == 0L) {
                        for (u in tunnels.getOrDefault(e, emptyList())) {
                            put(t + 1, v, u, m + (1L shl ids[v]!!), r + (26 - t - 1) * rv)
                        }
                    }
                } else {
                    if (rv > 0 && m and (1L shl ids[v]!!) == 0L && re > 0 && m and (1L shl ids[e]!!) == 0L) {
                        put(t + 1, v, e, m + (1L shl ids[v]!!) + (1L shl ids[e]!!), r + (26 - t - 1) * (rv + re))
                    } else if (rv > 0 && m and (1L shl ids[v]!!) == 0L) {
                        for (u in tunnels.getOrDefault(e, emptyList())) {
                            put(t + 1, v, u, m + (1L shl ids[v]!!), r + (26 - t - 1) * rv)
                        }
                    } else if (re > 0 && m and (1L shl ids[e]!!) == 0L) {
                        for (u in tunnels.getOrDefault(v, emptyList())) {
                            put(t + 1, u, e, m + (1L shl ids[e]!!), r + (26 - t - 1) * re)
                        }
                    }
                }
                for (uv in tunnels.getOrDefault(v, emptyList())) {
                    for (ue in tunnels.getOrDefault(e, emptyList())) {
                        put(t + 1, uv, ue, m, r)
                    }
                }
            }
            d[t].clear()
        }

        return mx
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
