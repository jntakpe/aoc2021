package com.github.jntakpe.aoc2021.days.day6

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day6 : Day {

    override val input = readInputLines(6).joinToString("").split(",").groupingBy { it.toInt() }.eachCount().mapValues { it.value.toLong() }

    override fun part1() = input.total(80)

    override fun part2() = input.total(256)

    private fun Map<Int, Long>.total(remaining: Int): Long {
        val newborn = get(0) ?: 0
        val updated = filter { it.key != 0 }.mapKeys { it.key - 1 }.toMutableMap()
        if (newborn > 0) {
            updated[8] = newborn
            updated[6] = newborn + updated.getOrDefault(6, 0)
        }
        return if (remaining == 1) updated.map { it.value }.sum() else updated.total(remaining - 1)
    }
}
