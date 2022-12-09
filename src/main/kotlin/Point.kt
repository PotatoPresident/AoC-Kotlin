import kotlin.math.abs
import kotlin.math.sign

interface AbstractPoint {
    fun x(): Int
    fun y(): Int

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
    }
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
    
    operator fun plus(point: Point): Point {
        return Point(x + point.x, y + point.y)
    }

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

    fun unit(): Point {
        return Point(x.sign, y.sign)
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
        start.x == end.x && start.y == end.y -> AbstractPoint.Direction.LTR
        start.x <  end.x && start.y == end.y -> AbstractPoint.Direction.LTR
        start.x >  end.x && start.y == end.y -> AbstractPoint.Direction.RTL
        start.x == end.x && start.y <  end.y -> AbstractPoint.Direction.TTB
        start.x == end.x                     -> AbstractPoint.Direction.BTT
        start.x <  end.x && start.y <  end.y -> AbstractPoint.Direction.BL_TR
        start.x <  end.x                     -> AbstractPoint.Direction.TL_BR
        start.y <  end.y                     -> AbstractPoint.Direction.TR_BL
        else                                 -> AbstractPoint.Direction.BR_TL
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