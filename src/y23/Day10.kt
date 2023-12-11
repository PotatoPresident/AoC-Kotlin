package y23

import AbstractPoint
import Grid
import Point
import puzzle
import toGrid

fun main() = puzzle(2023, 10) {
    val grid = inputLines.map { it.toCharArray().toList().map(Char::replaceNice) }.toGrid()
    val loop = mutableSetOf<Point>()
    val start = grid.toPointMap().toList().find { it.second == 'S' }!!.first

    submit {
        loop.add(start)

        var prev = AbstractPoint.Direction.TTB
        var cur: Point = start.plus(Point(0, 1))

        while (cur != start) {
            loop.add(cur)
            val dir = grid[cur].direction(prev)
            prev = dir
            cur = dir.next(cur)
        }

        loop.size / 2
    }

    submit {
        val newGrid = grid.scale(loop)

        val visited = mutableSetOf<Point>()
        val toVisit = ArrayDeque<Point>()
        toVisit.add(Point(0, 0))

        while (toVisit.isNotEmpty()) {
            val p = toVisit.removeFirst()
            visited.add(p)

            val neighbors = newGrid.getNeighbors(p, includeDiagonals = false).filter { it.key !in visited && it.value == '.' }.toList()
            toVisit.addAll(neighbors.map { it.first })
            visited.addAll(neighbors.map { it.first })
        }

        var i = 0
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (newGrid[Point(x * 3 + 1, y * 3 + 1)] == '.' && !visited.contains(Point(x * 3 + 1, y * 3 + 1))) {
                    val neighbors = newGrid.getNeighbors(Point(x * 3 + 1, y * 3 + 1), includeDiagonals = true)
                    if (neighbors.size == 8 && neighbors.all { it.value == '.' }) {
                        i++
                    }
                }
            }
        }

        val visualize = false
        if (visualize) {
            for (y in 0 until grid.height) {
                for (x in 0 until grid.width) {
                    val p = Point(x, y)
                    if (p == start) {
                        print("\u001B[31m")
                        print(grid[p])
                        print("\u001B[0m")
                    } else if (p in loop) {
                        print("\u001B[34m")
                        print(grid[p])
                        print("\u001B[0m")
                    } else if (p * 3 !in visited) {
                        print("\u001B[32m")
                        print(grid[p])
                        print("\u001B[0m")
                    } else {
                        print(grid[p])
                    }
                }
                println()
            }
        }

        i
    }
}

private fun Grid<Char>.scale(loop: Set<Point>): Grid<Char> {
    val newGrid = Grid<Char>(width * 3, height * 3, '.')
    this.toPointMap().forEach { (point, char) ->
//        newGrid.log()
//        println()
        if (loop.contains(point)) {
            val new = point * 3

            if (char == '┃') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '.'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '┃'
                newGrid[new + Point(1, 1)] = '┃'
                newGrid[new + Point(1, 2)] = '┃'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '.'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '━') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '━'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '.'
                newGrid[new + Point(1, 1)] = '━'
                newGrid[new + Point(1, 2)] = '.'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '━'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '┗') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '.'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '┃'
                newGrid[new + Point(1, 1)] = '┗'
                newGrid[new + Point(1, 2)] = '.'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '━'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '┛') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '━'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '┃'
                newGrid[new + Point(1, 1)] = '┛'
                newGrid[new + Point(1, 2)] = '.'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '.'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '┓') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '━'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '.'
                newGrid[new + Point(1, 1)] = '┓'
                newGrid[new + Point(1, 2)] = '┃'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '.'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == '┏') {
                newGrid[new] = '.'
                newGrid[new + Point(0, 1)] = '.'
                newGrid[new + Point(0, 2)] = '.'
                newGrid[new + Point(1, 0)] = '.'
                newGrid[new + Point(1, 1)] = '┏'
                newGrid[new + Point(1, 2)] = '┃'
                newGrid[new + Point(2, 0)] = '.'
                newGrid[new + Point(2, 1)] = '━'
                newGrid[new + Point(2, 2)] = '.'
            } else if (char == 'S') {
                newGrid[new] = '┏'
                newGrid[new + Point(0, 1)] = '┃'
                newGrid[new + Point(0, 2)] = '┗'
                newGrid[new + Point(1, 0)] = '━'
                newGrid[new + Point(1, 1)] = '━'
                newGrid[new + Point(1, 2)] = '━'
                newGrid[new + Point(2, 0)] = '┓'
                newGrid[new + Point(2, 1)] = '┃'
                newGrid[new + Point(2, 2)] = '┛'
            }
        }
    }

    return newGrid
}

private fun Char.direction(from: AbstractPoint.Direction): AbstractPoint.Direction = when {
    this == '━' && from == AbstractPoint.Direction.LTR -> AbstractPoint.Direction.LTR
    this == '━' && from == AbstractPoint.Direction.RTL -> AbstractPoint.Direction.RTL
    this == '┃' && from == AbstractPoint.Direction.TTB -> AbstractPoint.Direction.TTB
    this == '┃' && from == AbstractPoint.Direction.BTT -> AbstractPoint.Direction.BTT
    this == '┗' && from == AbstractPoint.Direction.TTB -> AbstractPoint.Direction.LTR
    this == '┗' && from == AbstractPoint.Direction.RTL -> AbstractPoint.Direction.BTT
    this == '┛' && from == AbstractPoint.Direction.TTB -> AbstractPoint.Direction.RTL
    this == '┛' && from == AbstractPoint.Direction.LTR -> AbstractPoint.Direction.BTT
    this == '┓' && from == AbstractPoint.Direction.LTR -> AbstractPoint.Direction.TTB
    this == '┓' && from == AbstractPoint.Direction.BTT -> AbstractPoint.Direction.RTL
    this == '┏' && from == AbstractPoint.Direction.BTT -> AbstractPoint.Direction.LTR
    this == '┏' && from == AbstractPoint.Direction.RTL -> AbstractPoint.Direction.TTB

    else -> error("Invalid direction: $this")
}

private fun Char.replaceNice() = when (this) {
    '-' -> '━'
    'L' -> '┗'
    'J' -> '┛'
    '7' -> '┓'
    'F' -> '┏'
    '|' -> '┃'
    else -> this
}
