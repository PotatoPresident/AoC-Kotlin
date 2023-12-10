package y23

import findLongs
import log
import puzzle

fun main() = puzzle(2023, 9) {
    fun generateHistory(list: List<Long>) =
        generateSequence(list) { prevList -> prevList.log().windowed(2) { (a, b) -> b - a } }
            .takeWhile { it.any { num -> num != 0L } }.toList()

    submit {
        inputLines.map { generateHistory(it.findLongs()) }.sumOf { history ->
            history.sumOf { it.last() }
        }
    }

    submit {
        inputLines.map { generateHistory(it.findLongs().reversed()) }.sumOf { history ->
            history.sumOf { it.last() }
        }
    }
}