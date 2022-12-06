/**
 * [AOC 2022 Day 6](https://adventofcode.com/2022/day/6)
 */
fun main() = puzzle(2022, 6) {
    val datastream = input
    submit {
        datastream.windowed(4).withIndex().find { (i, s) -> 
            val chars = mutableSetOf<Char>()
            for (char in s) {
                if (chars.contains(char)) {
                    return@find false
                } else {
                    chars.add(char)
                }
            }
            println(s)
            
            true
        }!!.index + 4
    }

    submit {
        datastream.windowed(14).withIndex().find { (i, s) ->
            val chars = mutableSetOf<Char>()
            for (char in s) {
                if (chars.contains(char)) {
                    return@find false
                } else {
                    chars.add(char)
                }
            }
            println(s)

            true
        }!!.index + 14
    }
}
