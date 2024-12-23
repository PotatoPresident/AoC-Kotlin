package y24

import puzzle

fun main() = puzzle(2024, 22) {
    val initialCodes = inputLines.map { it.toLong() }

    val prune = 16777216

    fun Long.nextCode(): Long {
        var num = this
        num = ((num * 64) xor num) % prune
        num = ((num / 32) xor num) % prune
        num = ((num * 2048) xor num) % prune
        return num
    }

    submit {
        initialCodes.sumOf {
            generateSequence(it) { secret -> secret.nextCode() }.take(2001).last()
        }
    }

    submit {
        initialCodes.map { code ->
            generateSequence(code) { secret -> secret.nextCode() }.take(2001)
                .map { it % 10 }
                .zipWithNext { a, b -> (b - a) to b }
                .windowed(4)
                .map { it.map { it.first } to it.last().second }
                .distinctBy { it.first }
                .toList()
        }.flatten()
            .groupBy { it.first }
            .values
            .maxOf { it.sumOf { it.second } }
    }
}