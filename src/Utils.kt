import java.io.File
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

data class Puzzle(val input: String) : CharSequence by input {
    val inputLines = input.lines()
    val results = mutableListOf<Pair<String, Duration>>()

    inline fun submit(action: () -> Any) {
        val result: String
        val time = measureTime {
            result = action().toString()
        }
        results.add(result to time)
    }

    override fun toString(): String = input
}

fun puzzle(year: Int, day: Int, action: Puzzle.() -> Unit) {
    val puzzle = File("src/inputs/y$year", "Day${day}.txt")
        .readText().replace("\r", "")
        .let(::Puzzle)

    val totalTime = measureTime {
        puzzle.action()
    }

    println("Puzzle: $year Day $day")
    println("Setup Time: ${totalTime - puzzle.results.sumOf { it.second.inWholeNanoseconds }.toDuration(DurationUnit.NANOSECONDS)}")
    puzzle.results.forEachIndexed { i, (s, time) ->
        println("Part ${i + 1} in $time")
    }
    println("Total Time: $totalTime")
    println()
    puzzle.results.forEachIndexed { i, (s, time) ->
        println("Part ${i + 1}: $s")
    }
}

fun String.findInts(): List<Int> = Regex("""-?\d+""").findAll(this).map { it.value.toInt() }.toList()
fun String.findLongs(): List<Long> = Regex("""-?\d+""").findAll(this).map { it.value.toLong() }.toList()
fun String.findDigits(): List<Int> = Regex("""\d""").findAll(this).map { it.value.toInt() }.toList()

fun String.toRange() = split("-").ints().let { it[0]..it[1] }

fun List<String>.ints() = map(String::toInt)
fun List<String>.csv() = map { it.split(",") }
fun List<String>.mapToChars() = map { it.toCharArray().toList() }
fun List<String>.toCharGrid() = mapToChars().toGrid()
fun Iterable<Any>.join() = joinToString("")
inline fun <T> Iterable<T>.splitOn(predicate: (T) -> Boolean): List<List<T>> {
    val d = mutableListOf<List<T>>()
    var u = mutableListOf<T>()
    for (i in this) {
        if (predicate(i)) {
            d += u
            u = mutableListOf()
        } else {
            u.add(i)
        }
    }
    d += u
    return d
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

fun <T> Iterable<T>.allDistinct() = toSet().size == count()
fun <T> Iterable<T>.productOf(f: (T) -> Int) = fold(1) { acc, t -> acc * f(t) }
fun <T> Iterable<T>.productOfLong(f: (T) -> Long) = fold(1L) { acc, t -> acc * f(t) }
fun <T> Iterable<T>.combinations(n: Int): List<List<T>> {
    if (n == 0) return listOf(emptyList())
    if (n == 1) return map { listOf(it) }
    return flatMapIndexed { i, t ->
        drop(i + 1).combinations(n - 1).map { listOf(t) + it }
    }
}

inline fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (predicate(item))
            break
    }
    return list
}

fun IntRange.contains(other: IntRange) = other.all { this.contains(it) }
fun IntRange.overlaps(other: IntRange) = other.intersect(this).isNotEmpty()

fun List<IntRange>.reduce(): List<IntRange> =
    if (this.size <= 1) this
    else {
        val sorted = this.sortedBy { it.first }
        sorted.drop(1).fold(mutableListOf(sorted.first())) { reduced, range ->
            val lastRange = reduced.last()
            if (range.first <= lastRange.last)
                reduced[reduced.lastIndex] = (lastRange.first..maxOf(lastRange.last, range.last))
            else
                reduced.add(range)
            reduced
        }
    }

fun IntRange.size() = this.last - this.first

fun <T> T?.logNullable() = println(this).let { this }
fun <T> T.log() = println(this).let { this }

fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)
fun Iterable<Long>.gcd(): Long = reduce(::gcd)

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
fun Iterable<Long>.lcm(): Long = reduce(::lcm)
