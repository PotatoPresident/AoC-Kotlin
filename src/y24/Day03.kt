package y24

import puzzle

fun main() = puzzle(2024, 3) {

    submit {
        Regex("""mul\((\d+),(\d+)\)""").findAll(input).map { match ->
            match.groupValues[1].toInt() to match.groupValues[2].toInt()
        }.sumOf { (a, b) -> a * b }
    }

    submit {
        var enabled = true
        Regex("""(don't|do)|mul\((\d+),(\d+)\)""").findAll(input).sumOf {
            when (it.value) {
                "do" -> {
                    enabled = true
                    0
                }
                "don't" -> {
                    enabled = false
                    0
                }
                else -> if (enabled) it.groupValues[2].toInt() * it.groupValues[3].toInt() else 0
            }
        }
    }
}