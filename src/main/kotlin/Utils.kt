import java.io.File

fun readInput(day: Int): List<String> = File("src/main/resources", "Day${day}Input.txt")
    .readLines()

fun readRawInput(day: Int): String = File("src/main/resources", "Day${day}Input.txt")
    .readText().replace("\r", "").trim()
