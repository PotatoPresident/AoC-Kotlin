class Day3 {
    val sacks = readInput(3)
    val compartments = sacks.map { it.substring(0, it.length/2) to it.substring(it.length/2) }

    fun findDuplicates(sacks: List<String>): Set<Char> {
        val duplicates = mutableSetOf<Char>()

        sacks.first().toCharArray().forEach { char ->
            if (sacks.all { it.contains(char) }) {
                duplicates.add(char)
            }
        }

        return duplicates
    }

    val Char.priority: Int
        get() = when (this) {
            in 'A'..'Z' -> this - 'A' + 27
            in 'a'..'z' -> this - 'a' + 1
            else -> throw IllegalArgumentException("Invalid char: $this")
        }

    fun part1() = compartments.map { findDuplicates(it.toList()) }.sumOf { it.sumOf { it.priority } }

    fun part2()= sacks.windowed(3, 3).sumOf { findDuplicates(it).first().priority }
}

fun main() {
    val day = Day3()
    println(day.part1())
    println(day.part2())
}
