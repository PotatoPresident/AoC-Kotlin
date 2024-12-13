import com.microsoft.z3.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.ExperimentalTypeInference
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
        if (predicate(item))
            break
        list.add(item)
    }
    return list
}

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOfIndexed(selector: (i: Int, T) -> Int): Int {
    var sum = 0
    for ((i, el) in this.withIndex()) sum += selector(i, el)
    return sum
}

@OptIn(ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOfIndexed(selector: (i: Int, T) -> Long): Long {
    var sum = 0L
    for ((i, el) in this.withIndex()) sum += selector(i, el)
    return sum
}

/**
 * Count the number of occurrences of each element in the iterable, and return the result as a map.
 */
fun <T> Iterable<T>.counts(): Map<T, Int> = groupingBy { it }.eachCount()

class DefaultMap<K, V> internal constructor(
    private val map: MutableMap<K, V>,
    private val defaultValue: V = USE_FUNCTION as V,
    private val defaultFunction: (K) -> V = { defaultValue },
) : MutableMap<K, V> by map {
    override operator fun get(key: K): V = map[key] ?: if (defaultValue != USE_FUNCTION) defaultValue else defaultFunction(key)
    override fun getOrDefault(key: K, defaultValue: V) = map.getOrDefault(key, defaultValue)

    companion object {
        private val USE_FUNCTION = Any()
    }
}

fun <K, V> Map<K, V>.default(default: V): DefaultMap<K, V> = DefaultMap(toMutableMap(), defaultValue = default)
fun <K, V> Map<K, V>.default(default: (K) -> V): DefaultMap<K, V> = DefaultMap(toMutableMap(), defaultFunction = default)
fun <K, V> Map<K, V>.default(default: () -> V): DefaultMap<K, V> = DefaultMap(toMutableMap()) { default() }
fun <K, V> defaultMap(default: V, vararg pairs: Pair<K, V>) = mutableMapOf(*pairs).default(default)

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

inline fun <T> Queue<T>.drain(use: (T) -> Unit) {
    while (isNotEmpty()) use(remove()!!)
}

inline fun <T> dijkstra(
    initial: T, // assuming start is cost zero
    isEnd: (T) -> Boolean,
    neighbors: (T) -> Iterable<T>,
    crossinline findCost: (T) -> Int
): DijkstraPath<T>? {
    val visited = hashSetOf(initial)
    val prev = hashMapOf<T, T>()
    val queue = PriorityQueue<Pair<T, Int>>(compareBy { (_, c) -> c }).also { it.add(initial to 0) }

    queue.drain { (current, currentCost) ->
        if (isEnd(current)) return DijkstraPath(
            end = current,
            path = generateSequence(current) { prev[it] }.toList().asReversed(),
            cost = currentCost
        )

        neighbors(current).forEach { new ->
            if (visited.add(new)) {
                prev[new] = current
                queue.offer(new to currentCost + findCost(new))
            }
        }
    }

    return null
}

data class DijkstraPath<T>(val end: T, val path: List<T>, val cost: Int)

class Z3Context(): Context() {
    operator fun <T: ArithSort> ArithExpr<T>.plus(other: ArithExpr<T>) = super.mkAdd(this, other)
    operator fun <T: ArithSort> ArithExpr<T>.minus(other: ArithExpr<T>) = super.mkSub(this, other)
    operator fun <T: ArithSort> ArithExpr<T>.times(other: ArithExpr<T>) = super.mkMul(this, other)
    operator fun <T: ArithSort> ArithExpr<T>.div(other: ArithExpr<T>) = super.mkDiv(this, other)
    infix fun <T: ArithSort> ArithExpr<T>.eq(other: ArithExpr<T>) = super.mkEq(this, other)
    operator fun Solver.plusAssign(expr: Expr<BoolSort>) = add(expr)
    fun Expr<*>.asLong() = when (this) {
        is IntNum -> int64
        is RatNum -> if (denominator.int64 == 1L) numerator.int64 else throw IllegalArgumentException("Rational number is not an integer: $this")
        else -> throw IllegalArgumentException("Cannot convert $this to a long")
    }
}

fun z3Context(action: Z3Context.() -> Any): Any {
    return action.invoke(Z3Context())
}