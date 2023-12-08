package y23

import lcm
import puzzle

fun main() = puzzle(2023, 8) {
    val directions = inputLines[0].toCharArray()
    val landmarks = inputLines.drop(2).associate { it.substringBefore("=").trim() to (it.substring(7,10) to it.substring(12, 15)) }

    submit {
        var pos = "AAA"
        var moves = 0

        var i = 0
        while (pos != "ZZZ") {
            if (directions[i] == 'L') {
                pos = landmarks[pos]!!.first
            } else {
                pos = landmarks[pos]!!.second
            }
            i = (i + 1) % directions.size
            moves++
        }

        moves
    }

    submit {
        val pos = landmarks.filter { it.key.endsWith("A") }.keys.toMutableList()
        val multiples = MutableList(pos.size) { 0L }
        var moves = 0L

        var i = 0
        while (pos.any { !it.endsWith("Z") }) {
            pos.forEachIndexed { index, s ->
                if (multiples[index] == 0L) {
                    if (directions[i] == 'L') {
                        pos[index] = landmarks[pos[index]]!!.first
                    } else {
                        pos[index] = landmarks[pos[index]]!!.second
                    }

                    if (pos[index].endsWith("Z")) {
                        multiples[index] = moves + 1
                    }
                }
            }
            i = (i + 1) % directions.size
            moves++
        }

        multiples.lcm()
    }
}
