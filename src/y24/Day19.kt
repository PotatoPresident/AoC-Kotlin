package y24

import com.sschr15.aoc.annotations.Memoize
import puzzle
import splitOnEmpty

fun main() = puzzle(2024, 19) {
    val designs = inputLines.splitOnEmpty()[0][0].split(", ")
    val targets = inputLines.splitOnEmpty()[1]

    submit {
        @Memoize
        fun canMake(target: String): Boolean {
            if (target.isEmpty()) return true
            return designs.filter { target.startsWith(it) }.any { canMake(target.drop(it.length)) }
        }

        targets.count { canMake(it) }
    }

    submit {
        @Memoize
        fun make(target: String): Long {
            if (target.isEmpty()) return 1L
            return designs.filter { target.startsWith(it) }.sumOf { make(target.drop(it.length)) }
        }

        targets.sumOf { make(it) }
    }
}