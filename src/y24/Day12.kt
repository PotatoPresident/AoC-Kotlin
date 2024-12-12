package y24

import Direction
import Grid
import Point
import puzzle
import rotateLeft
import rotateRight
import toCharGrid

fun main() = puzzle(2024, 12) {
    val grid = inputLines.toCharGrid()

    fun floodFill(grid: Grid<Char>, start: Point, target: Char): Set<Point> {
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque<Point>().apply { add(start) }

        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()

            if (p !in grid || p in visited || grid[p] != target) continue

            visited += p
            queue.addAll(Direction.dir4.map { p + it })
        }

        return visited
    }

    fun area(points: Set<Point>): Int {
        return points.size
    }

    fun perimeter(points: Set<Point>): Int {
        return points.sumOf { p -> Direction.dir4.count { p + it !in points } }
    }

    submit {
        val visited = mutableSetOf<Point>()

        var result = 0L
        for ((point, region) in grid.toPointMap()) {
            if (point in visited) continue
            val points = floodFill(grid, point, region)
            val cost = area(points) * perimeter(points)
            result += cost
            visited += points
        }

        result
    }

    fun sides(points: Set<Point>): Int {
        val edges = mutableSetOf<Pair<Point, Direction>>()

        points.forEach { p ->
            Direction.dir4.forEach {
                if (p + it !in points) {
                    edges += p to it
                }
            }
        }

        var sides = 0
        while (edges.isNotEmpty()) {
            val (p, dir) = edges.first()
            edges.remove(p to dir)
            sides++

            var next = p + dir.rotateRight()
            while (next to dir in edges) {
                edges.remove(next to dir)
                next += dir.rotateRight()
            }
            next = p + dir.rotateLeft()
            while (next to dir in edges) {
                edges.remove(next to dir)
                next += dir.rotateLeft()
            }
        }
        return sides
    }

    submit {
        val visited = mutableSetOf<Point>()

        var result = 0L
        for ((point, region) in grid.toPointMap()) {
            if (point in visited) continue
            val points = floodFill(grid, point, region)
            val cost = area(points) * sides(points)
            result += cost
            visited += points
        }

        result
    }
}
