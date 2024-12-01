package y24

import findLongs
import puzzle
import transpose
import kotlin.math.abs

fun main() = puzzle(2024, 1) {
    val sides = inputLines.map { it.findLongs() }.transpose()

    submit {
        val left = sides[0].sorted()
        val right = sides[1].sorted()
        val n = left.size
        var sum = 0L

        for (i in 0 until n) {
            val diff = abs(left[i] - right[i])
            sum += diff
        }

        sum
    }

    submit {
        val counts = sides[1].groupingBy { it }.eachCount()

        var res = 0L
        for (num in sides[0]) {
            if (num in counts) {
                res += num * counts[num]!!
            }
        }
        res
    }
}