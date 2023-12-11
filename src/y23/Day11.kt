package y23

import combinations
import manhattanDistance
import puzzle
import toGrid

fun main() = puzzle(2023, 11) {
    val space = inputLines.map { it.toCharArray().toMutableList() }.toMutableList()
    val emptyRows = space.withIndex().filter { it.value.all { it == '.' } }.map { it.index }.toSet()
    val emptyCols = space[0].indices.filter { col -> space.all { it[col] == '.' } }.toSet()
    val galaxies = space.toGrid().toPointMap().filter { it.value == '#' }.keys.toList()

    fun expandGalaxies(scale: Long) = galaxies.map { p ->
        (emptyCols.count { it < p.x } * (scale - 1L) + p.x) to (emptyRows.count { it < p.y } * (scale - 1L) + p.y)
        }

    submit {
        expandGalaxies(2).combinations(2).sumOf { (a, b) -> a.manhattanDistance(b) }
    }

    submit {
        expandGalaxies(1_000_000).combinations(2).sumOf { (a, b) -> a.manhattanDistance(b) }
    }
}