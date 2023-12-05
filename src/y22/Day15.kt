import kotlin.math.absoluteValue

/**
 * [AOC 2022 Day 15](https://adventofcode.com/2022/day/15)
 */
fun main() = puzzle(2022, 15) {
    data class Sensor(val pos: Point, val closest: Point) {
        val dist = pos.manhattanDistance(closest)

        fun findRange(y: Int): IntRange? {
            val scanWidth = dist - (pos.y - y).absoluteValue
            return (pos.x - scanWidth..pos.x + scanWidth).takeIf { it.first <= it.last }
        }

        fun isInRange(other: Point): Boolean = pos.manhattanDistance(other) <= dist
    }

    val sensors = buildSet {
        inputLines.forEach { line ->
            add(
                Sensor(
                    Point(
                        line.substringAfter("x=").substringBefore(",").toInt(),
                        line.substringAfter("y=").substringBefore(":").toInt()
                    ),
                    Point(
                        line.substringAfterLast("x=").substringBeforeLast(",").toInt(),
                        line.substringAfterLast("y=").toInt()
                    )
                )
            )
        }
    }

    submit {
        sensors.mapNotNull { it.findRange(2000000) }.reduce().sumOf { it.size() }
    }

    submit {
        val cave = (0..4000000)
        sensors.firstNotNullOf {
            val up = Point(it.pos.x, it.pos.y - it.dist - 1)
            val down = Point(it.pos.x, it.pos.y + it.dist + 1)
            val left = Point(it.pos.x - it.dist - 1, it.pos.y)
            val right = Point(it.pos.x + it.dist + 1, it.pos.y)
            
            (up.lineTo(right) + right.lineTo(down) + down.lineTo(left) + left.lineTo(up))
                .filter { it.x in cave && it.y in cave }
                .firstOrNull { candidate -> sensors.none { sensor -> sensor.isInRange(candidate) } }
        }.let { (it.x * 4000000L) + it.y }
    }
}


