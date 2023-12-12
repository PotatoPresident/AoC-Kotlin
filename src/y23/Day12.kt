package y23

import findInts
import puzzle

fun main() = puzzle(2023, 12) {
    val rows = inputLines.map { it.split(' ') }.map { (a, b) -> a to b.findInts() }

    fun countPermutations(input: String, cont: List<Int>, i: Int = 0, j: Int = 0, cur: Int = 0, mem: MutableMap<Triple<Int, Int, Int>, Long> = mutableMapOf()): Long {
        val key = Triple(i, j, cur)
        if (key in mem) {
            return mem[key]!!
        }

        if (i == input.length) { // Reached end, check if valid
            return if ((j == cont.size && cur == 0) || (j == cont.size - 1 && cont[j] == cur)) 1 else 0
        }

        val c = input[i]
        var total = 0L

        if (c == '.' || c == '?') { // It's a . or it could be a .
            if (cur == 0) { // No current streak
                total += countPermutations(input, cont, i + 1, j, 0, mem) // Count permutations as if it was a .
            } else if (cur > 0 && j < cont.size && cont[j] == cur) { // Ends valid streak
                total += countPermutations(input, cont, i + 1, j + 1, 0, mem) // Count permutations looking for next streak
            }
        }

        if (c == '#' || c == '?') { // It's a # or it could be a #
            total += countPermutations(input, cont, i + 1, j, cur + 1, mem) // Continue streak
        }
        mem[key] = total
        return total
    }

    submit {
        rows.sumOf { (springs, cont) ->
            countPermutations(springs, cont)
        }
    }

    submit {
        rows.sumOf { (springs, cont) ->
            var newSprings = springs
            val newCont = cont.toMutableList()
            repeat(4) {
                newSprings += "?$springs"
                newCont += cont
            }

            countPermutations(newSprings, newCont)
        }
    }
}
