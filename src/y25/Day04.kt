package y25

import puzzle
import toCharGrid

/**
 * [AOC 2025 Day 4](https://adventofcode.com/2025/day/4)
 */
fun main() = puzzle(2025, 4) {
    val grid = inputLines.toCharGrid()

    submit {
        grid.toPointMap().filter { it.value == '@' }.count { (point, c) ->
            val nei = grid.getNeighbors(point, true)

            nei.values.count { it == '@' } < 4
        }
    }

    submit {
        var rolls = grid.toPointMap().values.count { it == '@' }
        var removed = 0

        while (true) {
            val prev = rolls

            grid.toPointMap().filter { it.value == '@' }.forEach { (point, c) ->
                val nei = grid.getNeighbors(point, true)

                if (nei.values.count { it == '@' } < 4) {
                    grid[point] = '.'
                    rolls -= 1
                    removed++
                }
            }

            if (prev == rolls) break
        }

        removed
    }
}