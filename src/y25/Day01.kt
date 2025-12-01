package y25

import puzzle

/**
 * [AOC 2025 Day 1](https://adventofcode.com/2025/day/1)
 */
fun main() = puzzle(2025, 1) {
    val turns = inputLines.map { it[0] to it.drop(1).toInt()}

    submit {
        var cur = 50
        var res = 0
        for ((dir, count) in turns) {
            for (i in 0 until count) {
                if (dir == 'L') {
                    cur -= 1
                } else {
                    cur += 1
                }

                if (cur == -1) {
                    cur = 99
                } else if (cur == 100) {
                    cur = 0
                }
            }

            if (cur == 0){
                res += 1
            }
        }

        res
    }

    submit {
        var cur = 50
        var res = 0
        for ((dir, count) in turns) {
            for (i in 0 until count) {
                if (dir == 'L') {
                    cur -= 1
                } else {
                    cur += 1
                }

                if (cur == -1) {
                    cur = 99
                } else if (cur == 100) {
                    cur = 0
                }

                if (cur == 0){
                    res += 1
                }
            }
        }

        res
    }
}