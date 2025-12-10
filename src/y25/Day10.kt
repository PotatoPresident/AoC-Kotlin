package y25

import com.microsoft.z3.Expr
import com.microsoft.z3.IntSort
import ints
import puzzle
import z3Context

/**
 * [AOC 2025 Day 10](https://adventofcode.com/2025/day/10)
 */
fun main() = puzzle(2025, 10) {
    val machines = inputLines.map { line ->
        val target = line.drop(1).takeWhile { it != '}' }.map { it == '#' }
        val buttons = line.split(' ').drop(1).dropLast(1).map { it.drop(1).dropLast(1).split(',').ints() }
        val freq = line.dropWhile { it != '{' }.drop(1).dropLast(1).split(',').ints()

        Triple(target, buttons, freq)
    }
    
    submit {
        machines.sumOf { (target, buttons, freq) ->
            val seen = mutableSetOf<List<Boolean>>()

            val q = ArrayDeque<Pair<Long, List<Boolean>>>()
            q.add(0L to List(target.size) { false })

            while (q.isNotEmpty()) {
                val (presses, state) = q.removeFirst()
                if (seen.contains(state)) continue

                seen.add(state)

                if (state == target) {
                    return@sumOf presses
                }

                for (button in buttons) {
                    val newState = state.toMutableList()
                    for (index in button) {
                        newState[index] = !newState[index]
                    }
                    q.add((presses + 1) to newState)
                }
            }

            0L
        }
    }
    
    submit { 
        machines.sumOf { (target, buttons, freq) ->
            z3Context {
                val buttonVars = buttons.indices.map { mkIntConst("b$it") }

                mkOptimize().run {
                    buttonVars.forEach { Add(mkGe(it, mkInt(0))) }

                    freq.indices.forEach { i ->
                        Add(buttons.indices.filter { i in buttons[it] }
                            .fold(mkInt(0)) { acc: Expr<IntSort>, cur ->
                                mkAdd(
                                    acc,
                                    buttonVars[cur]
                                )
                            } eq mkInt(freq[i]))
                    }

                    val sum = buttonVars.fold(mkInt(0)) { acc: Expr<IntSort>, cur -> mkAdd(acc, cur) }
                    MkMinimize(sum)
                    Check()
                    model.eval(sum, true).asLong()
                }
            } as Long
        }
    }
}