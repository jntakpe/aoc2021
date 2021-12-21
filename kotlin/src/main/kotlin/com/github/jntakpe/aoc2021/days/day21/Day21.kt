package com.github.jntakpe.aoc2021.days.day21

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day21 : Day {

    override val input = readInputLines(21).map { Player.from(it) }

    override fun part1(): Int {
        var count = 0
        val dice = DeterministicDice()
        val players = input.map { it.copy() }
        while (players.none { it.score >= 1000 }) {
            players[count++ % 2].moves(dice)
        }
        return players.minOf { it.score } * dice.count
    }

    override fun part2(): Number {
        val cache = mutableMapOf<Pair<Player, Player>, Pair<Long, Long>>()
        fun play(players: Pair<Player, Player>): Pair<Long, Long> {
            cache[players]?.let { return it }
            var scores = 0L to 0L
            for (d1 in 1..3) for (d2 in 1..3) for (d3 in 1..3) {
                val next = players.first.copy().apply { moves(FixedDice(listOf(d1, d2, d3))) }
                val (x, y) = if (next.score >= 21) 0L to 1L else play(players.second.copy() to next)
                scores = scores.first + y to scores.second + x
            }
            return scores.also { cache[players] = it }
        }
        return play(input.first() to input.last()).let { maxOf(it.first, it.second) }
    }

    data class Player(var position: Int, var score: Int = 0) {

        companion object {

            fun from(line: String) = Player(line.substringAfter(": ").toInt())
        }

        fun moves(dice: Dice) {
            position = (position + dice.roll() + dice.roll() + dice.roll() - 1) % 10 + 1
            score += position
        }
    }

    sealed interface Dice {

        var count: Int

        fun roll(): Int
    }

    class DeterministicDice : Dice {

        private var state = 0
        override var count = 0

        override fun roll() = ((state++ % 100) + 1).also { count++ }
    }

    class FixedDice(private val outcomes: List<Int>) : Dice {

        override var count = 0

        override fun roll() = outcomes[count++]
    }
}
