package y24

import findInts
import puzzle
import splitOnEmpty

fun main() = puzzle(2024, 5) {
    val orders = inputLines.splitOnEmpty()[0].map { it.findInts() }.groupBy({ it[1] }, { it[0] })
    val updates = inputLines.splitOnEmpty()[1].map { it.findInts() }

    val comp = Comparator<Int> { a, b -> if (orders[a]?.contains(b) == true) 1 else -1 }

    submit {
        updates
            .filter { it == it.sortedWith(comp) }
            .sumOf { it[it.size / 2] }
    }

    submit {
        updates
            .filterNot { it == it.sortedWith(comp) }
            .sumOf { it.sortedWith(comp)[it.size / 2] }
    }
}