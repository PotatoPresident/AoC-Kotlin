package y23

import Point
import puzzle
import toGrid

fun main() = puzzle(2023, 3) {
    val schem = inputLines.map { it.toCharArray().toList() }.toGrid()

    submit {
        var sum = 0
        for (y in 0..<schem.height) {
            var num = ""
            var adjacent = false
            for (x in 0..<schem.width) {
                if (schem[x, y].isDigit()) {
                    num += schem[x, y]
                    schem.getNeighbors(Point(x, y)).forEach { (_, c) ->
                        if (!c.isDigit() && c != '.') adjacent = true
                    }
                } else {
                    if (num.isNotEmpty() && adjacent) sum += num.toInt()
                    num = ""
                    adjacent = false
                }
            }
            if (num.isNotEmpty() && adjacent) sum += num.toInt()
        }

        sum
    }

    submit {
        var sum = 0
        val gears = mutableMapOf<Point, Int>()
        for (y in 0..<schem.height) {
            var num = ""
            var gear: Point? = null
            for (x in 0..<schem.width) {
                if (schem[x, y].isDigit()) {
                    num += schem[x, y]
                    schem.getNeighbors(Point(x, y)).forEach { (p, c) ->
                        if (c == '*') gear = p
                    }
                } else {
                    if (num.isNotEmpty() && gear != null) {
                        if (gears.contains(gear)) {
                            sum += gears[gear!!]!! * num.toInt()
                        } else {
                            gears[gear!!] = num.toInt()
                        }
                    }
                    num = ""
                    gear = null
                }
            }
            if (num.isNotEmpty() && gear != null) {
                if (gears.contains(gear)) {
                    sum += gears[gear!!]!! * num.toInt()
                } else {
                    gears[gear!!] = num.toInt()
                }
            }
        }

        sum
    }
}