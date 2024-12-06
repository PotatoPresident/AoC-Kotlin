package y24

import Grid
import Point
import puzzle
import toCharGrid
import toGrid

fun main() = puzzle(2024, 6) {
    val grid = inputLines.toCharGrid()
    val start = grid.toPointMap().entries.first { it.value == '^' }.key

    data class Path(val positions: Set<Point>, val isCycle: Boolean)

    fun Grid<Char>.getPath(): Path {
        var dir = Point(0, -1)
        var pos = start
        val positions = mutableSetOf(pos)
        val visited = mutableSetOf<Pair<Point, Point>>()

        while (pos + dir in this) {
            val state = pos to dir
            if (state in visited) {
                return Path(positions, true)
            }
            visited.add(state)

            if (this[pos + dir] == '#') {
                dir = Point(-dir.y, dir.x)
                continue
            }
            pos += dir
            positions.add(pos)
        }

        return Path(positions, false)
    }

    submit {
        grid.getPath().positions.size
    }

    submit {
        grid.getPath().positions.filter { pos -> pos != start }
            .count { pos ->
                grid.toGrid().apply { this[pos] = '#' }.getPath().isCycle
            }
    }
}