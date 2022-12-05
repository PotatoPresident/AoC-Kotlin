/**
 * [AOC 2022 Day 5](https://adventofcode.com/2022/day/5)
 */
object Day5 {
    val trimmedStackInput = readInput(5).takeWhile { it.contains("[") }.map { it.padEnd(it.length + 1) }

    val stacks: List<ArrayDeque<Char>> =
        List(trimmedStackInput.sortedByDescending { it.length }.first().length / 4) { ArrayDeque<Char>() }.also { crates ->
            trimmedStackInput.forEach {
                for (i in 0 until it.length / 4) {
                    val symbol = it[(i * 4) + 1]
                    if (symbol.isLetter()) {
                        crates[i].addFirst(symbol)
                    }
                }
            }
        }

    val procedure = readInput(5).dropWhile { it.startsWith("[") || it.startsWith(" ")  || it.isEmpty() }.map { it.findInts() }

    fun rearrange(stackList: List<ArrayDeque<Char>>): List<ArrayDeque<Char>> {
        procedure.forEach { (amount, source, target) ->
            for (i in 0 until amount) {
                stackList[target - 1].addLast(stackList[source - 1].removeLast())
            }
        }

        return stackList
    }

    fun rearrange9001(stackList: List<ArrayDeque<Char>>): List<ArrayDeque<Char>> {
        procedure.forEach { (amount, source, target) ->
            val crates = mutableListOf<Char>()
            for (i in 0 until amount) {
                crates.add(stackList[source - 1].removeLast())
            }
            crates.asReversed().forEach {
                stackList[target - 1].addLast(it)
            }
        }

        return stackList
    }

    fun part1() = 1//rearrange(stacks.toMutableList()).forEach { print(it.last()) }.also { println() }

    fun part2() = rearrange9001(stacks.toList()).forEach { print(it.last()) }.also { println() }
}

fun main() {
    val day = Day5
    println(day.part1())
    println(day.part2())
}
