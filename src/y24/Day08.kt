package y24

import Point
import puzzle
import toCharGrid

fun main() = puzzle(2024, 8) {
    val grid = inputLines.toCharGrid()
    val antennas = grid.toPointMap().filter { it.value != '.' }.toList().groupBy({it.second}, {it.first})

    fun countAntinodes(positions: (Point, Point) -> Sequence<Point>): Int =
        antennas.values.flatMap { nodes ->
            nodes.asSequence().flatMap { node -> nodes.map { node to it } }
            .filter { (nodeA, nodeB) -> nodeA != nodeB }
            .flatMap { (a, b) -> positions(a, b) }
            .filter { it in grid }
        }.toSet().size

    submit {
        countAntinodes { a, b -> sequenceOf(a - b + a, b - a + b) }
    }

    submit {
        countAntinodes { a, b ->
            val diff = a - b
            generateSequence(a) { it + diff }.takeWhile { it in grid } + generateSequence(b) { it - diff }.takeWhile { it in grid }
        }
    }
}