import java.io.File

data class Puzzle(val input: String): CharSequence by input {
    val inputLines = input.lines()
    val results = mutableListOf<String>()
    
    inline fun submit(action: () -> Any) {
        results.add(action().toString())
    }

    override fun toString(): String = input
}

fun puzzle(year: Int, day: Int, action: Puzzle.() -> Unit) {
    File("src/main/resources/y$year", "Day${day}.txt")
        .readText().replace("\r", "")
        .let(::Puzzle)
        .apply(action)
        .results
        .forEachIndexed { i, s -> 
            println("Part ${i + 1}: $s")
        }
}

fun String.findInts() : List<Int> = Regex("""\d+""").findAll(this).map { it.value.toInt() }.toList()
fun String.toRange() = split("-").ints().let { it[0]..it[1] }

fun List<String>.ints() = map(String::toInt)
fun List<String>.csv() = map { it.split(",") }
fun Iterable<Any>.join() = joinToString("")
inline fun <T> Iterable<T>.splitOn(predicate: (T) -> Boolean) = fold(listOf(listOf<T>())) { acc, t ->
    if (predicate(t)) acc + listOf(listOf(t))
    else acc.dropLast(1) + listOf(acc.last() + t)
}
fun Iterable<String>.splitOnEmpty() = splitOn { it.isEmpty() }
fun <T> Iterable<Iterable<T>>.transpose(): List<List<T>> {
    val iter = iterator()
    if (!iter.hasNext()) return emptyList()

    val ret = mutableListOf<List<T>>()
    val iters = this.map { it.iterator() }

    while (iters.all { it.hasNext() }) {
        ret.add(iters.map { it.next() })
    }

    if (iters.any { it.hasNext() }) throw IllegalArgumentException("Iterators were not the same length")

    return ret
}
fun <T> Iterable<T>.allDistinct () = toSet().size == count()

fun IntRange.contains(other: IntRange) = other.all { this.contains(it) }
fun IntRange.overlaps(other: IntRange) = other.intersect(this).isNotEmpty()
