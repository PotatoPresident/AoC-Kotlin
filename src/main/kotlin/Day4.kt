class Day4 {
    val sections = readInput(4).csv().map {
        return@map it[0].toRange() to it[1].toRange()
    }

    fun String.toRange() = split("-").ints().let { it[0]..it[1] }

    fun part1() = sections.filter { it.first.contains(it.second) || it.second.contains(it.first) }.size

    fun part2() = sections.filter { it.first.overlaps(it.second) }.size
}

fun main() {
    val day = Day4()
    println(day.part1())
    println(day.part2())
}
