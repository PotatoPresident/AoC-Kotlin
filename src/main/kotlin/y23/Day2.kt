package y23

import puzzle

fun main() = puzzle(2023, 2) {
    val games = inputLines.map { s ->
        s.dropWhile { it != ':' }.drop(2).split(';', ',').map {
            val (number, color) = it.trim().split(" ")
            color to number.toInt()
        }
    }

    submit {
        var sum = 0
        games.forEachIndexed { index, game ->
            var works = true
            for ((color, number) in game) {
                when (color) {
                    "red" -> if (number > 12) works = false
                    "blue" -> if (number > 14) works = false
                    "green"-> if (number > 13) works = false
                }
            }
            if (works) sum += index + 1
        }

        sum
    }

    submit {
        games.sumOf { game ->
            var red = 0
            var blue = 0
            var green = 0
            for ((color, number) in game) {
                when (color) {
                    "red" -> red = maxOf(red, number)
                    "blue" -> blue = maxOf(blue, number)
                    "green"-> green = maxOf(green, number)
                }
            }
            red * blue * green
        }
    }
}