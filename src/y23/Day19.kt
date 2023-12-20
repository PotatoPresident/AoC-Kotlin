package y23

import findInts
import productOfLong
import puzzle
import splitOnEmpty

fun main() = puzzle(2023, 19) {
    val (first, second) = inputLines.splitOnEmpty()

    data class Workflow(val id: String, val rules: List<Pair<Triple<Char, Boolean, Int>, String>>, val default: String)

    val parts = second.map { it.split(',').map { it.findInts().first() } }
        .map { mapOf('x' to it[0], 'm' to it[1], 'a' to it[2], 's' to it[3]) }
    val workflows = first.map {
        var rules = it.substringAfter('{').split(',')
        val default = rules.last().dropLast(1)
        rules = rules.dropLast(1)
        val preds = rules.map {
            val id = it.substringBefore('<').substringBefore('>')[0]
            val greater = it.contains('>')
            val value = it.substringAfter('>').substringAfter('<').substringBefore(':').toInt()
            val result = it.substringAfter(':')
            Triple(id, greater, value) to result
        }
        Workflow(it.substringBefore('{'), preds, default)
    }

    submit {
        val total = mutableMapOf(
            'x' to 0,
            'm' to 0,
            'a' to 0,
            's' to 0
        )

        for (part in parts) {
            var cur = "in"
            while (true) {
                val workflow = workflows.find { it.id == cur }!!
                cur = workflow.default
                for ((pred, next) in workflow.rules) {
                    val (id, greater, value) = pred
                    if (greater) {
                        if (part[id]!! > value) {
                            cur = next
                            break
                        }
                    } else {
                        if (part[id]!! < value) {
                            cur = next
                            break
                        }
                    }
                }

                if (cur == "A") {
                    total['x'] = total['x']!! + part['x']!!
                    total['m'] = total['m']!! + part['m']!!
                    total['a'] = total['a']!! + part['a']!!
                    total['s'] = total['s']!! + part['s']!!
                    break
                } else if (cur == "R") {
                    break
                }
            }
        }

        total.values.sum()
    }

    fun countCombinations(workflow: String, part: Map<Char, IntRange>): Long {
        if (workflow == "A") return part.values.productOfLong { it.last - it.first.toLong() + 1 }
        else if (workflow == "R") return 0L

        val workflow = workflows.find { it.id == workflow }!!
        var total = 0L
        val cur = part.toMutableMap()
        for ((pred, target) in workflow.rules) {
            val new = cur.toMutableMap()
            val (id, greater, value) = pred
            val range = new[id]!!
            if (greater) {
                new[id] = range.first.coerceAtLeast(value+1)..range.last
                cur[id] = range.first.coerceAtMost(value)..range.last.coerceAtMost(value)
            } else {
                new[id] = range.first..range.last.coerceAtMost(value-1)
                cur[id] = range.first.coerceAtLeast(value)..range.last.coerceAtLeast(value)
            }
            total += countCombinations(target, new)
        }

        return total + countCombinations(workflow.default, cur)
    }

    submit {
        val initial = mapOf(
            'x' to 1..4000,
            'm' to 1..4000,
            'a' to 1..4000,
            's' to 1..4000
        )

        countCombinations("in", initial)
    }
}