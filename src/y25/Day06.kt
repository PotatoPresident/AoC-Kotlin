package y25

import join
import longs
import puzzle
import splitOnEmpty
import splitWhitespace
import transpose

/**
 * [AOC 2025 Day 6](https://adventofcode.com/2025/day/6)
 */
fun main() = puzzle(2025, 6) {
    val ops = inputLines.last().splitWhitespace()
    val problems = inputLines.dropLast(1).map { it.trim().splitWhitespace() }.transpose().map { it.longs() }

    fun List<Pair<List<Long>, String>>.eval() = this.sumOf { (problem, op) ->
        when (op) {
            "*" -> problem.reduce(Long::times)
            "+" -> problem.reduce(Long::plus)
            else -> 0
        }
    }

    submit {
        problems.zip(ops).eval()
    }

    val problems2 = inputLines.dropLast(1).transpose().map { it.join().trim() }.splitOnEmpty().map { it.longs() }

    submit {
        problems2.zip(ops).eval()
    }
}
