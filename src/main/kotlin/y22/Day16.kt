/**
 * [AOC 2022 Day 16](https://adventofcode.com/2022/day/16)
 */
fun main() = puzzle(2022, 16) {
    data class Valve(val flow: Int, val connectedValves: List<String>)

    val valves = buildMap {
        inputLines.forEach { line ->
            val label = line.substringAfter("Valve ").substringBefore(" has")
            val flow = line.substringAfter("rate=").substringBefore(";").toInt()
            val connectedValves = line.substringAfter("valve").substringAfter(" ").split(", ")
            put(label, Valve(flow, connectedValves))
        }
    }

    submit { 
        valves
    }
}
