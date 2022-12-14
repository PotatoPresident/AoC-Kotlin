/**
 * [AOC 2022 Day 12](https://adventofcode.com/2022/day/12)
 */
fun main() = puzzle(2022, 12) {
    var start = Point(0, 0)
    var end = Point(0, 0)
    val heightmap = mutableMapOf<Point, Int>().apply {
        inputLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == 'S') start = Point(x, y)
                if (char == 'E') end = Point(x, y)
                this[Point(x, y)] = when (char) {
                    'S' -> 0
                    'E' -> 'z' - 'a'
                    else -> char - 'a'
                }
            }
        }
    }
    
    val distanceMap = buildMap { 
        var count = 0
        var currentCanidates = setOf(end)
        while (currentCanidates.isNotEmpty()) {
            currentCanidates = buildSet { 
                for (canidate in currentCanidates) {
                    if (putIfAbsent(canidate, count) != null) continue
                    val value = heightmap[canidate]!!
                    for (neighbor in canidate.neighbors()) {
                        heightmap[neighbor]?.also { 
                            if (value - it <= 1) add(neighbor)
                        }
                    }
                }
            }
            
            count++
        }
    }
    
    submit { 
        distanceMap[start]!!
    }
    
    submit { 
        distanceMap
            .filter { heightmap[it.key] == 0 }
            .values.min()
    }
}

fun Point.neighbors() = sequenceOf(
    Point(x - 1, y),
    Point(x + 1, y),
    Point(x, y - 1),
    Point(x, y + 1),
)