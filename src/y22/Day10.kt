/**
 * [AOC 2022 Day 10](https://adventofcode.com/2022/day/10)
 */
fun main() = puzzle(2022, 10) {
    val instructions = inputLines.flatMap { if (it == "noop") listOf(0) else listOf(0, it.substringAfter(" ").toInt()) }
    
    submit {
        var x = 1
        var total = 0
        instructions.forEachIndexed { i, instruct -> 
            if (i + 1 in listOf(20, 60, 100, 140, 180, 220)) {
                total += x * (i + 1)
            }
            
            x += instruct
        }
        
        total
    }
    
    submit { 
        var x = 1
        val output = mutableListOf<String>()
        instructions.chunked(40).forEach {
            var lineOutput = ""
            it.forEachIndexed { index, i ->
                lineOutput += if (index in (x - 1)..(x + 1)) "█" else " "

                x += i
            }
            output += lineOutput
        }
        
        "\n" + output.joinToString("\n")
    }
}
