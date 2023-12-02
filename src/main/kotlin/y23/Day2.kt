package y23

import puzzle

fun main() = puzzle(2023, 2) {
    submit {
        var sum = 0
        val games = inputLines.forEachIndexed { index, s ->
            val rounds = s.dropWhile { it != ':' }.drop(2).split(';')
            var works = true
            for (round in rounds) {
                for (draw in round.split(",")) {
                    val (number, color) = draw.trim().split(" ")
                    when (color) {
                        "red" -> if (number.toInt() > 12) works = false
                        "blue" -> if (number.toInt() > 14) works = false
                        "green"-> if (number.toInt() > 13) works = false
                    }
                }
            }
            if (works) sum += index + 1
        }

        sum
    }

    submit {
        var sum = 0
        val games = inputLines.map { s ->
            val rounds = s.dropWhile { it != ':' }.drop(2).split(';')
            var red = 0
            var blue = 0
            var green = 0
            for (round in rounds) {
                for (draw in round.split(",")) {
                    val (number, color) = draw.trim().split(" ")
                    when (color) {
                        "red" -> red = maxOf(red, number.toInt())
                        "blue" -> blue = maxOf(blue, number.toInt())
                        "green"-> green = maxOf(green, number.toInt())
                    }
                }
            }
            red * blue * green
        }

        games.sum()
    }
}