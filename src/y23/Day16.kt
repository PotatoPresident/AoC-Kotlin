package y23

import Direction
import Point
import puzzle
import toCharGrid

fun main() = puzzle(2023, 16) {
    val grid = inputLines.toCharGrid()
    fun Char.getDirections(dir: Direction): List<Direction> = when {
        this == '.' -> listOf(dir)
        this == '/' && dir == Direction.LTR -> listOf(Direction.BTT)
        this == '/' && dir == Direction.RTL -> listOf(Direction.TTB)
        this == '/' && dir == Direction.TTB -> listOf(Direction.RTL)
        this == '/' && dir == Direction.BTT -> listOf(Direction.LTR)
        this == '\\' && dir == Direction.LTR -> listOf(Direction.TTB)
        this == '\\' && dir == Direction.RTL -> listOf(Direction.BTT)
        this == '\\' && dir == Direction.TTB -> listOf(Direction.LTR)
        this == '\\' && dir == Direction.BTT -> listOf(Direction.RTL)
        this == '-' && (dir == Direction.LTR || dir == Direction.RTL) -> listOf(dir)
        this == '-' && (dir == Direction.TTB || dir == Direction.BTT) -> {
            listOf(Direction.RTL, Direction.LTR)
        }
        this == '|' && (dir == Direction.TTB || dir == Direction.BTT) -> listOf(dir)
        this == '|' && (dir == Direction.LTR || dir == Direction.RTL) -> {
            listOf(Direction.TTB, Direction.BTT)
        }

        else -> error("Invalid direction: $this")
    }
    fun getEnergized(start: Point, startDir: Direction): Int {
        val queue = ArrayDeque<Pair<Point, Direction>>()
        queue.addFirst(start to startDir)
        val visited = mutableSetOf<Pair<Point, Direction>>()

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
        getEnergized(Point(0, 0), Direction.LTR)
    }

    submit {
        maxOf(
            (0..<grid.width).maxOf { getEnergized(Point(it, 0), Direction.TTB) },
            (0..<grid.width).maxOf { getEnergized(Point(it, grid.height - 1), Direction.BTT) },
            (0..<grid.height).maxOf { getEnergized(Point(0, it), Direction.LTR) },
            (0..<grid.height).maxOf { getEnergized(Point(grid.width - 1, it), Direction.RTL) }
        )
    }
}