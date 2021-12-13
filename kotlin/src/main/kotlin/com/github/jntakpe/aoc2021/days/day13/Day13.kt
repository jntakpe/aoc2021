package com.github.jntakpe.aoc2021.days.day13

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputSplitOnBlank
import kotlin.math.abs

object Day13 : Day {

    override val input = Grid.from(readInputSplitOnBlank(13).map { it.lines() })

    override fun part1() = input.foldOnce().count()

    override fun part2() = 0.apply { input.display() }

    data class Grid(val positions: Set<Position>, val folds: List<Fold>) {
        companion object {

            fun from(lines: List<List<String>>) = Grid(lines.first().positions(), lines.last().folds())

            private fun List<String>.positions() = map { it.split(',') }.map { Position(it.first().toInt(), it.last().toInt()) }.toSet()

            private fun List<String>.folds(): List<Fold> {
                return map { it.substringAfterLast(' ') }
                    .map { it.split('=') }
                    .map { Fold(Axis.valueOf(it.first().uppercase()), it.last().toInt()) }
            }
        }

        fun foldOnce() = positions.fold(folds.first())

        fun display() {
            with(foldAll()) {
                val (maxX, maxY) = (positions.maxOf { it.x } to positions.maxOf { it.y })
                (0..maxY).forEach { y ->
                    println((0..maxX).map { x -> if (positions.contains(Position(x, y))) '#' else '.' }.joinToString(""))
                }
            }
        }

        private fun foldAll() = copy(positions = folds.fold(positions) { acc, fold -> acc.fold(fold) })
    }

    data class Position(val x: Int, val y: Int) {

        fun fold(fold: Fold) = when (fold.axis) {
            Axis.X -> Position(abs(x - fold.value * 2), y)
            Axis.Y -> Position(x, abs(y - fold.value * 2))
        }
    }

    data class Fold(val axis: Axis, val value: Int)

    enum class Axis {
        X,
        Y
    }

    private fun Set<Position>.fold(fold: Fold) = when (fold.axis) {
        Axis.X -> fold(fold) { x }
        Axis.Y -> fold(fold) { y }
    }

    private fun Set<Position>.fold(fold: Fold, axis: Position.() -> Int): Set<Position> {
        return (filter { it.axis() < fold.value } + filter { it.axis() > fold.value }.map { it.fold(fold) }).toSet()
    }
}
