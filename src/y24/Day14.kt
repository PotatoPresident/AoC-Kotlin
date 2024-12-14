package y24

import MutablePoint
import findInts
import puzzle

fun main() = puzzle(2024, 14) {
    val robots = inputLines.map { it.split(" ").map { it.findInts() }.map { MutablePoint(it[0], it[1]) } }

    submit {
        repeat(100) {
            for (robot in robots) {
                val (pos, vel) = robot
                pos + vel
                pos.x = pos.x.mod(101)
                pos.y = pos.y.mod(103)
            }

        }

        var q1 = 0
        var q2 = 0
        var q3 = 0
        var q4 = 0

        for ((pos, vel) in robots) {
            when {
                pos.x < 50 && pos.y < 51 -> q1++
                pos.x > 50 && pos.y < 51 -> q2++
                pos.x < 50 && pos.y > 51 -> q3++
                pos.x > 50 && pos.y > 51 -> q4++
            }
        }

        q1 * q2 * q3 * q4
    }

    submit {
        var i = 0
        while (true) {
            i += 1
            for (robot in robots) {
                val (pos, vel) = robot
                pos + vel
                pos.x = pos.x.mod(101)
                pos.y = pos.y.mod(103)
            }

            // Check for overlapping robots
            val grid = Array(103) { IntArray(101) { 0 } }
            for ((pos, _) in robots) {
                grid[pos.y][pos.x]++
            }

            val hasOverlap = grid.any { row -> row.any { it > 1 } }
            if (!hasOverlap) {
                for (row in grid) {
                    for (c in row) {
                        if (c == 0) {
                            print('.')
                        } else {
                            print(c)
                        }
                    }
                    println()
                }
                break
            }

        }

        i
    }
}