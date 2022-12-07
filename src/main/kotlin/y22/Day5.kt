package y22

import findInts
import join
import puzzle
import splitOnEmpty
import transpose

/**
 * [AOC 2022 Day 5](https://adventofcode.com/2022/day/5)
 */
fun main() = puzzle(2022, 5) {
    val (stackInput, procedureInput) = inputLines.splitOnEmpty()
    val procedure = procedureInput.drop(1).map { it.findInts() }

    submit {
        val stacks = stackInput
            .dropLast(1)
            .reversed()
            .map { it.toList() }
            .transpose()
            .chunked(4) { it[1] }
            .map { it.filter { it != ' ' } }
            .map { ArrayDeque(it) }

        procedure.forEach { (amount, source, target) ->
            repeat(amount) {
                stacks[target - 1].addLast(stacks[source - 1].removeLast())
            }
        }

        stacks.map { it.last() }.join()
    }
    submit {
        val stacks = stackInput
            .dropLast(1)
            .reversed()
            .map { it.toList() }
            .transpose()
            .chunked(4) { it[1] }
            .map { it.filter { it != ' ' } }
            .map { ArrayDeque(it) }

        procedure.forEach { (amount, source, target) ->
            val crates = mutableListOf<Char>()
            repeat(amount) {
                crates.add(stacks[source - 1].removeLast())
            }
            crates.asReversed().forEach {
                stacks[target - 1].addLast(it)
            }
        }

        stacks.map { it.last() }.join()
    }
}
