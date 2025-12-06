package y25

import join
import longs
import mapToChars
import puzzle
import splitOn
import transpose

/**
 * [AOC 2025 Day 1](https://adventofcode.com/2025/day/1)
 */
fun main() = puzzle(2025, 6) {
    val problems = inputLines.map { it.trim().split("\\s+".toRegex()) }.transpose()

    submit {
        problems.sumOf { problem ->
            val nums = problem.dropLast(1).longs()
            val op = problem.last()

            if (op == "*") {
                nums.reduce(Long::times)
            } else {
                nums.reduce(Long::plus)
            }
        }
    }

    val p2Problems = inputLines.mapToChars().map { it.map { it.toString() } }.transpose()

    submit {
        val problems2 = p2Problems.map { it.dropLast(1) }.map { it.join() }.splitOn { it.isBlank() }.map { it.map { it.trim().toLong() } }
        val ops = p2Problems.map { it.takeLast(1) }.filter { it.any { it.isNotBlank() } }.flatten()
        
        problems2.zip(ops).sumOf { (problem, op) ->
            if (op == "*") {
                problem.reduce(Long::times)
            } else {
                problem.reduce(Long::plus)
            }
        }
    }
}