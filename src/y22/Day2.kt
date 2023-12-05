package y22

import puzzle

/**
 * [AOC 2022 Day 2](https://adventofcode.com/2022/day/2)
 */
fun main() = puzzle(2022, 2) {
    val guide = inputLines.map { Pair(it[0], it[2]) }

    fun scoreRound(opponent: Move, player: Move): Int {
        var score = player.worth
        score += when {
            player.beats(opponent) -> 6
            opponent.beats(player) -> 0
            else -> 3 // Tie
        }
        return score
    }

    fun getStrategicMove(move: Move, outcome: Char): Move {
        return when (outcome) {
            'X' -> Move.values().find { move.beats(it) }!!
            'Y' -> move
            'Z' -> Move.values().find { it.beats(move) }!!
            else -> throw IllegalArgumentException("Invalid outcome: $outcome")
        }
    }

    submit {
        guide.sumOf { scoreRound(Move.fromChars(it.first), Move.fromChars(it.second)) }
    }

    submit {
        guide.sumOf { scoreRound(Move.fromChars(it.first), getStrategicMove(Move.fromChars(it.first), it.second)) }
    }
}

enum class Move(val worth: Int, val chars: Array<Char>) {
    ROCK(1, arrayOf('A', 'X')),
    PAPER(2, arrayOf('B', 'Y')),
    SCISSORS(3, arrayOf('C', 'Z'));

    fun beats(other: Move) = when (this) {
        ROCK -> other == SCISSORS
        PAPER -> other == ROCK
        SCISSORS -> other == PAPER
    }

    companion object {
        fun fromChars(char: Char) = values().find { it.chars.contains(char) }!!
    }
}
