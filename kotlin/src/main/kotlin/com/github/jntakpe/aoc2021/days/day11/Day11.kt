package com.github.jntakpe.aoc2021.days.day11

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day11 : Day {

    override val input = readInputLines(11)
        .map { l -> l.toCharArray().map { it.digitToInt() } }
        .flatMapIndexed { y, i -> i.mapIndexed { x, v -> (Position(x, y) to v) } }
        .toMap()

    override fun part1() = with(input.toMutableMap()) { (0 until 100).fold(0) { a, _ -> a + step() } }

    override fun part2(): Int {
        with(input.toMutableMap()) {
            repeat(Int.MAX_VALUE) { step ->
                step()
                if (all { it.value == 0 }) return step + 1
            }
            error("No result found")
        }
    }

    private fun MutableMap<Position, Int>.step(): Int {
        replaceAll { _, v -> v + 1 }
        val count = asSequence().filter { (_, v) -> v == 10 }.map { (k) -> flash(k) }.sum()
        asSequence().filter { (_, v) -> v == -1 }.forEach { (k) -> this[k] = 0 }
        return count
    }

    private fun MutableMap<Position, Int>.flash(position: Position): Int {
        this[position] = -1
        return asSequence().filter { (k) -> k in position.adjacent() }
            .filter { (_, v) -> v != -1 }
            .onEach { (k, v) -> this[k] = v + 1 }
            .filter { (k, v) -> getOrDefault(k, 0) >= 10 }
            .fold(1) { a, (k) -> a + flash(k) }
    }

    data class Position(val x: Int, val y: Int) {

        fun adjacent(): List<Position> {
            return (0..2)
                .flatMap { x -> (0..2).map { x to it } }
                .map { (x, y) -> this.x + x - 1 to this.y + y - 1 }
                .filter { (x, y) -> this != Position(x, y) }
                .map { (x, y) -> Position(x, y) }
        }
    }
}
