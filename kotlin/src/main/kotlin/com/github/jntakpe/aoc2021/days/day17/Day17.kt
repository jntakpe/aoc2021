package com.github.jntakpe.aoc2021.days.day17

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInput

object Day17 : Day {

    override val input = readInput(17)

    override fun part1() = Launcher(TargetArea.from(input)).calibrate().maxOrNull()!!

    override fun part2() = Launcher(TargetArea.from(input)).calibrate().count()

    class Launcher(private val target: TargetArea) {

        fun calibrate() = (0..target.x.last).flatMap { x -> (target.y.first..200).mapNotNull { launch(Velocity(x, it)) } }

        private fun launch(velocity: Velocity): Int? {
            var probe = Probe(velocity, Position.START, target)
            var max = 0
            while (probe.hasNext()) {
                if (probe.position.y > max) {
                    max = probe.position.y
                }
                probe = probe.next()
            }
            return max.takeIf { probe.target.contains(probe.position) }
        }
    }

    data class Probe(val velocity: Velocity, val position: Position, val target: TargetArea) : Iterator<Probe> {

        override fun hasNext() = !target.missed(position) && !target.contains(position)

        override fun next() = copy(position = position.next(velocity), velocity = velocity.next())
    }

    data class TargetArea(val x: IntRange, val y: IntRange) {

        companion object {

            fun from(raw: String) = TargetArea(range(raw, 'x'), range(raw, 'y'))

            private fun range(raw: String, axis: Char): IntRange {
                return raw.substringAfter("$axis=").substringBefore(",").split("..").map { it.toInt() }
                    .let { IntRange(it.minOrNull()!!, it.maxOrNull()!!) }
            }
        }

        fun contains(position: Position) = x.contains(position.x) && y.contains(position.y)

        fun missed(position: Position) = position.x > x.last || position.y < y.first
    }

    data class Position(val x: Int, val y: Int) {
        companion object {

            val START = Position(0, 0)
        }

        fun next(velocity: Velocity) = Position(x + velocity.x, y + velocity.y)
    }

    data class Velocity(val x: Int, val y: Int) {

        fun next() = copy(x = if (x > 0) x - 1 else 0, y = y - 1)
    }
}
