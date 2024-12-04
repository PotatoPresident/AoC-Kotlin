package y24

import Direction
import puzzle
import toCharGrid
import toPoint

fun main() = puzzle(2024, 4) {
    val grid = inputLines.toCharGrid()

    submit {
        grid.toPointMap().entries.filter { it.value == 'X' }.sumOf { (point, char) ->
            Direction.entries.count { dir ->
                List(4) { distance -> grid.getOrNull(point + dir.toPoint() * distance) }.joinToString("") == "XMAS"
            }
        }
    }

    submit {
        grid.toPointMap().count { (point, char) ->
            val a = listOf(grid.getOrNull(point + Direction.BL_TR), char, grid.getOrNull(point + Direction.TR_BL)).joinToString("")
            val b = listOf(grid.getOrNull(point + Direction.BR_TL), char, grid.getOrNull(point + Direction.TL_BR)).joinToString("")
            listOf(a, b).all { it == "MAS" || it == "SAM"}
        }
    }
}