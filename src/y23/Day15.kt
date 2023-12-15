package y23

import puzzle
import sumOfIndexed

fun main() = puzzle(2023, 15) {
    val input = input.split(",")
    fun String.hash() = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    submit {
        input.sumOf { it.hash() }
    }

    submit {
        val boxes = MutableList(256) { linkedMapOf<String, Int>() }
        for (step in input) {
            if (step.contains('-')) {
                val label = step.substringBefore('-')
                boxes[label.hash()].remove(label)
            } else {
                val (label, value) = step.split('=')
                boxes[label.hash()][label] = value.toInt()
            }
        }
        boxes.sumOfIndexed { i, box -> box.toList().sumOfIndexed { j, (label, value) -> (i + 1) * (j + 1) * value } }
    }
}
