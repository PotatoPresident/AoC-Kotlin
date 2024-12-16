package y24

import Direction
import Point
import log
import puzzle
import splitOnEmpty
import toCharGrid

fun main() = puzzle(2024, 15) {
    fun Char.toDir() = when (this) {
        '<' -> Direction.LEFT
        '>' -> Direction.RIGHT
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        else -> Direction.UP
    }

    val instructions = inputLines.splitOnEmpty()[1].flatMap { it.map { it.toDir() } }

    submit {
        val grid = inputLines.splitOnEmpty()[0].toCharGrid()

        var pos = grid.toPointMap().toList().find { it.second == '@' }!!.first
        grid[pos] = '.'

        for (move in instructions) {
            var next = pos + move
            if (grid[next] == '#') {
                continue
            } else if (grid[next] == '.') {
                pos = next
            } else {
                val toMove = mutableListOf<Point>()
                var checkPos = next
                while (grid[checkPos] == 'O') {
                    toMove.add(checkPos)
                    checkPos += move
                }
                if (grid[checkPos] == '#') continue
                for (p in toMove) {
                    grid[p + move] = 'O'
                }
                grid[next] = '.'
                pos = next
            }
//            grid.log()
//            println()
        }

        val boxes = grid.toPointMap().filter { it.value == 'O' }.keys
        boxes.sumOf { it.y * 100 + it.x }
    }

    submit {
        val grid = inputLines.splitOnEmpty()[0].map { it.map {
            when (it) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                '@' -> "@."
                else -> ""
            }
        }.joinToString("") }.toCharGrid()

        var pos = grid.toPointMap().toList().find { it.second == '@' }!!.first
        grid[pos] = '.'


        for (move in instructions) {
            var next = pos + move
            if (grid[next] == '#') {
                continue
            } else if (grid[next] == '.') {
                pos = next
            } else {
                if (move.dx != 0) {
                    val toMove = mutableListOf<Point>()
                    var checkPos = next
                    while (grid[checkPos] in "[]") {
                        toMove.add(checkPos)
                        checkPos += move
                    }
                    if (grid[checkPos] == '#') continue

                    var c = if (move.dx < 0) ']' else '['
                    for (i in 0 until toMove.size) {
                        grid[toMove[i] + move] = c
                        c = if (c == ']') '[' else ']'
                    }
                    grid[next] = '.'
                    pos = next
                } else {
                    fun canMove(p: Point): Boolean {
                        val otherSide = if (grid[p] == '[') p.copy(x = p.x+1) else p.copy(x = p.x-1)
                        return ((grid[p + move] == '.' || (grid[p + move] in "[]" && canMove(p + move))) &&
                                (grid[otherSide + move] == '.' || (grid[otherSide + move] in "[]" && canMove(otherSide + move))))
                    }

                    fun move(p: Point) {
                        val otherSide = if (grid[p] == '[') p.copy(x = p.x+1) else p.copy(x = p.x-1)

                        if (grid[p + move] in "[]") move(p + move)
                        if (grid[otherSide + move] in "[]") move(otherSide + move)

                        grid[p + move] = grid[p]
                        grid[otherSide + move] = grid[otherSide]
                        grid[p] = '.'
                        grid[otherSide] = '.'
                    }

                    if (canMove(next)) {
                        move(next)
                        pos = next
                    }
                }
            }
//            grid.log()
//            println()
        }

        val boxes = grid.toPointMap().filter { it.value == '[' }.keys
        boxes.sumOf { it.y * 100 + it.x }
    }
}