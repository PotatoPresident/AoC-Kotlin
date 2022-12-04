import java.io.File

fun readInput(day: Int): List<String> = File("src/main/resources", "Day${day}Input.txt")
    .readLines()

fun readRawInput(day: Int): String = File("src/main/resources", "Day${day}Input.txt")
    .readText().replace("\r", "").trim()

fun List<String>.ints() = map(String::toInt)
fun List<String>.csv() = map { it.split(",") }

fun IntRange.contains(other: IntRange) = other.all { this.contains(it) }
fun IntRange.overlaps(other: IntRange) = other.intersect(this).isNotEmpty()
