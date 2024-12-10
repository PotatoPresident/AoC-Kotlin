package y24

import puzzle

fun main() = puzzle(2024, 9) {
    val disk = input.map(Char::digitToInt)

    submit {
        val n = disk.size
        val result = mutableListOf<Int>()
        for (i in 0 until n) {
            if (i % 2 == 0) {
                repeat(disk[i]) {
                    result.addLast(i/2)
                }
            } else {
                repeat(disk[i]) {
                    result.addLast(-1)
                }
            }
        }

        var l = result.indexOfFirst { it == -1 }
        var r = result.size - 1

        while (l < r) {
            val left = result[l]
            val right = result[r]
            result[l] = right
            result[r] = left
            l++
            r--
            while (result[l] != -1) l++
            while (result[r] == -1) r--
        }

        result.withIndex().takeWhile { (i, v) -> v != -1 }.sumOf { (i, v) ->
            i * v.toLong()
        }
    }

    submit {
        val files = mutableListOf<Triple<Int, Int, Int>>()

        var id = 0
        var idx = 0
        var free = false

        for (digit in disk) {
            if (!free) {
                files.add(Triple(idx, idx + digit, id++))
            }
            free = !free
            idx += digit
        }

        for ((start, end, idx) in files.reversed()) {
            for (j in files.indices) {
                if (files[j] == Triple(start, end, idx)) {
                    break
                }

                val (startA, endA, idxA) = files[j]
                val (startB, endB, idxB) = files[j + 1]
                val space = startB - endA
                if (space >= end - start) {
                    files.add(j + 1, Triple(endA, endA + end - start, idx))
                    files.remove(Triple(start, end, idx))
                    break
                }
            }
        }

        files.sumOf { (start, end, id) ->
            val n = end - start
            id * n * (end + start - 1L) / 2
        }
    }
}