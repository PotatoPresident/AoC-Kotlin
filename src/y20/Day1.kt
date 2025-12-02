package y20

import combinations
import puzzle

/**
 * [AOC 2020 Day 1](https://adventofcode.com/2020/day/1)
 */
fun main() = puzzle(2020, 1) {
    val expenses = inputLines.map { it.toInt() }

    submit {
        expenses.combinations(2).first { (a, b) -> a + b == 2020 }.let { (a, b) -> a * b }
    }

    submit {
        expenses.combinations(3).first { (a, b, c) -> a + b + c == 2020 }.let { (a, b, c) -> a * b * c }
    }
}
