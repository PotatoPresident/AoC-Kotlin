package y24

import findLongs
import puzzle

fun main() = puzzle(2024, 11) {
    val stones = input.findLongs().groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    fun blink(state: Map<Long, Long>): Map<Long, Long> {
        val new = mutableMapOf<Long, Long>()

        for ((num, count) in state) {
            if (num == 0L) {
                new[1] = new.getOrDefault(1, 0) + count
            } else if (num.toString().length % 2 == 0) {
                val digits = num.toString().map(Char::digitToInt)
                val half = digits.slice(0 until digits.size / 2).joinToString("").toLong()
                val rightHalf = digits.slice(digits.size / 2 until digits.size).joinToString("").toLong()

                new[half] = new.getOrDefault(half, 0) + count
                new[rightHalf] = new.getOrDefault(rightHalf, 0) + count
            } else {
                new[num * 2024] = new.getOrDefault(num * 2024, 0) + count
            }
        }

        return new
    }

    submit {
        var prev = stones

        repeat(25) {
            prev = blink(prev)
        }

        prev.values.sum()
    }

    submit {
        var prev = stones

        repeat(75) {
            prev = blink(prev)
        }

        prev.values.sum()
    }
}
