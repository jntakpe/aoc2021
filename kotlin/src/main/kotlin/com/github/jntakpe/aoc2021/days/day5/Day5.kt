package com.github.jntakpe.aoc2021.days.day5

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines
import kotlin.math.abs

object Day5 : Day {

    override val input: List<Line> = readInputLines(5).map { it.split("->").parseLine() }

    override fun part1() = input.flatMap { it.straight }.groupingBy { it }.eachCount().filter { it.value >= 2 }.count()

    override fun part2() = input.flatMap { it.straight + it.diagonal }.groupingBy { it }.eachCount().filter { it.value >= 2 }.count()

    private fun List<String>.parseLine() = map { it.trim().split(",").parsePosition() }.run { Line(first(), last()) }

    private fun List<String>.parsePosition() = Position(first().toInt(), last().toInt())

    data class Line(val from: Position, val to: Position) {

        val straight = from.straightPath(to)
        val diagonal = from.diagonal(to)
    }

    data class Position(val x: Int, val y: Int) {

        fun straightPath(other: Position): List<Position> {
            return if (notEqual(other) { x } xor notEqual(other) { y }) {
                if (notEqual(other) { x }) {
                    (if (x < other.x) (x..other.x) else (x downTo other.x)).map { copy(x = it) }
                } else {
                    (if (y < other.y) (y..other.y) else (y downTo other.y)).map { copy(y = it) }
                }
            } else {
                emptyList()
            }
        }

        fun diagonal(other: Position): List<Position> {
            return if (notEqual(other) { x } && notEqual(other) { y } && abs(x - other.x) == abs(y - other.y)) {
                val xRange = if (x < other.x) (x..other.x) else (x downTo other.x)
                val yRange = if (y < other.y) (y..other.y) else (y downTo other.y)
                xRange.zip(yRange).map { copy(x = it.first, y = it.second) }
            } else {
                emptyList()
            }
        }

        private inline fun notEqual(other: Position, extract: Position.() -> Int) = extract() != other.extract()
    }
}
