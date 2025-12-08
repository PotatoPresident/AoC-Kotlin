package y25

import combinations
import productOf
import puzzle
import kotlin.math.pow

/**
 * [AOC 2025 Day 8](https://adventofcode.com/2025/day/8)
 */
fun main() = puzzle(2025, 8) {
    val points = inputLines.map { it.toPoint3() }

    submit {
        val dsu = DSU(points)
        
        points.combinations(2).sortedBy { (a, b) -> a.distance(b) }.take(1000).forEach { (a, b) ->
            if (dsu.find(a) != dsu.find(b)) {
                dsu.union(a, b)
            }
        }

        dsu.distinctSets().sortedByDescending { dsu.getSize(it) }.take(3).productOf { dsu.getSize(it) }
    }

    submit {
        val dsu = DSU(points)
        var res = 0L

        points.combinations(2).sortedBy { (a, b) -> a.distance(b) }.forEach { (a, b) ->
            if (dsu.find(a) != dsu.find(b)) {
                dsu.union(a, b)
                res = a.x.toLong() * b.x
            }
        }
        
        res
    }
}

fun String.toPoint3(): Point3 = this.split(",")
    .map(String::toInt)
    .let { Point3(it[0], it[1], it[2]) }

data class Point3(val x: Int, val y: Int, val z: Int)

class DSU(points: List<Point3>) {
    private val parent = points.associateWith { it }.toMutableMap()
    private val size = points.associateWith { 1 }.toMutableMap()

    fun find(p: Point3): Point3 {
        if (parent[p] != p) {
            parent[p] = find(parent[p]!!)
        }
        return parent[p]!!
    }

    fun union(p1: Point3, p2: Point3) {
        val root1 = find(p1)
        val root2 = find(p2)

        if (root1 != root2) {
            when {
                size[root1]!! < size[root2]!! -> {
                    parent[root1] = root2
                    size[root2] = size[root2]!! + size[root1]!!
                }

                size[root1]!! > size[root2]!! -> {
                    parent[root2] = root1
                    size[root1] = size[root1]!! + size[root2]!!
                }

                else -> {
                    parent[root2] = root1
                    size[root1] = size[root1]!! + size[root2]!!
                }
            }
        }
    }
    
    fun getSize(p: Point3): Int {
        return size[find(p)]!!
    }

    fun distinctSets(): Set<Point3> = parent.keys.map { find(it) }.toSet()
}

fun Point3.distance(other: Point3): Double {
    return (x - other.x).toDouble().pow(2) +
            (y - other.y).toDouble().pow(2) +
            (z - other.z).toDouble().pow(2)

}
