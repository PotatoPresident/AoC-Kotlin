package y22

import allDistinct
import puzzle

/**
 * [AOC 2022 Day 6](https://adventofcode.com/2022/day/6)
 */
fun main() = puzzle(2022, 6) {
    val datastream = input
    submit {
        datastream.windowed(4).indexOfFirst { it.toList().allDistinct() } + 4
    }
    submit {
        datastream.windowed(14).indexOfFirst { it.toList().allDistinct() } + 14
    }
}
