/**
 * [AOC 2023 Day 1](https://adventofcode.com/2023/day/1)
 */
fun main() = puzzle(2023, 1) {
    val nums = inputLines.map { it.findDigits() }

    submit {
        nums.map { "${it.first()}${it.last()}".toLong() }.sum()
    }

    var sum = 0L
    for (input in inputLines) {
        var first = 0L
        for (i in 0 until input.length) {
            if (input[i].isDigit()) {
                first = input[i].toString().toLong()
                break
            } else {
                if (input.substring(i, (i + 3).coerceAtMost(input.length)).contains("one")) {
                    first = 1
                    break
                } else if (input.substring(i, (i + 3).coerceAtMost(input.length)).contains("two")) {
                    first = 2
                    break
                } else if (input.substring(i, (i + 5).coerceAtMost(input.length)).contains("three")) {
                    first = 3
                    break
                } else if (input.substring(i, (i + 4).coerceAtMost(input.length)).contains("four")) {
                    first = 4
                    break
                } else if (input.substring(i, (i + 4).coerceAtMost(input.length)).contains("five")) {
                    first = 5
                    break
                } else if (input.substring(i, (i + 3).coerceAtMost(input.length)).contains("six")) {
                    first = 6
                    break
                } else if (input.substring(i, (i + 5).coerceAtMost(input.length)).contains("seven")) {
                    first = 7
                    break
                } else if (input.substring(i, (i + 5).coerceAtMost(input.length)).contains("eight")) {
                    first = 8
                    break
                } else if (input.substring(i, (i + 4).coerceAtMost(input.length)).contains("nine")) {
                    first = 9
                    break
                }
            }
        }

        var second = 0L
        for (i in input.length - 1 downTo  0) {
            if (input[i].isDigit()) {
                second = input[i].toString().toLong()
                break
            } else {
                if (input.substring((i - 2).coerceAtLeast(0), i + 1).contains("one")) {
                    second = 1
                    break
                } else if (input.substring((i - 2).coerceAtLeast(0), i + 1).contains("two")) {
                    second = 2
                    break
                } else if (input.substring((i - 4).coerceAtLeast(0), i + 1).contains("three")) {
                    second = 3
                    break
                } else if (input.substring((i - 3).coerceAtLeast(0), i + 1).contains("four")) {
                    second = 4
                    break
                } else if (input.substring((i - 3).coerceAtLeast(0), i + 1).contains("five")) {
                    second = 5
                    break
                } else if (input.substring((i - 2).coerceAtLeast(0), i + 1).contains("six")) {
                    second = 6
                    break
                } else if (input.substring((i - 4).coerceAtLeast(0), i + 1).contains("seven")) {
                    second = 7
                    break
                } else if (input.substring((i - 4).coerceAtLeast(0), i + 1).contains("eight")) {
                    second = 8
                    break
                } else if (input.substring((i - 3).coerceAtLeast(0), i + 1).contains("nine")) {
                    second = 9
                    break
                }
            }
        }

//        println(input)
//        println("${first}${second}".toLong())
        sum += "${first}${second}".toLong()
    }

    submit {
        sum
    }
}
