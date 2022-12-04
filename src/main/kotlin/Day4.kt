class Day4 {
    val sections = readInput(4).map {
        val pair = it.split(",").let { it[0] to it[1] }
        return@map pair.first.toRange() to pair.second.toRange()
    }

    fun String.toRange() = split("-").let { it[0].toInt()..it[1].toInt() }
    fun IntRange.contains(other: IntRange) = other.all { this.contains(it) }
    fun IntRange.overlaps(other: IntRange) = other.any { this.contains(it) }

    fun part1() = sections.filter { it.first.contains(it.second) || it.second.contains(it.first) }.size

    fun part2() = sections.filter { it.first.overlaps(it.second) }.size
}

fun main() {
    val day = Day4()
    println(day.part1())
    println(day.part2())
}
