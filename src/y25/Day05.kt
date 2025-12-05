package y25

import longs
import puzzle
import mergeOverlap
import splitOnEmpty
import toLongRange

/**
 * [AOC 2025 Day 5](https://adventofcode.com/2025/day/5)
 */
fun main() = puzzle(2025, 5) {
    val ranges = inputLines.splitOnEmpty()[0].map { it.toLongRange() }
    val ids = inputLines.splitOnEmpty()[1].longs()

    submit {
        ids.count { id -> ranges.any { it.contains(id) } }
    }

    submit {
        ranges.mergeOverlap().sumOf { it.last - it.first + 1 }
    }
}