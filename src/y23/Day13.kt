package y23

import puzzle
import splitOnEmpty
import transpose

fun main() = puzzle(2023, 13) {
    val patterns = inputLines.splitOnEmpty()

    submit {
        patterns.sumOf { pattern ->
            val horizontalPairs = pattern.withIndex().windowed(2).filter { (a, b) -> a.value == b.value }
            var result = 0
            outer@ for ((a, b) in horizontalPairs) {
                var offset = 1
                while (a.index - offset >= 0 && b.index + offset < pattern.size) {
                    if (pattern[a.index - offset] == pattern[b.index + offset]) {
                        offset++
                    } else {
                        continue@outer
                    }
                }

                result += (a.index+1) * 100
                break@outer
            }

            val verticalPattern = pattern.map { it.toCharArray().toList() }.transpose()
            val verticalPairs = verticalPattern.withIndex().windowed(2).filter { (a, b) -> a.value == b.value }
            outer@ for ((a, b) in verticalPairs) {
                var offset = 1
                while (a.index - offset >= 0 && b.index + offset < verticalPattern.size) {
                    if (verticalPattern[a.index - offset] == verticalPattern[b.index + offset]) {
                        offset++
                    } else {
                        continue@outer
                    }
                }

                result += a.index+1
                break@outer
            }

            result
        }
    }


    submit {
        patterns.sumOf { pattern ->
            val horizontalPairs = pattern.withIndex().windowed(2).filter { (a, b) -> a.value.diff(b.value) <= 1 }
            var result = 0
            outer@ for ((a, b) in horizontalPairs) {
                var offset = 0
                var diffCount = 0
                while (a.index - offset >= 0 && b.index + offset < pattern.size) {
                    if (diffCount > 1) {
                        continue@outer
                    }
                    val diff = pattern[a.index - offset].diff(pattern[b.index + offset])
                    if (diff == 0 || diff == 1) {
                        offset++
                        diffCount += diff
                    } else {
                        continue@outer
                    }
                }
                if (diffCount == 1) {
                    println("H ${a.value} ${a.index + 1}")
                    result += (a.index + 1) * 100
                    break@outer
                }
            }

            val verticalPattern = pattern.map { it.toCharArray().toList() }.transpose().map { it.joinToString("") }
            val verticalPairs = verticalPattern.withIndex().windowed(2).filter { (a, b) -> a.value.diff(b.value) <= 1 }
            outer@ for ((a, b) in verticalPairs) {
                var offset = 0
                var diffCount = 0
                while (a.index - offset >= 0 && b.index + offset < verticalPattern.size) {
                    if (diffCount > 1) {
                        continue@outer
                    }
                    val diff = verticalPattern[a.index - offset].diff(verticalPattern[b.index + offset])
                    if (diff == 0 || diff == 1) {
                        offset++
                        diffCount += diff
                    } else {
                        continue@outer
                    }
                }
                if (diffCount == 1) {
                    println("V ${a.value} ${a.index + 1}")
                    result += (a.index + 1)
                    break@outer
                }
            }

            if (result == 0) println("AAA BAD")
            result
        }
    }
}

private fun String.diff(other: String): Int {
    return this.toCharArray().withIndex().count { (i, c) -> other.toCharArray()[i] != c }
}