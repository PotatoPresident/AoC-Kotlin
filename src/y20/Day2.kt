package y20

import ints
import puzzle

/**
 * [AOC 2020 Day 2](https://adventofcode.com/2020/day/2)
 */
fun main() = puzzle(2020, 2) {
    val limits = inputLines.map { it.split(' ')[0].split('-').ints() }
    val limitChar = inputLines.map { it.split(' ')[1].split(':')[0].toCharArray()[0] }
    val passwords = inputLines.map { it.split(": ")[1] }
    val n = inputLines.size

    submit {
        var count = 0
        for (i in 0 until n) {
            if (passwords[i].count { c -> c == limitChar[i] } >= limits[i][0] && passwords[i].count { c -> c == limitChar[i] } <= limits[i][1]) {
                count++
            }
        }

        count
    }

    submit {
        var count = 0
        for (i in 0 until n) {
            if ((passwords[i][limits[i][0]-1] == limitChar[i]) xor (passwords[i][limits[i][1]-1] == limitChar[i])) {
                count++
            }
        }

        count
    }
}
