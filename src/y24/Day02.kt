package y24

import findLongs
import puzzle

fun main() = puzzle(2024, 2) {
    val levels = inputLines.map { it.findLongs() }

    fun List<Long>.isSafe(): Boolean {
        val diff = this.windowed(2) { (a, b) -> (a - b) }
        return (diff.all { it in 1..3 } || diff.all { it in -3..-1 } )
    }

    submit {
        levels.count { it.isSafe() }
    }

    submit {
        levels.count { it.isSafe() || it.indices.any { removed -> it.filterIndexed { index, _ -> index != removed }.isSafe() } }
    }
}