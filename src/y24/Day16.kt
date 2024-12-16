package y24

import Direction
import Point
import dijkstra
import dijkstraAllPaths
import puzzle
import rotateLeft
import rotateRight
import toCharGrid
import toPoint

fun main() = puzzle(2024, 16) {
    val grid = inputLines.toCharGrid()
    val start = grid.toPointMap().filterValues { it == 'S' }.keys.first()
    val end = grid.toPointMap().filterValues { it == 'E' }.keys.first()

    data class State(val pos: Point, val dir: Direction)

    submit {
        dijkstra(
            initial = State(start, Direction.RIGHT),
            isEnd = { state -> state.pos == end },
            neighbors = { (pos, dir) ->
                val choices = mutableListOf<Pair<State, Int>>()

                val ld = dir.rotateLeft()
                choices.add(State(pos, ld) to 1000)

                val rd = dir.rotateRight()
                choices.add(State(pos, rd) to 1000)

                val forwardPos = pos + dir.toPoint()
                if (forwardPos in grid && grid[forwardPos] != '#') {
                    choices.add(State(forwardPos, dir) to 1)
                }

                choices
            }
        )!!.cost
    }

    submit {
        dijkstraAllPaths(
            initial = State(start, Direction.RIGHT),
            isEnd = { state -> state.pos == end },
            neighbors = { (pos, dir) ->
                val choices = mutableListOf<Pair<State, Int>>()

                val ld = dir.rotateLeft()
                choices.add(State(pos, ld) to 1000)

                val rd = dir.rotateRight()
                choices.add(State(pos, rd) to 1000)

                val forwardPos = pos + dir.toPoint()
                if (forwardPos in grid && grid[forwardPos] != '#') {
                    choices.add(State(forwardPos, dir) to 1)
                }

                choices
            }
        ).flatMap { it.path.map { it.pos } }.distinct().size
    }

}