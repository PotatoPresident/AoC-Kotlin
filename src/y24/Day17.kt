package y24

import findInts
import findLongs
import puzzle
import splitOnEmpty
import kotlin.math.pow

fun main() = puzzle(2024, 17) {
    val initialReg = inputLines.splitOnEmpty()[0].map { it.findLongs().first() }.toMutableList()
    val instructions = inputLines.splitOnEmpty()[1].first().findInts()

    val a = 0
    val b = 1
    val c = 2

    fun combo(operand: Int, registers: List<Long>): Long = when (operand) {
        in 0..3 -> operand.toLong()
        4 -> registers[0]
        5 -> registers[1]
        6 -> registers[2]
        7 -> -1
        else -> error("Invalid operand: $operand")
    }

    fun evaluateProgram(registers: MutableList<Long>): MutableList<Long> {
        val results = mutableListOf<Long>()

        var i = 0
        while (i + 1 < instructions.size) {
            val opcode = instructions[i]
            val operand = instructions[i + 1]
            val combo = combo(operand, registers)
            when (opcode) {
                0 -> {
                    registers[a] = registers[a] / (2f.pow(combo.toFloat())).toInt()
                    i += 2
                }
                1 -> {
                    registers[b] = registers[b] xor operand.toLong()
                    i += 2
                }
                2 -> {
                    registers[b] = combo % 8
                    i += 2
                }
                3 -> {
                    if (registers[a] != 0L) {
                        i = operand
                    } else {
                        i += 2
                    }
                }
                4 -> {
                    registers[b] = registers[b] xor registers[c]
                    i += 2
                }
                5 -> {
                    val result = combo % 8
                    results.add(result)
                    i += 2
                }
                6 -> {
                    registers[b] = registers[a] / (2f.pow(combo.toFloat())).toInt()
                    i += 2
                }
                7 -> {
                    registers[c] = registers[a] / (2f.pow(combo.toFloat())).toInt()
                    i += 2
                }
            }
        }

        return results
    }

    submit {
        evaluateProgram(initialReg).joinToString(",")
    }

    submit {
        val target = instructions.reversed()
        fun findA(a: Long, depth: Int): Long {
            if (depth == target.size) {
                return a
            }
            for (i in 0 until 8) {
                val output = evaluateProgram(mutableListOf(a * 8 + i, 0, 0))
                if (output.isNotEmpty() && output[0] == target[depth].toLong()) {
                    val result = findA((a*8+i), depth+1)
                    if (result != 0L) {
                        return result
                    }
                }
            }
            return 0
        }
        findA(0, 0)
    }
}