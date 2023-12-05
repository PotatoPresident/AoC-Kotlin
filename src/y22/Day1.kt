package y22

import puzzle

/**
 * [AOC 2022 Day 1](https://adventofcode.com/2022/day/1)
 */
fun main() = puzzle(2022, 1) {
    val calories = input.trim()
        .split("\n\n")
        .map { it.split("\n").map { it.toInt() }.sum() }

    submit { calories.maxOf { it } }

    submit { calories.sortedDescending().take(3).sum() }
}
