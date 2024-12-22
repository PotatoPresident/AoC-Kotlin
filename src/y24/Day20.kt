package y24

import Direction
import Point
import puzzle
import toCharGrid
import toPoint

fun main() = puzzle(2024, 20) {
    val grid = inputLines.toCharGrid()
    val start = grid.toPointMap().filterValues { it == 'S' }.keys.first()
    val end = grid.toPointMap().filterValues { it == 'E' }.keys.first()

    submit {
        fun bfs(start: Point, costMap: MutableMap<Point, Int>) {
            val queue = mutableListOf(start)
            costMap[start] = 0
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                val currentCost = costMap[current]!!
                for ((point, c) in grid.getNeighbors(current, includeDiagonals = false)) {
                    if (c == '#') continue
                    if (point !in costMap || costMap[point]!! > currentCost + 1) {
                        costMap[point] = currentCost + 1
                        queue.add(point)
                    }
                }
            }
        }

        val costToEnd = mutableMapOf<Point, Int>()
        val costToStart = mutableMapOf<Point, Int>()
        bfs(end, costToEnd)
        bfs(start, costToStart)

        val baseline = costToEnd[start]!!
        val res = mutableSetOf<Pair<Point, Point>>()

        costToStart.keys.forEach { pos ->
            val cheats = Direction.dir4.map { pos + (it.toPoint() * 2) }.filter { it in grid && grid[it] != '#' }
            cheats.forEach { cheatPos ->
                val dist = cheatPos.manhattanDistance(pos)
                val cost = costToStart[pos]!! + costToEnd[cheatPos]!! + dist
                if (cost + 100 <= baseline) {
                    res.add(pos to cheatPos)
                }
            }
        }

        res.size
    }

    submit {
        fun bfs(start: Point, costMap: MutableMap<Point, Int>) {
            val queue = mutableListOf(start)
            costMap[start] = 0
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                val currentCost = costMap[current]!!
                for ((point, c) in grid.getNeighbors(current, includeDiagonals = false)) {
                    if (c == '#') continue
                    if (point !in costMap || costMap[point]!! > currentCost + 1) {
                        costMap[point] = currentCost + 1
                        queue.add(point)
                    }
                }
            }
        }

        val costToEnd = mutableMapOf<Point, Int>()
        val costToStart = mutableMapOf<Point, Int>()
        bfs(end, costToEnd)
        bfs(start, costToStart)

        val baseline = costToEnd[start]!!
        val res = mutableSetOf<Pair<Point, Point>>()

        costToStart.keys.forEach { pos ->
            val cheats = grid.getTaxicabNeighbors(pos, 20).filterValues { it != '#' }.keys
            cheats.forEach { cheatPos ->
                val dist = cheatPos.manhattanDistance(pos)
                val cost = costToStart[pos]!! + costToEnd[cheatPos]!! + dist
                if (cost + 100 <= baseline) {
                    res.add(pos to cheatPos)
                }
            }
        }

        res.size
    }
}