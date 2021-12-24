package com.github.jntakpe.aoc2021.days.day24

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day24 : Day {

    override val input = readInputLines(24).chunked(18).map { Variables.from(it) }

    override fun part1() = solve(Part1)

    override fun part2() = solve(Part2)

    private fun solve(part: Part): Long {
        return buildMap {
            val stack = mutableListOf<Pair<Int, Int>>()
            var count = 0
            input.forEach {
                if (it.div) stack.add(count to it.offset)
                else {
                    val (previous, offset) = stack.removeLast()
                    val diff = offset + it.check
                    this[previous] = part.previous(diff)
                    this[count] = part.count(diff)
                }
                count++
            }
        }.toSortedMap().values.joinToString("").toLong()
    }

    data class Variables(val div: Boolean, val check: Int, val offset: Int) {
        companion object {

            fun from(chunk: List<String>): Variables {
                return chunk.filterIndexed { i, _ -> i in listOf(4, 5, 15) }.map { it.substringAfterLast(" ").toInt() }
                    .let { (div, check, offset) -> Variables(div == 1, check, offset) }
            }
        }
    }

    interface Part {

        fun previous(diff: Int): Int
        fun count(diff: Int): Int
    }

    object Part1 : Part {

        override fun previous(diff: Int) = if (diff < 0) 9 else 9 - diff

        override fun count(diff: Int) = if (diff < 0) 9 + diff else 9
    }

    object Part2 : Part {

        override fun previous(diff: Int) = if (diff < 0) 1 - diff else 1

        override fun count(diff: Int) = if (diff < 0) 1 else 1 + diff
    }
}
