package y25

import combinations
import mapToChars
import puzzle

/**
 * [AOC 2025 Day 3](https://adventofcode.com/2025/day/3)
 */
fun main() = puzzle(2025, 3) {
    val banks = inputLines.mapToChars().map { it.map { it.digitToInt() } }

    submit {
        banks.sumOf { it.combinations(2).maxOf { (a, b) -> a * 10 + b } }
    }

    submit {
        banks.sumOf { bank ->
            val stack = ArrayDeque<Int>()
            var dropCount = bank.size - 12

            bank.forEach { digit ->
                while (dropCount > 0 && stack.isNotEmpty() && stack.last() < digit) {
                    stack.removeLast()
                    dropCount--
                }
                stack.addLast(digit)
            }

            stack.take(12).fold(0L) { acc, digit -> acc * 10 + digit }
        }
    }
}