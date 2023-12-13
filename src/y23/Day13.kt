package y23

import Grid
import puzzle
import splitOnEmpty
import toCharGrid

fun main() = puzzle(2023, 13) {
    val patterns = inputLines.splitOnEmpty().map { it.toCharGrid() }

    submit {
        patterns.sumOf {
            val vr = (1 until it.width).firstOrNull { x -> it.diffVertical(x) == 0 } ?: 0
            val hr = (1 until it.height).firstOrNull { y -> it.diffHorizontal(y) == 0 } ?: 0
            vr + hr * 100
        }
    }


    submit {
        patterns.sumOf {
            val vr = (1 until it.width).firstOrNull { x -> it.diffVertical(x) == 1 } ?: 0
            val hr = (1 until it.height).firstOrNull { y -> it.diffHorizontal(y) == 1 } ?: 0
            vr + hr * 100
        }
    }
}

private fun Grid<Char>.diffVertical(x: Int): Int = (0 until minOf(x, width - x)).sumOf { offset ->
    (0..<height).count { y -> this[x - offset - 1, y] != this[x + offset, y] }
}

private fun Grid<Char>.diffHorizontal(y: Int): Int = (0 until minOf(y, height - y)).sumOf { offset ->
    (0..<width).count { x -> this[x, y - offset - 1] != this[x, y + offset] }
}
