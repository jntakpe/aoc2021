package com.github.jntakpe.aoc2021.days.day1

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputNumbers

object Day1 : Day {

    override val input: List<Int> = readInputNumbers(1)

    override fun part1() = input.windowed(2).count { it.first() < it.last() }

    override fun part2() = input.windowed(3).map { it.sum() }.windowed(2).count { it.first() < it.last() }
}
