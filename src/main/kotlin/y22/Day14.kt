/**
 * [AOC 2022 Day 14](https://adventofcode.com/2022/day/14)
 */
fun main() = puzzle(2022, 14) {
    fun getCave(): MutableSet<Point> = mutableSetOf<Point>().apply {
        inputLines.forEach { line ->
            val points = line.split(" -> ").map {
                val (x, y) = it.split(",")
                Point(x.toInt(), y.toInt())
            }
            points.windowed(2).forEach { (a, b) ->
                add(a)
                add(b)
                a.rangeTo(b).forEach { add(it) }
            }
        }
    }
    val sandSpawn = Point(500, 0)

    fun Point.down() = Point(x, y + 1)
    fun Point.downLeft() = Point(x - 1, y + 1)
    fun Point.downRight() = Point(x + 1, y + 1)

    fun display(cave: MutableSet<Point>, floor: Int) {
        for (y in 0..floor) {
            for (x in 490..510) {
                print(if (cave.contains(Point(x, y))) "#" else ".")
            }
            println()
        }
        println()
    }

    fun dropSand(cave: MutableSet<Point>, floor: Int): Boolean {
        fun Point.isTaken() = cave.contains(this)

        var sandPos = sandSpawn.copy()
        while (sandPos.y <= floor) {
            sandPos = when {
                !sandPos.down().isTaken() -> sandPos.down()
                !sandPos.downLeft().isTaken() -> sandPos.downLeft()
                !sandPos.downRight().isTaken() -> sandPos.downRight()
                sandPos.isTaken() -> return false
                else -> break
            }
        }
        cave.add(sandPos)
        return sandPos.y <= floor
    }
    
    fun dropMaxSand(cave: MutableSet<Point>, floor: Int): Int {
        var count = 0
        while (dropSand(cave, floor)) { 
            //display(cave, floor)
            count++ 
        }
        return count
    }

    submit {
        val cave = getCave()
        dropMaxSand(cave, cave.maxOf { it.y })
    }
    
    submit { 
        val cave = getCave()
        val maxY = cave.maxOf { it.y } + 2
        val minX = cave.minOf { it.x }
        val maxX = cave.maxOf { it.x }
        Point(minX - maxY, maxY).rangeTo(Point(maxX + maxY, maxY)).forEach { cave.add(it) }
        dropMaxSand(cave, cave.maxOf { it.y } + 2)
    }
}
