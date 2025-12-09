package y25

import Point
import combinations
import puzzle
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * [AOC 2025 Day 9](https://adventofcode.com/2025/day/9)
 */
fun main() = puzzle(2025, 9) {
    val points = inputLines.map { it.split(",").map(String::toInt).let { Point(it[0], it[1]) } }

    submit {
        points.combinations(2).maxOf { (a, b) ->
            val l = (a.x - b.x + 1L).absoluteValue
            val w = (a.y - b.y + 1L).absoluteValue

            l * w
        }
    }

    data class Rect(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
        val area: Long get() = (x2 - x1 + 1L) * (y2 - y1 + 1L)

        constructor(p1: Point, p2: Point) : this(
            min(p1.x, p2.x), max(p1.x, p2.x),
            min(p1.y, p2.y), max(p1.y, p2.y)
        )

        fun isValid(polygon: List<Point>): Boolean {
            val edges = polygon.zipWithNext() + (polygon.last() to polygon.first())

            return edges.none { (pA, pB) -> intersectsEdge(pA, pB) }
        }

        private fun intersectsEdge(pA: Point, pB: Point): Boolean {
            return if (pA.x == pB.x) {
                val edgeX = pA.x
                val (minEy, maxEy) = min(pA.y, pB.y) to max(pA.y, pB.y)
                (edgeX in (x1 + 1)..<x2) && (max(minEy, y1) < min(maxEy, y2))
            } else {
                val edgeY = pA.y
                val (minEx, maxEx) = min(pA.x, pB.x) to max(pA.x, pB.x)
                (edgeY in (y1 + 1)..<y2) && (max(minEx, x1) < min(maxEx, x2))
            }
        }
    }

    submit {
        points.combinations(2)
            .filter { (p1, p2) -> p1.x != p2.x && p1.y != p2.y }
            .map { (p1, p2) -> Rect(p1, p2) }
            .filter { it.isValid(points) }
            .maxOf { it.area }
    }
}
