package y24

import Point
import com.sschr15.z3kt.z3
import findInts
import puzzle
import splitOnEmpty

fun main() = puzzle(2024, 13) {
    val machines = inputLines.splitOnEmpty().map { it.map { it.findInts() }.map { Point(it[0], it[1]) } }

    fun minTokens(buttonA: Point, buttonB: Point, px: Long, py: Long): Long {
        val b = (buttonA.x * py - buttonA.y * px) / (buttonA.x * buttonB.y - buttonA.y * buttonB.x)
        val a = (py - buttonB.y * b) / buttonA.y
        val isValid = (buttonA.y * a + buttonB.y * b == py) && (buttonA.x * a + buttonB.x * b == px)
        return if (isValid) a * 3 + b else 0
    }

    submit {
        machines.sumOf { (buttonA, buttonB, prize) -> minTokens(buttonA, buttonB, prize.x.toLong(), prize.y.toLong()) }
    }

    val offset = 10000000000000L
    submit {
        machines.sumOf { (buttonA, buttonB, prize) -> minTokens(buttonA, buttonB, prize.x + offset, prize.y + offset) }
    }
}