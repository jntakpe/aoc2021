package com.github.jntakpe.aoc2021.days.day5

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines
import kotlin.math.abs

object Day5 : Day {

    override val input = readInputLines(5).map { it.split("->").parseLine() }

    override fun part1() = input.flatMap { it.straight }.result()

    override fun part2() = input.flatMap { it.straight + it.diagonal }.result()

    private fun List<Position>.result() = groupingBy { it }.eachCount().filter { it.value >= 2 }.count()

    private fun List<String>.parseLine() = map { it.trim().split(",").parsePosition() }.run { Line(first(), last()) }

    private fun List<String>.parsePosition() = Position(first().toInt(), last().toInt())

    data class Line(val from: Position, val to: Position) {

        val straight = from straight to
        val diagonal = from diagonal to
    }

    data class Position(val x: Int, val y: Int) {

        infix fun straight(other: Position): List<Position> {
            return if (notEqual(other) { x } xor notEqual(other) { y }) {
                if (notEqual(other) { x }) {
                    progression(other) { x }.map { copy(x = it) }
                } else {
                    progression(other) { y }.map { copy(y = it) }
                }
            } else {
                emptyList()
            }
        }

        infix fun diagonal(other: Position): List<Position> {
            return if (notEqual(other) { x } && notEqual(other) { y } && abs(x - other.x) == abs(y - other.y)) {
                progression(other) { x }.zip(progression(other) { y }).map { Position(it.first, it.second) }
            } else {
                emptyList()
            }
        }

        private inline fun notEqual(other: Position, extract: Position.() -> Int) = extract() != other.extract()

        private inline fun progression(other: Position, extract: Position.() -> Int): IntProgression {
            return if (extract() < other.extract()) (extract()..other.extract()) else (extract() downTo other.extract())
        }
    }
}
