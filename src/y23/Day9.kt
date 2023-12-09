package y23

import findLongs
import puzzle

fun main() = puzzle(2023, 9) {
    val histories = inputLines.map { hs ->
        generateSequence(hs.findLongs()) { prevList -> prevList.windowed(2) { (a, b) -> b - a } }
            .takeWhile { it.any { num -> num != 0L } }.toList()
    }

    submit {
        histories.sumOf { history ->
            history.sumOf { it.last() }
        }
    }

    submit {
        histories.sumOf { history ->
            history.foldRight(0L) { l, acc -> l.first() - acc }
        }
    }
}