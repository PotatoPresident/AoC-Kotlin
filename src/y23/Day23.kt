package y23

import Direction
import Point
import asDirection
import puzzle
import toCharGrid

fun main() = puzzle(2023, 23) {
    val grid = inputLines.toCharGrid()


    fun Char.isSteepValid(direction: Direction): Boolean {
        return when (this) {
            '.' -> true
            '>' -> direction == Direction.RIGHT
            '<' -> direction == Direction.LEFT
            '^' -> direction == Direction.UP
            'v' -> direction == Direction.DOWN
            else -> false
        }
    }

    fun Char.isValid(direction: Direction): Boolean {
        return when (this) {
            '.' -> true
            '>' -> true
            '<' -> true
            '^' -> true
            'v' -> true
            else -> false
        }
    }

    submit {
        fun traverse(p: Point, dist: Int, visited: Set<Point>): Int {
            val next = grid.getNeighbors(p, includeDiagonals = false).filter { (p2, c) -> p2 !in visited && c.isSteepValid((p2-p).asDirection()) }
            return next.map { traverse(it.key, dist + 1, visited.toSet() + it.key) }.maxOrNull() ?: dist
        }

        val start = grid.toPointMap().filter { it.key.y == 0 && it.value == '.' }.keys.first()

        traverse(start, 0, setOf(start))
    }

//    submit {
//        val start = grid.toPointMap().filter { it.key.y == 0 && it.value == '.' }.keys.first()
//        val end = grid.toPointMap().filter { it.key.y == grid.height-1 && it.value == '.' }.keys.first()
//
//        val shortened = mutableMapOf<Point, Pair<Set<Point>, Int>>()
//        fun dfs(p: Point, visited: MutableSet<Point> = mutableSetOf()) {
//            if (p in shortened) return
//            var cur = p
//            val visitedNow = mutableSetOf<Point>()
//            while (grid.getNeighbors(cur, includeDiagonals = false).filter { (p2, c) -> p2 !in visited && c.isValid((p2-cur).asDirection()) }.size == 1) {
//                visited += cur
//                visitedNow += cur
//                cur = grid.getNeighbors(cur, includeDiagonals = false).filter { (p2, c) -> p2 !in visited && c.isValid((p2-cur).asDirection()) }.keys.first()
//            }
//            var intersection = grid.getNeighbors(cur, includeDiagonals = false).filter { (p2, c) -> c.isValid((p2-cur).asDirection()) }.keys
//            if (intersection.isEmpty()) {
//                intersection = setOf(cur)
//                visited += cur
//                shortened[p] = intersection to visitedNow.size
//            } else {
//                visited += cur
//                shortened[p] = intersection to visitedNow.size
//                intersection.filter { it !in visited }.forEach { dfs(it, visited.toMutableSet()) }
//            }
//        }
//        dfs(start)
//
//        val traverse: DeepRecursiveFunction<Triple<Point, Int, Set<Point>>, Int> = DeepRecursiveFunction { (p, dist, visited) ->
//            val (neighbors, distN) = shortened[p]!!
//            val next = neighbors.filter { p2 -> p2 !in visited }
//            val result = next.maxOfOrNull { this.callRecursive(Triple(it, dist + distN, visited.toSet() + it)) } ?: if (p == end) visited.size + dist else 0
//            result
//        }
//
////        visited.forEach {
////            grid[it] = 'O'
////        }
////        grid.log()
//        traverse(Triple(start, 0, setOf(start)))-1
//    }

    submit {
        val start = grid.toPointMap().filter { it.key.y == 0 && it.value == '.' }.keys.first()
        val end = grid.toPointMap().filter { it.key.y == grid.height-1 && it.value == '.' }.keys.first()

        var currentMax = 0

        fun traverse(p: Point, cur: Int = 0, visited: MutableSet<Point> = mutableSetOf()): Int {
            if (grid[p] == '#') return -1

            if (p == end) {
                if (cur > currentMax) {
                    currentMax = cur
                    println(currentMax)
                }
                return cur
            }

            val next = grid.getNeighbors(p, includeDiagonals = false).filter { (p2, c) -> p2 !in visited && c.isValid((p2-p).asDirection()) }
            visited.add(p)
            val result = next.map { traverse(it.key, cur + 1, visited) }.maxOrNull() ?: -1
            visited.remove(p)

            return result
        }

        traverse(start)
    }
}
