package com.github.jntakpe.aoc2021.days.day2

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines
import kotlin.math.abs

object Day2 : Day {

    override val input: List<Move> = readInputLines(2).map { it.parseMove() }

    override fun part1() = input.fold(Position()) { acc, cur -> acc.apply { move(cur) } }.result()

    override fun part2() = input.fold(Position()) { acc, cur -> acc.apply { aim(cur) } }.result()

    private fun String.parseMove() = split(' ').let { Move(Direction.valueOf(it.first().uppercase()), it.last().toInt()) }

    enum class Direction {
        FORWARD,
        DOWN,
        UP
    }

    data class Move(val direction: Direction, val units: Int)

    class Position {

        private var horizontal: Int = 0
        private var depth: Int = 0
        private var aim: Int = 0

        fun move(move: Move) {
            when (move.direction) {
                Direction.FORWARD -> horizontal += move.units
                Direction.UP -> depth += move.units
                Direction.DOWN -> depth -= move.units
            }
        }

        fun aim(move: Move) {
            when (move.direction) {
                Direction.FORWARD -> {
                    horizontal += move.units
                    depth += aim * move.units
                }
                Direction.UP -> {
                    aim -= move.units
                }
                Direction.DOWN -> {
                    aim += move.units
                }
            }
        }

        fun result() = abs(horizontal * depth)
    }
}
