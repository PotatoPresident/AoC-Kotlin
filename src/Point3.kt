import kotlin.math.pow

data class Point3(val x: Long, val y: Long, val z: Long)

fun String.toPoint3(): Point3 = this.split(",")
    .map(String::toLong)
    .let { Point3(it[0], it[1], it[2]) }

fun Point3.sqrDistance(other: Point3): Double {
    return (x - other.x).toDouble().pow(2) +
            (y - other.y).toDouble().pow(2) +
            (z - other.z).toDouble().pow(2)

}

