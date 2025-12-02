package y25

import puzzle
import toLongRange

/**
 * [AOC 2025 Day 2](https://adventofcode.com/2025/day/2)
 */
fun main() = puzzle(2025, 2) {
    val ranges = input.split(",").map { it.toLongRange() }

    submit {
        ranges.sumOf { range ->
            range.filter { r ->
                val s = r.toString()

                s.substring(0, s.length / 2) == s.substring(s.length / 2)
            }.sum()
        }
    }

    submit {
        ranges.sumOf { range ->
            range.filter { r ->
                val s = r.toString()

                (s + s).drop(1).dropLast(1).contains(s)
            }.sum()
        }
    }
}