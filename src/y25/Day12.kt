package y25

import findInts
import puzzle
import sumOfIndexed

/**
 * [AOC 2025 Day 11](https://adventofcode.com/2025/day/11)
 */
fun main() = puzzle(2025, 12) {
    val parts = input.split("\n\n")
    val shapes = parts.dropLast(1)
        .map { it.lines().drop(1)
        .sumOf { line -> line.count { c -> c == '#' } } }
    val regions = parts.last().lines()
    
    submit { 
        regions.count { 
            val (size, req) = it.split(':')
            val (w, h) = size.findInts()
            w * h >= req.findInts().sumOfIndexed { idx, amount -> amount * shapes[idx] }
        }
    }
}
