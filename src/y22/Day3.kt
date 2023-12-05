package y22

import puzzle

/**
 * [AOC 2022 Day 3](https://adventofcode.com/2022/day/3)
 */
fun main() = puzzle(2022, 3) {
    val sacks = inputLines
    val compartments = sacks.map { it.substring(0, it.length/2) to it.substring(it.length/2) }

    fun findDuplicates(sacks: List<String>): Set<Char> {
        val duplicates = mutableSetOf<Char>()

        sacks.first().toCharArray().forEach { char ->
            if (sacks.all { it.contains(char) }) {
                duplicates.add(char)
            }
        }

        return duplicates
    }

    submit { compartments.map { findDuplicates(it.toList()) }.sumOf { it.sumOf { it.priority } } }

    submit { sacks.windowed(3, 3).sumOf { findDuplicates(it).first().priority } }
}

private val Char.priority: Int
    get() = when (this) {
        in 'A'..'Z' -> this - 'A' + 27
        in 'a'..'z' -> this - 'a' + 1
        else -> throw IllegalArgumentException("Invalid char: $this")
    }
