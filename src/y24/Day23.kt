package y24

import combinations
import puzzle

fun main() = puzzle(2024, 23) {
    val graph = mutableMapOf<String, MutableSet<String>>()
    inputLines.map { it.split('-') }.forEach { (a, b) ->
        graph.getOrPut(a) { mutableSetOf() }.add(b)
        graph.getOrPut(b) { mutableSetOf() }.add(a)
    }

    submit {
        val groups = mutableSetOf<Set<String>>()
        for (node in graph.keys) {
            graph[node]!!.combinations(2).map { it + node }.forEach { (a, b, c) ->
                if (graph[a]!!.contains(b) && graph[a]!!.contains(c)
                    && graph[b]!!.contains(c) && graph[b]!!.contains(a)
                    && graph[c]!!.contains(a) && graph[c]!!.contains(b)) {
                    groups.add(setOf(a, b, c))
                }
            }
        }

        groups.count { it.any { it.startsWith("t") } }
    }


    submit {
        sequence {
            val q = graph.keys.toMutableSet()

            while (q.isNotEmpty()) {
                val cur = q.first()
                q -= cur
                val group = hashSetOf(cur)
                for (n in graph[cur]!!) if (n !in group && group.all { n in graph[it]!! }) group += n
                yield(group)
            }
        }.maxBy { it.size }.sorted().joinToString(",")
    }
}