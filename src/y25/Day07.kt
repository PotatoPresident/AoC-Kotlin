package y25

import Direction
import Point
import puzzle
import toCharGrid

/**
 * [AOC 2025 Day 7](https://adventofcode.com/2025/day/7)
 */
fun main() = puzzle(2025, 7) {
    val grid = inputLines.toCharGrid()
    val start = grid.toPointMap().filter { it.value == 'S' }.keys.first()

    submit { 
        var count = 0
        val hit = mutableSetOf<Point>()
        
        fun dfs(p: Point) {
            if (!grid.contains(p)) return
            if (p in hit) return
            hit.add(p)
            
            if (grid[p] == '^' ) {
                count++
                dfs(p.plus(Direction.LEFT))
                dfs(p.plus(Direction.RIGHT))
            } else {
                dfs(p.minus(Direction.UP))
            }
        }
        
        
        dfs(start)
        count
    }

    submit {
        val memo = mutableMapOf<Point, Long>()

        fun dfs(p: Point): Long {
            if (!grid.contains(p)) return 1
            if (p in memo) return memo[p]!!

            if (grid[p] == '^' ) {
                val r1 = dfs(p.plus(Direction.LEFT))
                val r2 = dfs(p.plus(Direction.RIGHT))
                memo[p] = r1 + r2
            } else {
                val res = dfs(p.minus(Direction.UP))
                memo[p] = res
            }
            
            return memo[p]!!
        }
        
        dfs(start)
    }
}
