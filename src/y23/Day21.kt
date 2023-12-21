package y23

import Point
import log
import puzzle
import toCharGrid

fun main() = puzzle(2023, 21) {
    val grid = inputLines.toCharGrid()
    val start = grid.toPointMap().toList().find { it.second == 'S' }!!.first.also {
        grid[it] = '.'
    }

    fun countSteps(start: Point, steps: Int): Set<Point> {
        val toVisit = mutableSetOf<Point>()
        toVisit.add(start)

        for (i in 0 until steps) {
            val new = mutableSetOf<Point>()
            for (p in toVisit) {
                val neighbors = grid.getNeighbors(p, includeDiagonals = false).filter { it.value == '.' }.map { it.key }
                new.addAll(neighbors)
            }
            toVisit.clear()
            toVisit.addAll(new)
        }
        return toVisit
    }

    fun countInfiniteSteps(start: Point, steps: Int): Set<Point> {
        val toVisit = mutableSetOf<Point>()
        toVisit.add(start)

        for (i in 0 until steps) {
            val new = mutableSetOf<Point>()
            for (p in toVisit) {
                val neighbors = p.getNeighboringPoints(includeDiagonals = false).filter { grid[it.x.mod(grid.width), it.y.mod(grid.height)] == '.' }
                new.addAll(neighbors)
            }
            toVisit.clear()
            toVisit.addAll(new)
        }
        return toVisit
    }

    submit {
        val spots = countSteps(start, 64)
        spots.size
    }

    submit {
        val (a, b, c) = List(3) { 65 + it * 131 to countInfiniteSteps(start, 65 + it * 131).size }

        "$a $b $c"
        // Then I used desmos lol
    }
}
