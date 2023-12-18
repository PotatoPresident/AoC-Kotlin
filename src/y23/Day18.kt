package y23

import Dir
import Direction
import Point
import puzzle
import toPoint
import kotlin.math.absoluteValue

@OptIn(ExperimentalStdlibApi::class)
fun main() = puzzle(2023, 18) {
    fun shoelaceArea(points: List<Point>): Double = points.windowed(2).sumOf { (a, b) ->
        a.x.toLong() * b.y - a.y.toLong() * b.x
    }.absoluteValue / 2.0

    fun calculateInteriorPoints(area: Double, boundaryPoints: Int): Long =
        (area - (boundaryPoints / 2.0) + 1).toLong()

    fun solve(plans: List<Pair<Direction, Int>>): Long {
        val corners = mutableListOf<Point>()

        var pos = Point(0, 0)
        corners.add(pos)
        plans.forEach { (dir, n) ->
            repeat(n) {
                pos += dir.toPoint()
            }
            corners.add(pos)
        }

        val area = shoelaceArea(corners)
        val boundaryPoints = corners.windowed(2).sumOf { (a, b) -> a.manhattanDistance(b) }
        val interiorPoints = calculateInteriorPoints(area, boundaryPoints)
        return interiorPoints + boundaryPoints
    }

    submit {
        fun Char.toDir() = when (this) {
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> error("Invalid direction: $this")
        }

        val plans = inputLines.map { it.split(' ') }.map { (d, n, _) -> d[0].toDir() to n.toInt() }
        solve(plans)
    }

    submit {
        fun Int.toDir() = when (this) {
            3 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            0 -> Direction.RIGHT
            else -> error("Invalid direction: $this")
        }

        val plans = inputLines.map { it.substringAfter('#').dropLast(1) }
            .map { it.takeLast(1).toInt().toDir() to it.take(5).hexToInt()  }
        solve(plans)
    }
}