package y23

import findLongs
import productOf
import puzzle

fun main() = puzzle(2023, 6) {
    fun race(time: Long, record: Long) =
        (1..<time).count { i ->
            i*(time-i) > record
        }

    submit {
        val races = inputLines[0].findLongs().zip(inputLines[1].findLongs())
        races.productOf { (time, distance) -> race(time, distance) }
    }

    submit {
        val (time, distance) = inputLines.map { it.filter(Char::isDigit).toLong() }
        race(time, distance)
    }
}
