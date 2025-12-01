package y25

import puzzle
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * [AOC 2025 Day 1](https://adventofcode.com/2025/day/1)
 */
fun main() = puzzle(2025, 1) {
    val turns = inputLines.map { line ->
        val dist = line.drop(1).toInt()
        if (line.first() == 'L') -dist else dist
    }
    val start = 50
    val loopSize = 100

    submit {
        turns.runningFold(start) { cur, dir ->
            (cur + dir).mod(loopSize)
        }.drop(1).count { it == 0 }
    }

    submit {
        turns.flatMap { turn -> List(turn.absoluteValue) { turn.sign } }
            .runningFold(start) { cur, dir ->
                (cur + dir).mod(loopSize)
            }.drop(1).count { it == 0 }
    }
}