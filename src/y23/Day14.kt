package y23

import Direction
import Grid
import puzzle
import toCharGrid
import toGrid

fun main() = puzzle(2023, 14) {
    val grid = inputLines.toCharGrid()

    submit {
        grid.toGrid().roll(Direction.BTT).toPointMap().filter { it.value == 'O' }.map { grid.height - it.key.y }.sum()
    }

    submit {
        grid.rollAllDir(1_000_000_000L).toPointMap().filter { it.value == 'O' }.map { grid.height - it.key.y }.sum()
    }
}

private fun Grid<Char>.roll(direction: Direction): Grid<Char> {
    var moved: Boolean
    val grid = this.toGrid()
    do {
        moved = false
        grid.toPointMap().filter { (p, c) -> c == 'O' && grid.contains(direction.next(p)) }.forEach { (p, c) ->
            if (grid[direction.next(p)] == '.') {
                grid[direction.next(p)] = c
                grid[p] = '.'
                moved = true
            }
        }
    } while (moved)

    return grid
}

private fun Grid<Char>.rollAllDir(count: Long): Grid<Char> {
    val seen = mutableMapOf<Grid<Char>, Long>()
    var target = count
    var i = 0L
    var cur = this
    while (target > 0) {
        val new = cur.roll(Direction.BTT).roll(Direction.RTL).roll(Direction.TTB).roll(Direction.LTR)
        if (new in seen && i != seen[new]) {
            println("Found loop at $i")
            val loopLength = i - seen[new]!!
            i += target / loopLength
            target %= loopLength
        }
        seen[new] = i
        cur = new
        i++
        target--
    }
    return cur
}