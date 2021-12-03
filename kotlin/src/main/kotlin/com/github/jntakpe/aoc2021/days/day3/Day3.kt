package com.github.jntakpe.aoc2021.days.day3

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day3 : Day {

    private const val ZERO = '0'
    private const val ONE = '1'
    override val input: List<String> = readInputLines(3)
    private val range = (0 until input.first().length)

    override fun part1() = range.map { input.commonBit(it) }.joinToString("").let { it.toDecimal() * it.reverseBits().toDecimal() }

    override fun part2() = input.rating(0) * input.rating(0, true)

    private fun List<String>.commonBit(index: Int): Char {
        return if (map { it[index] }.count { it == ZERO } > size / 2)
            ZERO
        else
            ONE
    }

    private fun List<String>.rating(index: Int, reverse: Boolean = false): Int {
        val char = commonBit(index).run { if (reverse) reverse() else this }
        return filter { it[index] == char }.run { singleOrNull()?.toDecimal() ?: rating(index + 1, reverse) }
    }

    private fun String.reverseBits() = map { it.reverse() }.joinToString("")

    private fun Char.reverse() = if (this == ZERO) ONE else ZERO

    private fun String.toDecimal() = Integer.parseInt(this, 2)
}
