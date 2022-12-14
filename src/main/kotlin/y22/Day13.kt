import com.beust.klaxon.*
import java.lang.StringBuilder

/**
 * [AOC 2022 Day 13](https://adventofcode.com/2022/day/13)
 */
fun main() = puzzle(2022, 13) {
    fun parse(line: String): List<*> {
        val stack = ArrayDeque<MutableList<Any>>()
        var current = mutableListOf<Any>()
        var num = -1

        fun flushNum() {
            if (num != -1) {
                current.add(num)
                num = -1
            }
        }

        line.forEach { c ->
            when (c) {
                '[' -> {
                    stack.addLast(current)
                    current = mutableListOf()
                    stack.last().add(current)
                }

                ']' -> {
                    flushNum()
                    current = stack.removeLast()
                }

                ',' -> flushNum()
                else -> num = num.coerceAtLeast(0) * 10 + (c - '0')
            }
        }

        return current[0] as List<*>
    }


    submit {
        val packets = inputLines.splitOnEmpty().map { it.map { Packet.of(it) } }

        packets.mapIndexed { i, packets ->
            if (packets[0] < packets[1]) (i + 1) else 0 
        }.sum()
    }
    
    submit {
        val decoderPackets = IntPacket(2) to IntPacket(6)
        
        val packets = inputLines.filter { it.isNotEmpty() }.map { Packet.of(it) }.toMutableList()
        packets.add(decoderPackets.first)
        packets.add(decoderPackets.second)
        packets.sort()

        (packets.indexOf(decoderPackets.first) + 1) * (packets.indexOf(decoderPackets.second) + 1)
    }
}

private sealed class Packet : Comparable<Packet> {
    companion object {
        fun of(input: String): Packet = of(input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
            .filter { it.isNotBlank() }
            .filter { it != "," }
            .iterator()
        )

        private fun of(input: Iterator<String>): Packet {
            val packets = mutableListOf<Packet>()
            while (input.hasNext()) {
                when (val symbol = input.next()) {
                    "]" -> return ListPacket(packets)
                    "[" -> packets.add(of(input))
                    else -> packets.add(IntPacket(symbol.toInt()))
                }
            }

            return ListPacket(packets)
        }
    }
}

private class IntPacket(val amount: Int) : Packet() {
    fun asList() = ListPacket(listOf(this))
    
    override fun compareTo(other: Packet): Int = when (other) {
        is IntPacket -> amount.compareTo(other.amount)
        is ListPacket -> asList().compareTo(other)
    }
}

private class ListPacket(val list: List<Packet>) : Packet() {
    override fun compareTo(other: Packet): Int = when (other) {
        is IntPacket -> compareTo(other.asList())
        is ListPacket -> list.zip(other.list).map { it.first.compareTo(it.second) }.firstOrNull { it != 0} ?: list.size.compareTo(other.list.size)
    }
}
