package y23

import findLongs
import puzzle

fun main() = puzzle(2023, 9) {
    val histories = inputLines.map { it.findLongs() }

    submit {
        histories.sumOf { list ->
            val lists = mutableListOf<MutableList<Long>>()
            lists.add(list.toMutableList())
            var i = 0

            while (lists.last().any { it != 0L }) {
                val new = mutableListOf<Long>()
                lists.add(new)
                lists[i].windowed(2).forEach { longs ->
                    new.add(longs[1] - longs[0])
                }
                if (new.isEmpty()) new.add(0)
                i++
            }

            lists.sumOf { it.last() }
        }
    }

    submit {
        histories.sumOf { list ->
            val lists = mutableListOf<MutableList<Long>>()
            lists.add(list.toMutableList())
            var i = 0

            while (lists.last().any { it != 0L }) {
                val new = mutableListOf<Long>()
                lists.add(new)
                lists[i].windowed(2).forEach { longs ->
                    new.add(longs[1] - longs[0])
                }
                if (new.isEmpty()) new.add(0)
                i++
            }

            lists.foldRight(0L) { l, acc -> l.first() - acc }
        }
    }
}