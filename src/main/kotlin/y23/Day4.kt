package y23

import findInts
import puzzle

fun main() = puzzle(2023, 4) {
    val cards = inputLines.map { s ->
        val (nums, winning) = s.dropWhile { it != ':' }.drop(2).split('|').map { it.findInts() }
        nums to winning
    }

    submit {
        cards.sumOf { (nums, winning) ->
            var sum = 0
            nums.forEach { num ->
                if (winning.contains(num)) {
                    if (sum == 0) sum = 1
                    else sum *= 2
                }
            }
            sum
        }
    }

    submit {
        val multipliers = IntArray(cards.size) { 1 }
        cards.forEachIndexed { i, (nums, winning) ->
            var matches = 0
            nums.forEach {
                if (winning.contains(it)) {
                    matches++
                    multipliers[i + matches] += 1 * multipliers[i]
                }
            }
        }

        multipliers.sum()
    }
}