import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sign

interface AbstractPoint {
    fun x(): Int
    fun y(): Int
}

data class Point(val x: Int, val y: Int) : Comparable<Point>, AbstractPoint {
    override fun compareTo(other: Point): Int {
        return if (y == other.y) x.compareTo(other.x) else y.compareTo(other.y)
    }

    override fun x() = x

    override fun y() = y

    override fun equals(other: Any?): Boolean = when (other) {
        is AbstractPoint -> x == other.x() && y == other.y()
        is Pair<*, *> -> x == other.first && y == other.second
        else -> false
    }

    operator fun minus(point: Point): Point {
        return Point(x - point.x, y - point.y)
    }

    operator fun minus(i: Int): Point {
        return Point(x - i, y - i)
    }

    operator fun plus(point: Point): Point {
        return Point(x + point.x, y + point.y)
    }

    operator fun plus(dir: Direction): Point {
        return Point(x + dir.dx, y + dir.dy)
    }

    operator fun times(i: Int): Point {
        return Point(x * i, y * i)
    }

    operator fun unaryMinus() = Point(-x, -y)

    operator fun rangeTo(other: Point) = PointRange(this, other)
    operator fun rangeUntil(other: Point) = ExclusivePointRange(this, other)

    override fun hashCode() = 31 * x + y

    override fun toString(): String {
        return "($x, $y)"
    }

    companion object {
        val origin = Point(0, 0)
    }

    /**
     * Get the neighboring points of a given point
     * @param point the point you want neighboring points of
     * @param includeDiagonals should points diagonal to that point be included?
     * @return a list of neighboring points
     */
    fun getNeighboringPoints(includeDiagonals: Boolean = true, searchDistance: Int = 1) =
        if (includeDiagonals) {
            (-searchDistance..searchDistance).flatMap { x ->
                (-searchDistance..searchDistance).filter { x != 0 || it != 0 }.map { y ->
                    Point(this.x + x, this.y + y)
                }
            }
        } else {
            (-searchDistance..searchDistance).flatMap { x ->
                (-searchDistance..searchDistance).filter { x != it && x != -it }.map { y ->
                    Point(this.x + x, this.y + y)
                }
            }
        }

    fun chebyshevDistance(other: Point): Int {
        return maxOf(abs(x - other.x), abs(y - other.y))
    }

    fun manhattanDistance(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun unit(): Point {
        return Point(x.sign, y.sign)
    }

    fun lineTo(other: Point): List<Point> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign
        val steps = maxOf((x - other.x).absoluteValue, (y - other.y).absoluteValue)
        return (1..steps).scan(this) { last, _ -> Point(last.x + xDelta, last.y + yDelta) }
    }
}
data class MutablePoint(var x: Int, var y: Int) : AbstractPoint {
    override fun equals(other: Any?) = when (other) {
        is AbstractPoint -> x == other.x() && y == other.y()
        is Pair<*, *> -> x == other.first && y == other.second
        else -> false
    }

    override fun hashCode() = 31 * x + y

    override fun x() = x
    override fun y() = y

    override fun toString(): String {
        return "($x, $y)"
    }
}

data class PointRange(
    override val start: Point,
    override val endInclusive: Point
) : ClosedRange<Point>, Iterable<Point> {
    override fun iterator(): Iterator<Point> = PointIterator(start, endInclusive, true)
}

data class ExclusivePointRange(
    override val start: Point,
    override val endExclusive: Point
) : OpenEndRange<Point>, Iterable<Point> {
    override fun iterator(): Iterator<Point> = PointIterator(start, endExclusive, false)
}

private class PointIterator(start: Point, private val end: Point, private val includeEnd: Boolean) : Iterator<Point> {
    val direction = when {
        start.x == end.x && start.y == end.y -> Direction.LTR
        start.x <  end.x && start.y == end.y -> Direction.LTR
        start.x >  end.x && start.y == end.y -> Direction.RTL
        start.x == end.x && start.y <  end.y -> Direction.TTB
        start.x == end.x                     -> Direction.BTT
        start.x <  end.x && start.y <  end.y -> Direction.BL_TR
        start.x <  end.x                     -> Direction.TL_BR
        start.y <  end.y                     -> Direction.TR_BL
        else                                 -> Direction.BR_TL
    }

    private var working = Point(start.x, start.y)

    override fun hasNext() = if (includeEnd) working != direction.next(end) else working != end

    override fun next(): Point {
        if (!hasNext()) throw NoSuchElementException()
        val current = working
        working = direction.next(working)
        return current
    }
}

fun Pair<Long, Long>.manhattanDistance(other: Pair<Long, Long>): Long {
    return abs(this.first - other.first) + abs(this.second - other.second)
}

enum class Direction(val dx: Int, val dy: Int) {
    LTR(1, 0),
    RTL(-1, 0),
    TTB(0, 1),
    BTT(0, -1),
    BL_TR(1, 1),
    TR_BL(-1, -1),
    TL_BR(1, -1),
    BR_TL(-1, 1);

    fun next(point: AbstractPoint) = Point(point.x() + dx, point.y() + dy)
    fun next(n: Int, point: AbstractPoint) = Point(point.x() + dx * n, point.y() + dy * n)

    companion object {
        val UP = BTT
        val RIGHT = LTR
        val DOWN = TTB
        val LEFT = RTL
    }

    val dir4: List<Direction>
        get() = listOf(UP, RIGHT, DOWN, LEFT)
}

fun Direction.toPoint() = Point(dx, dy)
operator fun Direction.times(n: Int) = Point(dx * n, dy * n)
operator fun Direction.unaryMinus() = (-toPoint()).asDirection()

fun Point.asDirection() = enumValues<Direction>().first { it.dx == x && it.dy == y }

