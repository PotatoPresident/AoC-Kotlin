/**
 * [AOC 2022 Day 8](https://adventofcode.com/2022/day/8)
 */
fun main() = puzzle(2022, 8) {
    val treeHGrid: Grid<Int> = inputLines.map { it.toCharArray().map { it.digitToInt() } }.toGrid()
    submit { 
        treeHGrid.toPointMap().count { (pos, height) -> 
            if (pos.x == 0 || pos.y == 0 || pos.x == treeHGrid.width - 1 || pos.y == treeHGrid.height - 1) {
                return@count true
            }
            
            for (dir in Dir.values()) {
                val edge = when (dir) {
                    Dir.UP -> Point(pos.x, 0)
                    Dir.DOWN -> Point(pos.x, treeHGrid.height - 1)
                    Dir.LEFT -> Point(0, pos.y)
                    Dir.RIGHT -> Point(treeHGrid.width - 1, pos.y)
                }
                
                val range = pos.rangeTo(edge)
                if (range.drop(1).all { treeHGrid[it] < height }) {
                    return@count true
                }
            }
            
            false
        }
    }
    
    submit {
        treeHGrid.toPointMap().maxOf { (pos, height) ->
            Dir.values().map {
                val edge = when (it) {
                    Dir.UP -> Point(pos.x, 0)
                    Dir.DOWN -> Point(pos.x, treeHGrid.height - 1)
                    Dir.LEFT -> Point(0, pos.y)
                    Dir.RIGHT -> Point(treeHGrid.width - 1, pos.y)
                }

                val range = pos.rangeTo(edge)
                range.drop(1).takeUntil { treeHGrid[it] < height }.size
            }.fold(1) { acc, i -> acc * i }
        }
    }
}

enum class Dir {
    UP, DOWN, LEFT, RIGHT
}

inline fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}