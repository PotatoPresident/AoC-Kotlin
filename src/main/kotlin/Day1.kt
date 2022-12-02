class Day1 {
    private val calories = readRawInput(1)
        .split("\n\n")
        .map { it.split("\n").map { it.toInt() } }
        .map { it.sum() }

    fun part1() = calories.maxOf { it }

    fun part2() = calories.sortedDescending().take(3).sum()
}

fun main() {
    val day1 = Day1()
    println(day1.part1())
    println(day1.part2())
}
