package y22

import contains
import csv
import overlaps
import puzzle
import toRange

/**
 * [AOC 2022 Day 4](https://adventofcode.com/2022/day/4)
 */
fun main() = puzzle(2022, 4) {
    val sections = inputLines.csv().map {
        return@map it[0].toRange() to it[1].toRange()
    }
    
    submit { sections.filter { it.first.contains(it.second) || it.second.contains(it.first) }.size }

    submit { sections.filter { it.first.overlaps(it.second) }.size }
}
