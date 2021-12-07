package com.github.jntakpe.aoc2021.days.day7

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines
import kotlin.math.abs

object Day7 : Day {

    override val input = readInputLines(7).joinToString("").split(",").map { it.toInt() }

    override fun part1() = resolve { from, to -> abs(from - to) }

    override fun part2() = resolve { from, to -> (0..abs(from - to)).sum() }

    private fun resolve(fuel: (Int, Int) -> Int) = (input.minOf { it }..input.maxOf { it }).minOf { d -> input.sumOf { fuel(it, d) } }
}
