package y23

import findLongs
import puzzle
import z3Context

fun main() = puzzle(2023, 24) {
    submit {
        val hailstones = inputLines.map { it.split('@') }.map { (p, v) ->
            val (x, y, z) = p.findLongs().map { it.toDouble() }
            val (dx, dy, dz) = v.findLongs().map { it.toDouble() }
            Hailstone(Vec3(x, y, z), Vec3(dx, dy, dz))
        }

        val intersections = mutableListOf<Vec3>()
        val range = 200000000000000.0..400000000000000.0
        for (i in hailstones.indices) {
            for (j in i + 1 until hailstones.size) {
                val intersection = hailstones[i].intersect(hailstones[j], range.endInclusive)
                if (intersection != null && intersection.x in range && intersection.y in range) {
                    intersections.add(intersection)
                }
            }
        }

        intersections.size
    }

    submit {
        z3Context {
            val s = mkSolver()

            val x = mkRealConst("x")
            val y = mkRealConst("y")
            val z = mkRealConst("z")
            val vx = mkRealConst("vx")
            val vy = mkRealConst("vy")
            val vz = mkRealConst("vz")

            for ((i, line) in inputLines.withIndex()) {
                val (pos, vel) = line.split('@')
                val (xI, yI, zI) = pos.findLongs().map { mkReal(it) }
                val (vxI, vyI, vzI) = vel.findLongs().map { mkReal(it) }
                val t = mkRealConst("t$i")
                s += xI + vxI * t eq x + vx * t
                s += yI + vyI * t eq y + vy * t
                s += zI + vzI * t eq z + vz * t
            }

            s.check()
            val m = s.model
            m.getConstInterp(x).asLong() + m.getConstInterp(y).asLong() + m.getConstInterp(z).asLong()
        }
    }
}

data class Hailstone(val pos: Vec3, val vec: Vec3) {
    fun isPointWithinSegment(point: Vec3, endpoint1: Vec3, endpoint2: Vec3): Boolean {
        val minX = minOf(endpoint1.x, endpoint2.x)
        val maxX = maxOf(endpoint1.x, endpoint2.x)
        val minY = minOf(endpoint1.y, endpoint2.y)
        val maxY = maxOf(endpoint1.y, endpoint2.y)

        return point.x in minX..maxX && point.y in minY..maxY
    }

    fun intersect(v2: Hailstone, limit: Double): Vec3? {
        val a1 = this.vec.y
        val b1 = -this.vec.x
        val c1 = a1 * this.pos.x + b1 * this.pos.y

        val a2 = v2.vec.y
        val b2 = -v2.vec.x
        val c2 = a2 * v2.pos.x + b2 * v2.pos.y

        val delta = a1 * b2 - a2 * b1
        return if (delta == 0.0) {
            null // The lines are parallel so no intersection
        } else {
            val x = (b2 * c1 - b1 * c2) / delta
            val y = (a1 * c2 - a2 * c1) / delta
            val intersection = Vec3(x, y, 0.0)
            val isInSegment1 = isPointWithinSegment(intersection, this.pos, this.pos + this.vec*limit)
            val isInSegment2 = isPointWithinSegment(intersection, v2.pos, v2.pos + v2.vec*limit)

            return if (isInSegment1 && isInSegment2) {
                intersection
            } else {
                null
            }
        }
    }
}

data class Vec3(val x: Double, val y: Double, val z: Double) {
    operator fun plus(vec: Vec3): Vec3 {
        return Vec3(x + vec.x, y + vec.y, z + vec.z)
    }

    operator fun times(i: Double): Vec3 {
        return Vec3(x * i, y * i, z * i)
    }
}
