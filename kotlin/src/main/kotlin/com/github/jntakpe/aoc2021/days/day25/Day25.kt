package com.github.jntakpe.aoc2021.days.day25

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day25 : Day {

    override val input = parseInput()

    override fun part1(): Number {
        var steps = 0
        var seafloor = input
        while (true) {
            steps++
            seafloor.move(SeaCucumber.EAST).move(SeaCucumber.SOUTH).run { if (this == seafloor) return steps else seafloor = this }
        }
    }

    override fun part2() = 0

    private fun parseInput(): Seafloor {
        return readInputLines(25)
            .flatMapIndexed { y, line -> line.toCharArray().mapIndexed { x, c -> Position(x, y) to SeaCucumber.from(c) } }
            .toMap()
            .let { l -> Seafloor(l, l.keys.run { Position(count { it.y == 0 }, count { it.x == 0 }) }) }
    }

    data class Seafloor(val locations: Map<Position, SeaCucumber?>, private val dimensions: Position) {

        fun move(cucumber: SeaCucumber) = copy(locations = buildMap {
            putAll(locations)
            (0 until dimensions.y).flatMap { y -> (0 until dimensions.x).map { x -> Position(x, y) } }
                .filter { locations[it] == cucumber }
                .forEach {
                    val next = it.next(cucumber)
                    if (locations[next] == null) {
                        put(it, null)
                        put(next, cucumber)
                    }
                }
        })

        private fun Position.next(cucumber: SeaCucumber) = when (cucumber) {
            SeaCucumber.EAST -> copy(x = (x + 1) % dimensions.x)
            SeaCucumber.SOUTH -> copy(y = (y + 1) % dimensions.y)
        }
    }

    data class Position(val x: Int, val y: Int)

    enum class SeaCucumber {
        EAST,
        SOUTH;

        companion object {

            fun from(char: Char) = when (char) {
                '>' -> EAST
                'v' -> SOUTH
                '.' -> null
                else -> error("Illegal $char character")
            }
        }
    }
}
