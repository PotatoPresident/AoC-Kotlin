package y25

import com.sschr15.aoc.annotations.Memoize
import puzzle
import splitWhitespace

/**
 * [AOC 2025 Day 11](https://adventofcode.com/2025/day/11)
 */
fun main() = puzzle(2025, 11) {
    val neighbours = mutableMapOf<String, Set<String>>()
    
    inputLines.forEach { line ->
        val source = line.substringBefore(':')
        val dests = line.substringAfter(':').splitWhitespace()
        
        neighbours[source] = dests.toSet()
    }
    
    submit {
        fun dfs(node: String): Long {
            if (node == "out") {
                return 1
            }
            
            return neighbours[node]?.sumOf { dfs(it) } ?: 0
        }
        
        dfs("you")
    }

    submit {
        @Memoize
        fun dfs(node: String, dac: Boolean, fft: Boolean): Long {
            if (node == "out" && dac && fft) {
                return 1
            }
            
            val dac = dac || node == "dac"
            val fft = fft || node == "fft"
            
            return neighbours[node]?.sumOf { dfs(it, dac, fft) } ?: 0
        }

        dfs("svr", false, false)
    }
}
