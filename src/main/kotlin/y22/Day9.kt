/**
 * [AOC 2022 Day 9](https://adventofcode.com/2022/day/9)
 */
fun main() = puzzle(2022, 9) {
    val motions = inputLines.map {
        when (it[0]) {
            'U' -> Point(0, 1)
            'D' -> Point(0, -1)
            'L' -> Point(-1, 0)
            'R' -> Point(1, 0)
            else -> throw IllegalArgumentException("Invalid direction: ${it[0]}")
        } to it.substringAfter(" ").toInt()
    }

    fun display(points: List<Point>) {
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        for (y in minY-3..maxY+3) {
            for (x in minX-3..maxX+3) {
                print(if (points.contains(Point(x, y))) if (points.first() == Point(x, y)) 'H' else 'T' else '.')
            }
            println()
        }
        println()
    }
    
    fun sim(n: Int): Int {
        var knots = MutableList(n) { Point.origin }
        val visited = mutableSetOf<Point>()
        
        motions.forEach { (direction, steps) ->
            repeat(steps) {
                knots[0] = knots[0] + direction
                knots.drop(1).indices.forEach { index ->
                    val head = knots[index]
                    val tail = knots[index + 1]
                    if (!head.getNeighboringPoints(true).contains(tail)) {
                        knots[index + 1] = tail + (head - tail).unit()
                    }

                    visited += knots.last()
                }
            }
        }

        return visited.size
    }
    
    submit { sim(2) }
    
    submit { sim(10) }
}
