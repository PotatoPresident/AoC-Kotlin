package y23

import findLongs
import puzzle
import splitOnEmpty

// I hate it
fun main() = puzzle(2023, 5) {
    val seeds = inputLines[0].findLongs()
    val maps = inputLines.splitOnEmpty().drop(1).map {
        it.drop(1).map { it.findLongs() }
            .map { (dest, source, length) -> (source..<source + length) to (dest..<dest + length) }.sortedBy { it.first.first }
    }

    fun mapRange(range: LongRange, map: List<Pair<LongRange, LongRange>>): List<LongRange> = buildList {
        val enclosed = map.find { it.first.contains(range) }
        if (enclosed != null) {
            val start = range.first
            val end = range.last
            val shift = enclosed.second.first - enclosed.first.first
            add((start + shift)..(end + shift))
        } else {
            val outputs = map.mapNotNull { (src, dest) ->
                val start = maxOf(src.first, range.first())
                val end = minOf(src.last, range.last)
                val shift = dest.first - src.first
                if (end < start) null else (start + shift)..(end + shift)
            }
            addAll(outputs)
        }
        if (this.isEmpty()) {
            add(range)
        }
        if (size > 1) {
            val theirMin = map.minOf { it.first.first }
            val theirMax = map.maxOf { it.first.last }
            if (range.first < theirMin) add(range.first until theirMin)
            if (range.last > theirMax) add(theirMax + 1..range.last)
        }
    }

    fun mapSeedRanges(seedRanges: List<LongRange>): Long = seedRanges.flatMap { initialRange ->
        maps.fold(listOf(initialRange)) { ranges, rangeMap ->
            ranges.flatMap { range -> mapRange(range, rangeMap) }
        }
    }.minOf { it.first }

    submit {
        mapSeedRanges(seeds.map { it..it })
    }

    submit {
        mapSeedRanges(seeds.chunked(2).map { (start, length) -> start..<start + length })
    }
}

operator fun LongRange.contains(other: LongRange) = other.all { this.contains(it) }
