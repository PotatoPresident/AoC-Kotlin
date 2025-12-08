package y25

import DSU
import combinations
import puzzle
import sqrDistance
import toPoint3

/**
 * [AOC 2025 Day 8](https://adventofcode.com/2025/day/8)
 */
fun main() = puzzle(2025, 8) {
    val points = inputLines.map { it.toPoint3() }

    submit {
        val dsu = DSU(points)
        
        points.combinations(2).sortedBy { (a, b) -> a.sqrDistance(b) }.take(1000).forEach { (a, b) ->
            if (dsu.find(a) != dsu.find(b)) {
                dsu.union(a, b)
            }
        }

        dsu.sets().values.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

    submit {
        val dsu = DSU(points)
        var res = 0L

        points.combinations(2).sortedBy { (a, b) -> a.sqrDistance(b) }.forEach { (a, b) ->
            if (!dsu.areUnioned(a, b)) {
                dsu.union(a, b)
                res = a.x * b.x
            }
        }
        
        res
    }
}
