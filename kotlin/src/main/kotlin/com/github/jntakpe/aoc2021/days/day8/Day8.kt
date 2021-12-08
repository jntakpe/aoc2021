package com.github.jntakpe.aoc2021.days.day8

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day8 : Day {

    override val input = readInputLines(8).map { Line(it) }

    override fun part1() = input.sumOf { it.countDirectSolution() }

    override fun part2() = input.sumOf { it.sum() }

    class Line(raw: String) {

        private val patterns: List<String>
        private val output: List<String>

        init {
            with(raw.split("|").map { it.trim() }.map { it.split(" ") }) {
                patterns = first()
                output = last()
            }
        }

        fun countDirectSolution() = output.count { it.length in listOf(2, 3, 4, 7) }

        fun sum() = patternMap().run { output.map { forDigit(it) }.joinToString("").toInt() }

        private fun patternMap() = buildMap<Int, String>(10) {
            patterns.groupBy { it.length }.let {
                this[1] = it[2]!!.single()
                this[7] = it[3]!!.single()
                this[4] = it[4]!!.single()
                this[8] = it[7]!!.single()
                it[6]!!.forEach { pattern ->
                    when {
                        !pattern.containsAll(this[1]!!) -> this[6] = pattern
                        pattern.containsAll(this[4]!!) -> this[9] = pattern
                        else -> this[0] = pattern
                    }
                }
                it[5]!!.forEach { pattern ->
                    when {
                        pattern.containsAll(this[1]!!) -> this[3] = pattern
                        this[9]!!.containsAll(pattern) -> this[5] = pattern
                        else -> this[2] = pattern
                    }
                }
            }
        }
    }

    private fun String.containsAll(other: String) = toList().containsAll(other.toList())

    private fun String.isSame(str: String) = length == str.length && containsAll(str)

    private fun Map<Int, String>.forDigit(digit: String) = entries.single { it.value.isSame(digit) }.key
}
