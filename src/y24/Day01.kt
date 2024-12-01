package y24

import findLongs
import puzzle
import transpose
import kotlin.math.absoluteValue

fun main() = puzzle(2024, 1) {
    val (left, right) = inputLines.map { it.findLongs() }.transpose().map { it.sorted() }

    submit {
        left.zip(right).sumOf { (a, b) -> (b - a).absoluteValue }
    }

    submit {
        val counts = right.groupingBy { it }.eachCount()
        left.sumOf { it * (counts[it] ?: 0) }
    }
}