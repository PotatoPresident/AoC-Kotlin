package y24

import Grid
import Point
import dijkstra
import findInts
import puzzle

fun main() = puzzle(2024, 18) {
    val positions = inputLines.map { it.findInts() }

    submit {
        val grid = Grid(71, 71, '.')
        for (i in 0 until 1024) {
            val (x, y) = positions[i]
            grid[Point(x, y)] = '#'
        }

        val res = dijkstra(Point(0,0), { it == Point(70, 70) },
            { grid.getNeighbors(it, includeDiagonals = false).filter { it.value != '#' }.map { it.key to 1 } })!!
        res.cost
    }

    submit {
        val grid = Grid(71, 71, '.')
        for ((x, y) in positions) {
            grid[Point(x, y)] = '#'
            val res = dijkstra(Point(0,0), { it == Point(70, 70) },
                { grid.getNeighbors(it, includeDiagonals = false).filter { it.value != '#' }.map { it.key to 1 } })

            if (res == null) {
                return@submit "$x,$y"
            }
        }
    }
}