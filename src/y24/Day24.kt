package y24

import com.sschr15.aoc.annotations.Memoize
import log
import puzzle
import splitOnEmpty
import java.io.File

fun main() = puzzle(2024, 24) {
    val initialStates = inputLines.splitOnEmpty()[0].map { it.split(": ") }.associate { it[0] to it[1].toInt() }
    data class Gate(val wireA: String, val wireB: String, val operation: String, val result: String)
    val gates = inputLines.splitOnEmpty()[1].map { it.split(' ') }.map {
        Gate(it[0], it[2], it[1], it[4])
    }

    submit {
        val states = initialStates.toMutableMap()

        @Memoize
        fun resolve(gate: Gate) {
            val a = gate.wireA.toIntOrNull() ?: states[gate.wireA] ?: run {
                resolve(gates.first { it.result == gate.wireA })
                states[gate.wireA]!!
            }
            val b = gate.wireB.toIntOrNull() ?: states[gate.wireB] ?: run {
                resolve(gates.first { it.result == gate.wireB })
                states[gate.wireB]!!
            }
            states[gate.result] = when (gate.operation) {
                "AND" -> a and b
                "OR" -> a or b
                "XOR" -> a xor b
                else -> error("Unknown operation")
            }
        }

        gates.forEach { resolve(it) }
        states.filter { it.key.startsWith('z') }.entries.sortedBy { it.key }.joinToString("") { it.value.toString() }.reversed().toLong(2)
    }

    submit {
        val states = initialStates.toMutableMap()

//        for (i in 0 until 45) {
//            val id = if (i < 10) "0$i" else i.toString()
//            states["x$id"] = 0
//        }
//        for (i in 0 until 45) {
//            val id = if (i < 10) "0$i" else i.toString()
//            states["y$id"] = 1
//        }

        @Memoize
        fun resolveGate(gate: Gate) {
            val a = states[gate.wireA] ?: run {
                resolveGate(gates.first { it.result == gate.wireA })
                states[gate.wireA]!!
            }
            val b = states[gate.wireB] ?: run {
                resolveGate(gates.first { it.result == gate.wireB })
                states[gate.wireB]!!
            }
            states[gate.result] = when (gate.operation) {
                "AND" -> a and b
                "OR" -> a or b
                "XOR" -> a xor b
                else -> error("Unknown operation")
            }
        }

        val x = states.filter { it.key.startsWith('x') }.entries.sortedByDescending { it.key }.joinToString("") { it.value.toString() }.log()
        val y = states.filter { it.key.startsWith('y') }.entries.sortedByDescending { it.key }.joinToString("") { it.value.toString() }.log()
        val result = (x.toLong(2) + y.toLong(2)).toString(2).log()

        gates.forEach { resolveGate(it) }

        fun String.toColor() = when (this) {
            "AND" -> "blue"
            "OR" -> "red"
            "XOR" -> "green"
            else -> "black"
        }

        val dot = buildString {
            appendLine("digraph G {")
            gates.forEach {
                appendLine("  ${it.wireA}_${states[it.wireA]} -> ${it.result}_${states[it.result]} [color=${it.operation.toColor()}];")
                appendLine("  ${it.wireB}_${states[it.wireB]} -> ${it.result}_${states[it.result]} [color=${it.operation.toColor()}];")
            }
            appendLine("}")
        }

        File("graph.dot").writeText(dot)
        listOf("wss", "wrm", "gbs", "z29", "thm", "z08", "z22", "hwq").sorted().joinToString(",")
    }
}
