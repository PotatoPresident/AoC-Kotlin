package y23

import AbstractPoint
import Grid
import Point
import log
import puzzle
import toGrid

fun main() = puzzle(2023, 10) {
    val grid = inputLines.map { it.toCharArray().toList() }.toGrid()

//    submit {
//        val start = grid.toPointMap().toList().find { it.second == 'S' }!!.first
//
//        var prev = AbstractPoint.Direction.LTR
//        var cur: Point = start.plus(Point(1, 0))
//
//        var i = 0
//        while (cur != start) {
//            val dir = grid[cur].direction(prev)
//            prev = dir
//            cur = dir.next(cur)
//            i++
//        }
//
//        (i + 1) / 2
//    }

    submit {
        val start = grid.toPointMap().toList().find { it.second == 'S' }!!.first

        var prev = AbstractPoint.Direction.TTB
        var cur: Point = start.plus(Point(0, 1))

        val loop = mutableSetOf<Point>()
        loop.add(start)
        while (cur != start) {
            loop.add(cur)
            val dir = grid[cur].direction(prev)
            prev = dir
            cur = dir.next(cur)
        }

        val newGrid = grid.scale(loop)
        newGrid.log()
        println()

        val spaces = newGrid.toPointMap().filter { it.value == '.' }.map { it.key }.toSet()
        val escapes = mutableSetOf<Point>()
        val enclosed = mutableSetOf<Point>()

        fun visitNeighbors(point: Point, visited: MutableSet<Point>): Boolean {
            val toVisit = ArrayDeque<Point>()
            toVisit.add(point)

            while (toVisit.isNotEmpty()) {
                val p = toVisit.removeFirst()
                visited.add(p)
                if (p in escapes) return true
                if (p in enclosed) return false
                val neighbors = newGrid.getNeighbors(p, includeDiagonals = false)
                if (neighbors.size < 4) {
                    visited.addAll(neighbors.filter { it.value == '.' }.map { it.key })
                    return true
                }

                val neighborsF = neighbors.filter { it.value == '.' }
                if (neighborsF.any { escapes.contains(it.key) }) {
                    return true
                } else if (neighborsF.any { enclosed.contains(it.key) }) {
                    visited.addAll(neighborsF.map { it.key })
                    return false
                }

                if (neighborsF.any { it.key !in visited}) {
                    toVisit.addAll(neighborsF.filter { it.key !in visited }.map { it.key })
                    visited.addAll(neighborsF.map { it.key })
                }
            }

            return false
        }

        while (spaces.size > escapes.size + enclosed.size) {
            val initial = spaces.first { it !in escapes && it !in enclosed }
            val visited = mutableSetOf<Point>()

            val escaped = visitNeighbors(initial, visited)
            if (escaped) {
                escapes += visited
            } else {
                enclosed += visited
            }
        }

        var i = 0
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (newGrid[Point(x * 3 + 1, y * 3 + 1)] == '.' && enclosed.contains(Point(x * 3 + 1, y * 3 + 1))) {
                    val neighbors = newGrid.getNeighbors(Point(x * 3 + 1, y * 3 + 1), includeDiagonals = true)
                    if (neighbors.size == 8 && neighbors.all { it.value == '.' }) {
                        i++
                    }
                }
            }
        }

        enclosed.forEach { newGrid[it] = 'X' }
        newGrid.log()

        i
    }
}

private fun Grid<Char>.scale(loop: MutableSet<Point>): Grid<Char> {
    val newGrid = Grid<Char>(width * 3, height * 3, '.')
    this.toPointMap().forEach { (point, char) ->
//        newGrid.log()
//        println()
        if (loop.contains(point)) {
            val new = point * 3

            if (char == '|') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '.'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '|'
                newGrid[new + Point(1, 1)] = '|'
                newGrid[new + Point(1, 2)] = '|'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '.'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '-') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '-'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '.'
                newGrid[new + Point(1, 1)] = '-'
                newGrid[new + Point(1, 2)] = '.'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '-'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == 'L') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '.'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '|'
                newGrid[new + Point(1, 1)] = 'L'
                newGrid[new + Point(1, 2)] = '.'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '-'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == 'J') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '-'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '|'
                newGrid[new + Point(1, 1)] = 'J'
                newGrid[new + Point(1, 2)] = '.'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '.'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '7') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '-'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '.'
                newGrid[new + Point(1, 1)] = '7'
                newGrid[new + Point(1, 2)] = '|'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '.'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == 'F') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '.'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '.'
                newGrid[new + Point(1, 1)] = 'F'
                newGrid[new + Point(1, 2)] = '|'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '-'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == 'S') {
                newGrid[new] = 'F'
                newGrid[new + Point(0, 1)] = '|'
                newGrid[new + Point(0, 2)] = 'L'
                newGrid[new + Point(1, 0)] = '-'
                newGrid[new + Point(1, 1)] = '-'
                newGrid[new + Point(1, 2)] = '-'
                newGrid[new + Point(2, 0)] = '7'
                newGrid[new + Point(2, 1)] = '|'
                newGrid[new + Point(2, 2)] = 'J'
            }
        }
    }

    return newGrid
}

private fun Char.isVertical(left: Boolean) = when {
    this == '|' -> true
    this == 'L' && !left -> true
    this == 'J' && left -> true
    this == '7' && left -> true
    this == 'F' && !left -> true
    else -> false
}

private fun Char.isHorizontal(top: Boolean) = when {
    this == '-' -> true
    this == 'L' && !top -> true
    this == 'J' && !top -> true
    this == '7' && top -> true
    this == 'F' && top -> true
    else -> false
}

private fun Char.direction(from: AbstractPoint.Direction): AbstractPoint.Direction = when {
    this == '-' && from == AbstractPoint.Direction.LTR -> AbstractPoint.Direction.LTR
    this == '-' && from == AbstractPoint.Direction.RTL -> AbstractPoint.Direction.RTL
    this == '|' && from == AbstractPoint.Direction.TTB -> AbstractPoint.Direction.TTB
    this == '|' && from == AbstractPoint.Direction.BTT -> AbstractPoint.Direction.BTT
    this == 'L' && from == AbstractPoint.Direction.TTB -> AbstractPoint.Direction.LTR
    this == 'L' && from == AbstractPoint.Direction.RTL -> AbstractPoint.Direction.BTT
    this == 'J' && from == AbstractPoint.Direction.TTB -> AbstractPoint.Direction.RTL
    this == 'J' && from == AbstractPoint.Direction.LTR -> AbstractPoint.Direction.BTT
    this == '7' && from == AbstractPoint.Direction.LTR -> AbstractPoint.Direction.TTB
    this == '7' && from == AbstractPoint.Direction.BTT -> AbstractPoint.Direction.RTL
    this == 'F' && from == AbstractPoint.Direction.BTT -> AbstractPoint.Direction.LTR
    this == 'F' && from == AbstractPoint.Direction.RTL -> AbstractPoint.Direction.TTB

    else -> error("Invalid direction: $this")
}