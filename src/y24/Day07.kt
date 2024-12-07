package y24

import findLongs
import puzzle

fun main() = puzzle(2024, 7) {
    val equations = inputLines.map { it.split(":") }.map { (target, nums) -> target.toLong() to nums.findLongs() }

    submit {
        equations.filter { (target, nums) ->
            target in nums.drop(1).fold(setOf(nums[0])) { acc, num ->
                acc.flatMap { setOf(it + num, it * num) }.filter { it <= target }.toSet()
            }
        }.sumOf { it.first }
    }

    submit {
        equations.filter { (target, nums) ->
            target in nums.drop(1).fold(setOf(nums[0])) { acc, num ->
                acc.flatMap { setOf(it + num, it * num, "$it$num".toLong()) }.filter { it <= target }.toSet()
            }
        }.sumOf { it.first }
    }
}