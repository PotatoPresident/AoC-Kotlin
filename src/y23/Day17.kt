package y23

import Direction
import Direction.Companion.DOWN
import Direction.Companion.LEFT
import Direction.Companion.RIGHT
import Direction.Companion.UP
import Point
import dijkstra
import findDigits
import puzzle
import toGrid
import toPoint

fun main() = puzzle(2023, 17) {
    val grid = inputLines.map { it.findDigits() }.toGrid()

    data class State(
        val pos: Point = Point(0, 0),
        val sameDir: Int = 0,
        val lastDir: Direction = RIGHT,
    )

    val directions = mapOf(
        UP to setOf(UP, RIGHT, LEFT),
        LEFT to setOf(LEFT, UP, DOWN),
        DOWN to setOf(DOWN, RIGHT, LEFT),
        RIGHT to setOf(RIGHT, UP, DOWN)
    )

    submit {
        dijkstra(State(), { it.pos == Point(grid.width - 1, grid.height - 1) }, { s ->
            directions[s.lastDir]!!
                .filter { s.sameDir < 3 || s.lastDir != it }
                .filter { s.pos + it.toPoint() in grid }
                .map {
                    State(
                        pos = s.pos + it.toPoint(),
                        sameDir = if (it == s.lastDir) s.sameDir + 1 else 1,
                        lastDir = it
                    )
                }
        }, { s -> grid[s.pos] }
        )!!.cost
    }

    submit {
        dijkstra(State(), { it.pos == Point(grid.width - 1, grid.height - 1) && it.sameDir >= 4 }, { s ->
            directions[s.lastDir]!!
                .filter {
                    if (s.sameDir > 9) s.lastDir != it
                    else if (s.sameDir < 4) s.lastDir == it
                    else true
                }
                .filter { s.pos + it.toPoint() in grid }
                .map {
                    State(
                        pos = s.pos + it.toPoint(),
                        sameDir = if (it == s.lastDir) s.sameDir + 1 else 1,
                        lastDir = it
                    )
                }
        }, { s -> grid[s.pos] }
        )!!.cost
    }
}