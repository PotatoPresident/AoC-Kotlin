package y23

import puzzle

fun main() = puzzle(2023, 22) {
//    data class Brick(var x: IntRange, var y: IntRange, var z: IntRange) {
//        val volume = x.count() * y.count() * z.count()
//        val height = z.count()
//
//        fun contains(x: Int, y: Int, z: Int) = x in this.x && y in this.y && z in this.z
//        fun intersectsX(other: Brick) = x.intersect(other.x).isNotEmpty()
//        fun intersectsY(other: Brick) = y.intersect(other.y).isNotEmpty()
//        fun intersectsZ(other: Brick) = z.intersect(other.z).isNotEmpty()
//        fun intersects(other: Brick) = intersectsX(other) && intersectsY(other) && intersectsZ(other)
//    }

    val bricks = inputLines.map {
        it.split('~').map { p ->
            val (x, y, z) = p.split(',').map(String::toInt)
            Point3(x, y, z)
        }.let { it[0] to it[1] }
    }

    val grid = bricks.map { (a, b) ->
        val points = mutableSetOf<Point3>()
        for (x in a.x..b.x) {
            for (y in a.y..b.y) {
                for (z in a.z..b.z) {
                    points.add(Point3(x, y, z))
                }
            }
        }

        points
    }

    fun List<Set<Point3>>.fall(): Pair<List<Set<Point3>>, Int> {
        val new = mutableListOf<Set<Point3>>()
        val fallen = mutableSetOf<Point3>()
        var count = 0

        for (brick in this.sortedBy { b -> b.minOf { it.z } }) {
            var cur = brick
            while (true) {
                val down = cur.mapTo(mutableSetOf()) { Point3(it.x, it.y, it.z - 1) }
                if (down.any { it in fallen || it.z <= 0 }) {
                    new += cur
                    fallen += cur
                    if (cur != brick) count++
                    break
                }

                cur = down
            }
        }

        return new to count
    }

    val (fallen) = grid.fall()
    val falls = fallen.map { brick ->
        val n = fallen.minusElement(brick)
        n.fall().second
    }

    submit {
        falls.count { it == 0 }
    }

    submit {
        falls.sum()
    }

    // This is off by 2 and idk why aaaaaaaaaaaaaaa

//    fun Brick.getBelow(bricks: List<Brick>): List<Brick> {
//        return bricks.filter { it.z.last <= z.first - 1 && (it.intersectsX(this) && it.intersectsY(this)) }
//    }
//
//    fun Brick.getDirectlyBelow(bricks: List<Brick>): List<Brick> {
//        return bricks.filter { it.z.last == z.first - 1 && (it.intersectsX(this) && it.intersectsY(this)) }
//    }
//
//    fun Brick.getDirectlyAbove(bricks: List<Brick>): List<Brick> {
//        return bricks.filter { it.z.first == z.last + 1 && (it.intersectsX(this) && it.intersectsY(this)) }
//    }
//
//    fun bricksFall(bricks: List<Brick>): MutableList<Brick> {
//        val new = bricks.toMutableList()
//
//        for (brick in bricks.sortedBy { it.z.first }) {
//            val (x, y, z) = brick
//
//            if (z.first == 1) continue
//
//            val below = new.filter { it.z.last < z.first && (it.intersectsX(brick) && it.intersectsY(brick)) }
//
//            if (below.isEmpty()) {
//                new.remove(brick)
//                new.add(Brick(x, y, 1..1+brick.height))
//            } else {
//                val top = below.maxByOrNull { it.z.last }!!
//                new.remove(brick)
//                new.add(Brick(x, y, top.z.last+1..brick.height+top.z.last))
//            }
//        }
//
//        return new
//    }
//
//    submit {
//        val new = bricksFall(bricks)
//
//        val disintegrated = mutableSetOf<Brick>()
//        new.sortedBy { it.z.first }.forEach { brick ->
//            val above = brick.getDirectlyAbove(new)
//            if (above.all { it.getDirectlyBelow(new).size > 1 }) {
//                disintegrated.add(brick)
//            }
//        }
//
//        disintegrated.size
//    }
}

data class Point3(val x: Int, val y: Int, val z: Int)

private operator fun <E> List<E>.component6(): E = this[5]
