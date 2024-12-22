data class Grid<T> private constructor(private val data: MutableList<MutableList<T>>) : Iterable<Iterable<T>> {
    val height: Int
        get() = data.size
    val width: Int
        get() = (data.map { it.size }.firstOrNull() ?: 0)

    fun getRow(row: Int) = data[row]
    fun getColumn(col: Int) = data.map { it[col] }.toMutableList()

    operator fun get(x: Int, y: Int) = data[y][x]
    operator fun get(point: Point) = data[point.y][point.x]
    fun getOrNull(point: Point) = data.getOrNull(point.y)?.getOrNull(point.x)
    operator fun set(x: Int, y: Int, value: T) {
        data[y][x] = value
    }
    operator fun set(point: Point, value: T) {
        data[point.y][point.x] = value
    }
    operator fun contains(point: Point) = point.x in 0 until width && point.y in 0 until height

    fun getNeighbors(point: Point, includeDiagonals: Boolean = true, searchDistance: Int = 1): Map<Point, T> {
        val points = getNeighboringPoints(point, includeDiagonals, searchDistance)
            .filter { it.x in 0 until width && it.y in 0 until height } // only get points in the grid
        return points.associateWith { this[it] }
    }

    fun getTaxicabNeighbors(point: Point, searchDistance: Int): Map<Point, T> {
        val points = (-searchDistance..searchDistance).flatMap { dx ->
            (-searchDistance..searchDistance).map { dy ->
                Point(point.x + dx, point.y + dy)
            }
        }
            .filter { p ->
                // Ensure we don't include the original point and only include points within Manhattan distance
                (p != point) &&
                        (p.x in 0 until width && p.y in 0 until height) &&
                        (kotlin.math.abs(p.x - point.x) + kotlin.math.abs(p.y - point.y) <= searchDistance)
            }

        return points.associateWith { this[it] }
    }

    /**
     * Returns a map where the keys are points and the values are the grid's values at their points.
     * ---
     *     12
     *     34
     * This example will create a map of `(0, 0) to 1, (0, 1) to 2, (1, 0) to 3, (1, 1) to 4`.
     */
    fun toPointMap() = data.mapIndexed { y, row ->
        row.mapIndexed { x, value -> Point(x, y) to value }
    }.flatten().toMap()

    override fun toString(): String {
        val allStrings = data.map { it.map { t -> t.toString() } }
        val longestLength = allStrings.map { it.maxByOrNull { s -> s.length }?.length ?: 0 }.maxOrNull() ?: 0
        val paddedStrings = allStrings.map { it.map { s -> s.padEnd(longestLength) } }
        return paddedStrings.joinToString("\n") { it.joinToString("") }
    }

    init {
        // remove empty rows because who knows what the input will be
        data.removeAll { it.isEmpty() }

        // ensure that the grid is rectangular
        require(data.all { it.size == data[0].size }) { "Grid must be rectangular" }
    }

    companion object {
        operator fun <E> invoke(data: List<List<E>> = listOf()) = Grid(data.map { it.toMutableList() }.toMutableList())

        inline operator fun <reified E> invoke(width: Int, height: Int, defaultValue: E) =
            invoke(Array(height) { Array(width) { defaultValue }.toMutableList() }.toMutableList())

        /**
         * Get the neighboring points of a given point
         * @param point the point you want neighboring points of
         * @param includeDiagonals should points diagonal to that point be included?
         * @return a list of neighboring points
         */
        fun getNeighboringPoints(point: Point, includeDiagonals: Boolean = true, searchDistance: Int = 1) =
            if (includeDiagonals) {
                (-searchDistance..searchDistance).flatMap { x ->
                    (-searchDistance..searchDistance).filter { x != 0 || it != 0 }.map { y ->
                        Point(point.x + x, point.y + y)
                    }
                }
            } else {
                (-searchDistance..searchDistance).flatMap { x ->
                    (-searchDistance..searchDistance).filter { x != it && x != -it }.map { y ->
                        Point(point.x + x, point.y + y)
                    }
                }
            }
    }

    override fun iterator(): Iterator<Iterable<T>> = data.iterator()
}

fun floodFill(start: Point, next: (Point) -> Iterable<Point>): Set<Point> {
    val stack = mutableListOf(start)
    val visited = mutableSetOf<Point>()

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (current in visited) continue
        visited += current
        stack += next(current)
    }

    return visited
}

fun <T> Iterable<Iterable<T>>.toGrid() = Grid(this.toList().map { it.toList() })