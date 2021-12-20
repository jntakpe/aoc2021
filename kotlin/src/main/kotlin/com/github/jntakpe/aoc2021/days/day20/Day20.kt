package com.github.jntakpe.aoc2021.days.day20

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputSplitOnBlank

object Day20 : Day {

    override val input = ScannerImage.from(readInputSplitOnBlank(20))

    override fun part1() = enhance(2).image.filterValues { it == 1 }.size

    override fun part2() = enhance(50).image.filterValues { it == 1 }.size

    private fun enhance(times: Int): ScannerImage {
        var current = input
        repeat(times) {
            current = current.enhance()
        }
        return current
    }

    data class ScannerImage(val algorithm: List<Int>, val image: Map<Position, Int>, val default: Int) {
        companion object {

            fun from(input: List<String>) = ScannerImage(
                input.first().toList().map { if (it == '#') 1 else 0 },
                input.last().lines()
                    .flatMapIndexed { y: Int, s: String -> s.toList().mapIndexed { x, c -> Position(x, y) to if (c == '#') 1 else 0 } }
                    .toMap(),
                0
            )
        }

        fun enhance(): ScannerImage {
            val enhanced = buildMap {
                for (y in image.keys.minOf { it.y } - 1..image.keys.maxOf { it.y } + 1) {
                    for (x in image.keys.minOf { it.x } - 1..image.keys.maxOf { it.x } + 1) {
                        val position = Position(x, y)
                        val bits = buildString { position.adjacent().forEach { append(image.getOrDefault(it, default).toString()) } }
                        put(position, algorithm[bits.toInt(2)])
                    }
                }
            }
            return copy(image = enhanced, default = algorithm[default.toString().repeat(9).toInt(2)])
        }
    }

    data class Position(val x: Int, val y: Int) {

        fun adjacent(): List<Position> {
            return (0..2).flatMap { y -> (0..2).map { it to y } }.map { (x, y) -> Position(this.x + x - 1, this.y + y - 1) }
        }
    }
}
