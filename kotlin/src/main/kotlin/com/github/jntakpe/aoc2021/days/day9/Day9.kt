package com.github.jntakpe.aoc2021.days.day9

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day9 : Day {

    override val input = readInputLines(9)
        .map { l -> l.toCharArray().map { it.digitToInt() } }
        .flatMapIndexed { y, i -> i.mapIndexed { x, v -> (Position(x, y) to v) } }
        .toMap()

    override fun part1() = input.lowPoints().values.sumOf { it + 1 }

    override fun part2(): Int {
        return input.lowPoints()
            .map { input.basin(mapOf(it.toPair())) }
            .sortedDescending()
            .take(3)
            .fold(1) { a, c -> a * c }
    }

    data class Position(val x: Int, val y: Int) {

        fun adjacent() = listOf(copy(x = x - 1), copy(x = x + 1), copy(y = y - 1), copy(y = y + 1))
    }

    private fun Map<Position, Int>.lowPoints() = filter { (k, v) -> k.adjacent().mapNotNull { input[it] }.all { it > v } }

    private fun Map<Position, Int>.basin(initial: Map<Position, Int>): Int {
        val next = initial.flatMap { nextBasinCells(it.key, it.value, initial) }.toMap()
        val all = initial + next
        if (next.isNotEmpty()) {
            return basin(all)
        }
        return all.values.count()
    }

    private fun Map<Position, Int>.nextBasinCells(position: Position, value: Int, cells: Map<Position, Int>): List<Pair<Position, Int>> {
        return position.adjacent().mapNotNull { p -> get(p)?.takeIf { !cells.containsKey(p) && it > value && it != 9 }?.let { p to it } }
    }
}
