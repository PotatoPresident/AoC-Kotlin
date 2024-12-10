package y24

import Direction
import Point
import puzzle
import toCharGrid

fun main() = puzzle(2024, 10) {
    val grid = inputLines.toCharGrid()
    val trailheads = grid.toPointMap().filter { it.value == '0' }.keys

    submit {
        fun dfs(prev: Int, p: Point, visited: MutableSet<Point>) {
            if (p !in grid || p in visited || prev + 1 != grid[p].digitToInt()) return
            val value = grid[p].digitToInt()

            visited += p
            Direction.dir4.forEach { dir -> dfs(value, p + dir, visited) }
        }

        trailheads.sumOf {
            mutableSetOf<Point>().apply { dfs(-1, it, this) }.count { grid[it] == '9' }
        }
    }

    submit {
        fun dfs(prev: Int, p: Point): Int {
            if (p !in grid || prev + 1 != grid[p].digitToInt()) return 0
            val value = grid[p].digitToInt()

            return (if (value == 9) 1 else 0) + Direction.dir4.sumOf { dir -> dfs(value, p + dir) }
        }

        trailheads.sumOf { dfs(-1, it) }
    }
}
