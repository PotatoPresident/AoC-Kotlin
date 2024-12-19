package y24

import com.sschr15.aoc.annotations.Memoize
import puzzle
import splitOnEmpty

fun main() = puzzle(2024, 19) {
    val designs = inputLines.splitOnEmpty()[0][0].split(", ")
    val targets = inputLines.splitOnEmpty()[1]

    @Memoize
    fun make(target: String): Long {
        if (target.isEmpty()) return 1L
        return designs.filter { target.startsWith(it) }.sumOf { make(target.drop(it.length)) }
    }

    submit {
        targets.count { make(it) > 0 }
    }

    submit {
        targets.sumOf { make(it) }
    }
}