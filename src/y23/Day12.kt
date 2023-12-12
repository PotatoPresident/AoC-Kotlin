package y23

import findInts
import log
import puzzle

fun main() = puzzle(2023, 12) {
    val rows = inputLines.map { it.split(' ') }.map { (a, b) -> a to b.findInts() }

    fun generatePermutations(input: String, cont: List<Int>, i: Int = 0, j: Int = 0, cur: Int = 0, mem: MutableMap<Triple<Int, Int, Int>, Long> = mutableMapOf()): Long {
        val key = Triple(i, j, cur)
        if (key in mem) {
            return mem[key]!!
        }

        if (i == input.length) {
            return if ((j == cont.size && cur == 0) || (j == cont.size - 1 && cont[j] == cur)) 1 else 0
        }
        val c = input[i]
        var total = 0L
        if (cur == 0 && (c == '.' || c == '?')) {
            total += generatePermutations(input, cont, i + 1, j, 0, mem) // Start of streak
        } else if (cur > 0 && j < cont.size && cont[j] == cur && (c == '.' || c == '?')) {
            total += generatePermutations(input, cont, i + 1, j + 1, 0, mem) // End of streak
        }

        if (c == '#' || c == '?') {
            total += generatePermutations(input, cont, i + 1, j, cur + 1, mem) // Continue streak
        }
        mem[key] = total
        return total
    }

    submit {
        rows.sumOf { (springs, cont) ->
            generatePermutations(springs, cont).log()
        }
    }

    submit {
        rows.sumOf { (springs, cont) ->
            var newSprings = springs
            var newCont = cont
            repeat(4) {
                newSprings += "?" + springs
                newCont += cont
            }

            generatePermutations(newSprings, newCont)
        }
    }
}
