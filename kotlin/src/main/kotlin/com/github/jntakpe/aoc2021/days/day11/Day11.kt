package com.github.jntakpe.aoc2021.days.day11

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day11 : Day {

    override val input = readInputLines(11)
        .map { l -> l.toCharArray().map { it.digitToInt() } }
        .flatMapIndexed { y, i -> i.mapIndexed { x, v -> (Position(x, y) to v) } }
        .toMap()

    override fun part1(): Int {
        val lights = input.toMutableMap()
        var count = 0
        repeat(100) {
            count += lights.step()
        }
        return count
    }

    override fun part2(): Int {
        val lights = input.toMutableMap()
        repeat(Int.MAX_VALUE) { step ->
            lights.step()
            if (lights.all { it.value == 0 }) return step + 1
        }
        return 0
    }

    private fun MutableMap<Position, Int>.step(): Int {
        replaceAll { _, v -> v + 1 }
        val count = input.keys.sumOf { if (this[it] == 10) flash(it) else 0 }
        input.keys.forEach { if (this[it] == -1) computeIfPresent(it) { _, _ -> 0 } }
        return count
    }

    private fun MutableMap<Position, Int>.flash(position: Position): Int {
        var flashes = 1
        computeIfPresent(position) { _, _ -> -1 }
        val adjacent = position.adjacent()
        filterKeys { it in adjacent }.forEach {
            if (getOrDefault(it.key, 0) != -1) {
                computeIfPresent(it.key) { _, v -> v + 1 }
                if (getOrDefault(it.key, 0) >= 10) {
                    flashes += flash(it.key)
                }
            }
        }
        return flashes
    }

    data class Position(val x: Int, val y: Int) {

        fun adjacent(): List<Position> {
            return (0..2)
                .flatMap { x -> (0..2).map { x to it } }
                .map { (x, y) -> this.x + x - 1 to this.y + y - 1 }
                .filter { (x, y) -> x != this.x || y != this.y }
                .map { (x, y) -> Position(x, y) }
        }
    }
}
