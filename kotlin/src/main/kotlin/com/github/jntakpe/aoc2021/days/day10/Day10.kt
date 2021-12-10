package com.github.jntakpe.aoc2021.days.day10

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day10 : Day {

    override val input = readInputLines(10)

    override fun part1() = input.mapNotNull { Line.from(it).illegalPoints() }.sum()

    override fun part2() = input.mapNotNull { Line.from(it).missingPoints() }.sorted().let { it[it.size / 2] }

    class Line(private val illegal: LegalPairs? = null, private val incomplete: List<LegalPairs>? = null) {

        companion object {

            fun from(line: String): Line {
                val stack = mutableListOf<LegalPairs>()
                line.toList().forEach { char ->
                    if (char in LegalPairs.openingChars) {
                        stack.add(LegalPairs.openingValueOf(char))
                    } else {
                        LegalPairs.closingValueOf(char).run {
                            if (char in LegalPairs.closingChars && stack.last() == this) {
                                stack.removeLast()
                            } else {
                                return Line(this)
                            }
                        }
                    }
                }
                return Line(incomplete = stack.reversed())
            }
        }

        fun illegalPoints() = illegal?.illegalPoints

        fun missingPoints() = incomplete?.fold(0L) { a, c -> a * 5 + c.missingPoints }
    }

    enum class LegalPairs(val opening: Char, val closing: Char, val illegalPoints: Int, val missingPoints: Int) {
        PARENTHESIS('(', ')', 3, 1),
        CURLY_BRACKETS('{', '}', 57, 3),
        BRACKETS('[', ']', 1197, 2),
        ANGLE_BRACKETS('<', '>', 25137, 4);

        companion object {

            val openingChars = values().map { it.opening }
            val closingChars = values().map { it.closing }

            fun openingValueOf(char: Char) = values().single { it.opening == char }

            fun closingValueOf(char: Char) = values().single { it.closing == char }
        }
    }
}
