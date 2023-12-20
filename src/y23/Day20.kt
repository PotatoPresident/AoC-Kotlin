package y23

import lcm
import puzzle

fun main() = puzzle(2023, 20) {
//    abstract class Module(val id: String, adjacent: List<Module> = emptyList()) {
//        abstract fun update(high: Boolean, sender: Module)
//        abstract fun process(adjacent: List<Module>)
//    }
//
//    class Flip(id: String) : Module(id) {
//        var on = false
//        override fun update(high: Boolean, sender: Module) {
//            if (!high) on = !on
//        }
//
//        override fun process(adjacent: List<Module>) {
//            adjacent.forEach { it.update(on, this) }
//        }
//    }
//
//    class Conj(id: String) : Module(id) {
//        val memory = mutableMapOf<String, Boolean>()
//        override fun update(high: Boolean, sender: Module) {
//            memory[sender.id] = high
//        }
//
//        override fun process(adjacent: List<Module>) {
//            val high = !memory.all { it.value }
//            adjacent.forEach { it.update(high, this) }
//        }
//    }
//
//    class Broad(id: String) : Module(id) {
//        var high = false
//        override fun update(high: Boolean, sender: Module) {
//            this.high = high
//        }
//
//        override fun process(adjacent: List<Module>) {
//            adjacent.forEach { it.update(high, this) }
//            adjacent.forEach { it.process(adjacent) }
//        }
//    }
//
//    class Dummy: Module("") {
//        override fun update(high: Boolean, sender: Module) {}
//        override fun process(adjacent: List<Module>) {}
//    }

    val modules = inputLines.associate { line ->
        val (module, next) = line.split(" -> ")
        val adjacent = next.split(", ")
        when {
            module == "broadcaster" -> {
                module to (module to adjacent)
            }

            module.contains("%") -> {
                module.drop(1) to ("%" to adjacent)
            }

            else -> {
                module.drop(1) to ("&" to adjacent)
            }
        }
    }

//    submit {
//        val queue = ArrayDeque<Triple<String, String, Boolean>>() // (module, sender, high)
//        val flipFlopState = mutableMapOf<String, Boolean>()
//        val conjState = mutableMapOf<String, MutableMap<String, Boolean>>()
//        modules.entries.filter { it.value.first == "&" }.forEach { (id, adjacent) ->
//            conjState[id] = mutableMapOf()
//            modules.entries.filter { it.value.second.contains(id) }.forEach { (sender, _) ->
//                conjState[id]!![sender] = false
//            }
//        }
//
//        val (broadcaster, bAdjacent) = modules["broadcaster"]!!
//        var highPulses = 0
//        var lowPulses = 0
//
//        repeat(1000) {
//            queue.addAll(bAdjacent.map { Triple(it, broadcaster, false) })
//            lowPulses++
//            while (queue.isNotEmpty()) {
//                val (id, sender, high) = queue.removeFirst()
//                val (type, adjacent) = modules[id] ?: (id to emptyList())
////                println("$sender -${if (high) "high" else "low"}-> $id")
//
//                if (high) highPulses++ else lowPulses++
//
//                when (type) {
//                    "broadcaster" -> {
//                        queue.addAll(adjacent.map { Triple(it, broadcaster, high) })
//                    }
//
//                    "&" -> {
//                        val state = conjState[id]!!
//                        state[sender] = high
//                        val allHigh = state.all { it.value }
//                        queue.addAll(adjacent.map { Triple(it, id, !allHigh) })
//                    }
//
//                    "%" -> {
//                        if (!high) {
//                            if (flipFlopState[id] == true) {
//                                flipFlopState[id] = false
//                                queue.addAll(adjacent.map { Triple(it, id, false) })
//                            } else {
//                                flipFlopState[id] = true
//                                queue.addAll(adjacent.map { Triple(it, id, true) })
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        highPulses * lowPulses
//    }

    submit {
        val queue = ArrayDeque<Triple<String, String, Boolean>>() // (module, sender, high)
        val flipFlopState = mutableMapOf<String, Boolean>()
        val conjState = mutableMapOf<String, MutableMap<String, Boolean>>()
        modules.entries.filter { it.value.first == "&" }.forEach { (id, adjacent) ->
            conjState[id] = mutableMapOf()
            modules.entries.filter { it.value.second.contains(id) }.forEach { (sender, _) ->
                conjState[id]!![sender] = false
            }
        }

        val (broadcaster, bAdjacent) = modules["broadcaster"]!!


        var cycle = 0L
        var nx = 0L
        var sp = 0L
        var cc = 0L
        var jq = 0L
        while (nx == 0L || sp == 0L || cc == 0L || jq == 0L) {
            cycle++

            queue.addAll(bAdjacent.map { Triple(it, broadcaster, false) })

            while (queue.isNotEmpty()) {
                if (conjState["dd"]!!["nx"] == true && nx == 0L) {
                    nx = cycle
                }
                if (conjState["dd"]!!["sp"] == true && sp == 0L) {
                    sp = cycle
                }
                if (conjState["dd"]!!["cc"] == true && cc == 0L) {
                    cc = cycle
                }
                if (conjState["dd"]!!["jq"] == true && jq == 0L) {
                    jq = cycle
                }

                val (id, sender, high) = queue.removeFirst()
                val (type, adjacent) = modules[id] ?: (id to emptyList())

                when (type) {
                    "broadcaster" -> {
                        queue.addAll(adjacent.map { Triple(it, broadcaster, high) })
                    }

                    "&" -> {
                        val state = conjState[id]!!
                        state[sender] = high
                        val allHigh = state.all { it.value }
                        queue.addAll(adjacent.map { Triple(it, id, !allHigh) })
                    }

                    "%" -> {
                        if (!high) {
                            if (flipFlopState[id] == true) {
                                flipFlopState[id] = false
                                queue.addAll(adjacent.map { Triple(it, id, false) })
                            } else {
                                flipFlopState[id] = true
                                queue.addAll(adjacent.map { Triple(it, id, true) })
                            }
                        }
                    }
                }
            }
        }

        listOf(nx, sp, cc, jq).lcm()
    }
}