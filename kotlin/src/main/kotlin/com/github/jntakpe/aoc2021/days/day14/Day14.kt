package com.github.jntakpe.aoc2021.days.day14

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputSplitOnBlank

object Day14 : Day {

    override val input = Instructions.from(readInputSplitOnBlank(14).map { it.lines() })

    override fun part1() = input.result(10)

    override fun part2() = input.result(40)

    class Instructions(private val frequency: Map<String, Long>, private val rules: Map<String, List<String>>, private val last: Char) {
        companion object {

            fun from(lines: List<List<String>>): Instructions {
                val firstLine = lines.first().first()
                return Instructions(firstLine.parseFrequency(), lines.last().parseRules(), firstLine.last())
            }

            private fun String.parseFrequency() = windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

            private fun List<String>.parseRules(): Map<String, List<String>> {
                return map { it.split(" -> ") }.map { it.first() to it.last() }
                    .associate { (r, c) -> r to listOf("${r.first()}$c", "$c${r.last()}") }
            }
        }

        fun result(steps: Int): Long {
            val chars = afterStep(steps).toList().groupBy({ it.first.first() }, { it.second }).mapValues { it.value.sum() }.toMutableMap()
            chars.compute(last) { _, v -> (v ?: 0) + 1 }
            return chars.maxOf { it.value } - chars.minOf { it.value }
        }

        private fun afterStep(steps: Int) = (0 until steps).fold(frequency) { a, _ -> next(a) }

        private fun next(frequency: Map<String, Long>): Map<String, Long> {
            return frequency.toList()
                .fold(mutableMapOf()) { a, c -> a.apply { rules[c.first]!!.forEach { compute(it) { _, i -> c.second + (i ?: 0) } } } }
        }
    }
}
