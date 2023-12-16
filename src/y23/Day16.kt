package y23

import AbstractPoint
import Point
import puzzle
import toCharGrid

fun main() = puzzle(2023, 16) {
    val grid = inputLines.toCharGrid()
    fun Char.getDirections(dir: AbstractPoint.Direction): List<AbstractPoint.Direction> = when {
        this == '.' -> listOf(dir)
        this == '/' && dir == AbstractPoint.Direction.LTR -> listOf(AbstractPoint.Direction.BTT)
        this == '/' && dir == AbstractPoint.Direction.RTL -> listOf(AbstractPoint.Direction.TTB)
        this == '/' && dir == AbstractPoint.Direction.TTB -> listOf(AbstractPoint.Direction.RTL)
        this == '/' && dir == AbstractPoint.Direction.BTT -> listOf(AbstractPoint.Direction.LTR)
        this == '\\' && dir == AbstractPoint.Direction.LTR -> listOf(AbstractPoint.Direction.TTB)
        this == '\\' && dir == AbstractPoint.Direction.RTL -> listOf(AbstractPoint.Direction.BTT)
        this == '\\' && dir == AbstractPoint.Direction.TTB -> listOf(AbstractPoint.Direction.LTR)
        this == '\\' && dir == AbstractPoint.Direction.BTT -> listOf(AbstractPoint.Direction.RTL)
        this == '-' && (dir == AbstractPoint.Direction.LTR || dir == AbstractPoint.Direction.RTL) -> listOf(dir)
        this == '-' && (dir == AbstractPoint.Direction.TTB || dir == AbstractPoint.Direction.BTT) -> {
            listOf(AbstractPoint.Direction.RTL, AbstractPoint.Direction.LTR)
        }
        this == '|' && (dir == AbstractPoint.Direction.TTB || dir == AbstractPoint.Direction.BTT) -> listOf(dir)
        this == '|' && (dir == AbstractPoint.Direction.LTR || dir == AbstractPoint.Direction.RTL) -> {
            listOf(AbstractPoint.Direction.TTB, AbstractPoint.Direction.BTT)
        }

        else -> error("Invalid direction: $this")
    }
    fun getEnergized(start: Point, startDir: AbstractPoint.Direction): Int {
        val queue = ArrayDeque<Pair<Point, AbstractPoint.Direction>>()
        queue.addFirst(start to startDir)
        val visited = mutableSetOf<Pair<Point, AbstractPoint.Direction>>()

        while (queue.isNotEmpty()) {
            val pair = queue.removeFirst()
            val (p, dir) = pair
            if (grid.contains(p) && (pair !in visited)) {
                visited.add(pair)
                grid[p].getDirections(dir).forEach {
                    queue.addFirst(it.next(p) to it)
                }
            }
        }

        return visited.map { it.first }.toSet().size
    }

    submit {
        getEnergized(Point(0, 0), AbstractPoint.Direction.LTR)
    }

    submit {
        maxOf(
            (0..<grid.width).maxOf { getEnergized(Point(it, 0), AbstractPoint.Direction.TTB) },
            (0..<grid.width).maxOf { getEnergized(Point(it, grid.height - 1), AbstractPoint.Direction.BTT) },
            (0..<grid.height).maxOf { getEnergized(Point(0, it), AbstractPoint.Direction.LTR) },
            (0..<grid.height).maxOf { getEnergized(Point(grid.width - 1, it), AbstractPoint.Direction.RTL) }
        )
    }
}