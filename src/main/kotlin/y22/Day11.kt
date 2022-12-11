/**
 * [AOC 2022 Day 11](https://adventofcode.com/2022/day/11)
 */
fun main() = puzzle(2022, 11) {
    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: Int,
        val trueMonkeyId: Int,
        val falseMonkeyId: Int,
        var inspectedCount: Long = 0
    )
    
    fun getMonkeys() = buildList { 
        inputLines.splitOnEmpty().map { it.filter { it.isNotEmpty() } }.forEach { section ->
            val items = section[1].findInts().map { it.toLong() }

            val operation = when (section[2][23]) {
                '*' -> { a: Long, -> a * (if (section[2].substring(25) == "old") a else section[2].substring(25).toLong()) }
                '+' -> { a: Long -> a + (if (section[2].substring(25) == "old") a else section[2].substring(25).toLong()) }
                else -> error("Unknown operation")
            }
            val test = section[3].substringAfter("by ").toInt()
            val trueMonkeyId = section[4].substringAfter("monkey ").toInt()
            val falseMonkeyId = section[5].substringAfter("monkey ").toInt()
            add(Monkey(items.toMutableList(), operation, test, trueMonkeyId, falseMonkeyId))
        }
    }
    
    val multiple = getMonkeys().productOf { it.test }
    
    fun round(monkeys: List<Monkey>, ridiculous: Boolean = false) {
        monkeys.forEach { 
            while (it.items.isNotEmpty()) {
                val item = it.items.removeFirst()
                var new = it.operation(item)
                if (!ridiculous) new /= 3L
                new %= multiple
                if (new % it.test == 0L) {
                    monkeys[it.trueMonkeyId].items += new
                } else {
                    monkeys[it.falseMonkeyId].items += new
                }
                it.inspectedCount++
            }
        }
    }
    
    submit {
        val monkeys = getMonkeys()
        
        repeat(20) {
            round(monkeys)
        }
        
        monkeys.sortedByDescending { it.inspectedCount }.take(2).productOfLong { it.inspectedCount }
    }

    submit {
        val monkeys = getMonkeys()

        repeat(10000) {
            round(monkeys, true)
        }

        monkeys.sortedByDescending { it.inspectedCount }.take(2).productOfLong { it.inspectedCount }
    }
}
