class Day2 {
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

    val guide = readInput(2).map { Pair(it[0], it[2]) }

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

    fun part1() = guide.sumOf { scoreRound(Move.fromChars(it.first), Move.fromChars(it.second)) }

    fun part2() = guide.sumOf { scoreRound(Move.fromChars(it.first), getStrategicMove(Move.fromChars(it.first), it.second)) }
}

fun main() {
    val day = Day2()
    println(day.part1())
    println(day.part2())
}
