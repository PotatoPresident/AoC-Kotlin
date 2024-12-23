package y24

import Direction
import Grid
import Point
import com.sschr15.aoc.annotations.Memoize
import puzzle
import toCharGrid
import kotlin.math.absoluteValue

fun main() = puzzle(2024, 20) {
    val codes = inputLines

    val numKeypad = listOf(
        "789",
        "456",
        "123",
        " 0A"
    ).toCharGrid()

    val dirKeypad = listOf(
        " ^A",
        "<v>"
    ) .toCharGrid()

    val dirs = mapOf(
        '^' to Direction.UP,
        'v' to Direction.DOWN,
        '<' to Direction.LEFT,
        '>' to Direction.RIGHT
    )

    fun Grid<Char>.isValid(start: Point, path: String): Boolean {
        var cur = start
        for (c in path) {
            cur += dirs[c]!!
            if (cur !in this || this[cur] == ' ') return false
        }
        return true
    }

    @Memoize
    fun shortest(from: Char, to: Char, num: Boolean, depth: Int): Long {
        val keypad = if (num) numKeypad else dirKeypad
        val positions = keypad.toPointMap().entries.associate { it.value to it.key }

        val fromPos = positions[from]!!
        val toPos = positions[to]!!
        val moves = toPos - fromPos
        val xKeys = (if (moves.x > 0) ">" else "<").repeat(moves.x.absoluteValue)
        val yKeys = (if (moves.y > 0) "v" else "^").repeat(moves.y.absoluteValue)

        fun String.pathDist(fromPos: Point, toPos: Point, keypad: Grid<Char>, depth: Int): Long {
            if (!keypad.isValid(fromPos, this)) {
                return Long.MAX_VALUE
            }
            return "A${this}A".zipWithNext().sumOf { (a, b) ->
                shortest(a, b, false, depth - 1)
            }
        }

        val minLen = if (depth == 0) {
            xKeys.length + yKeys.length + 1L
        } else {
            val xLen = "$xKeys$yKeys".pathDist(fromPos, toPos, keypad, depth)
            val yLen = "$yKeys$xKeys".pathDist(fromPos, toPos, keypad, depth)
            minOf(xLen, yLen)
        }
        return minLen
    }

    submit {
        codes.sumOf { code ->
            val num = code.substring(0..2).toInt()
            val len = "A$code".zipWithNext().sumOf { (from, to) ->
                shortest(from, to, true, 2)
            }
            num * len
        }
    }

    submit {
        codes.sumOf { code ->
            val num = code.substring(0..2).toInt()
            val len = "A$code".zipWithNext().sumOf { (from, to) ->
                shortest(from, to, true, 25)
            }
            num * len
        }
    }
}