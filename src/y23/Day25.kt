package y23

import puzzle

fun main() = puzzle(2023, 25) {
    val graph: MutableMap<String, MutableList<String>> = mutableMapOf()
    inputLines.map { it.split(":") }.forEach { (from, a) ->
        val to = a.drop(1).split(" ").map { it.trim() }
        graph.getOrPut(from) { mutableListOf() }.addAll(to)
        to.forEach {
            graph.getOrPut(it) { mutableListOf() }.add(from)
        }
        //rxt-bqq
        //bgl-vfx
        //btp-qxr
    }

    submit {
        graph.forEach { (t, u) ->
            println("$t -- {${u.joinToString(" ")}};")
        }

        val left = mutableSetOf<String>()
        val right = mutableSetOf<String>()

        fun dfsLeft(cur: String) {
            if (cur in left) return
            left.add(cur)
            graph[cur]?.forEach { dfsLeft(it) }
        }
        dfsLeft("pqm")

        fun dfsRight(cur: String) {
            if (cur in right) return
            right.add(cur)
            graph[cur]?.forEach { dfsRight(it) }
        }
        dfsRight("gpt")

        left.size * right.size
    }
}