package com.github.jntakpe.aoc2021.days.day4

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day4 : Day {

    override val input = readInputLines(4).run { Game(first().parseNumbers(), drop(1).parseBoards()) }

    override fun part1() = input.first()

    override fun part2() = input.last()

    private fun String.parseNumbers() = split(",").map { it.toInt() }

    private fun List<String>.parseBoards() = chunked(boardSize() + 1).map { it.parseBoard() }

    private fun List<String>.boardSize() = drop(1).mapIndexedNotNull { i, s -> i.takeIf { s.trim().isBlank() } }.first()

    private fun List<String>.parseBoard() = Board(filter { it.trim().isNotBlank() }.readCells())

    private fun List<String>.readCells() = flatMapIndexed { i, l -> l.trim().parseLine(i) }

    private fun String.parseLine(index: Int): List<Cell> {
        return split(" ").filter { it.trim().isNotBlank() }.mapIndexed { i, v -> Cell(Position(i, index), v.toInt()) }
    }

    class Game(private val numbers: List<Int>, private val boards: List<Board>) {

        fun first() = numbers.firstNotNullOf { nb -> boards.firstOrNull { it.call(nb) }?.run { result(nb) } }.apply { reset() }

        fun last() = numbers.firstNotNullOf { nb -> boards.filter { it.call(nb) }.lastOrNull { boards.all { it.resolved } }?.result(nb) }

        private fun reset() = boards.forEach { it.reset() }
    }

    data class Board(val cells: List<Cell>) {

        var resolved = false

        fun call(number: Int) = if (resolved) false else cells.filter { it.value == number }.any { it.call() }

        fun result(number: Int) = cells.filter { !it.called }.sumOf { it.value } * number

        fun reset() {
            resolved = false
            cells.forEach { it.called = false }
        }

        private fun Cell.call(): Boolean {
            called = true
            if (allComplete { position.x } || allComplete { position.y }) {
                resolved = true
            }
            return resolved
        }

        private fun Cell.allComplete(extract: Cell.() -> Int) = cells.filter { it.extract() == extract() }.all { it.called }
    }

    class Cell(val position: Position, val value: Int) {

        var called: Boolean = false
    }

    data class Position(val x: Int, val y: Int)
}
