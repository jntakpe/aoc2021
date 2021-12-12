package com.github.jntakpe.aoc2021.days.day12

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day12 : Day {

    override val input = readInputLines(12).flatMap { l -> l.split('-').let { Path.from(Cave(it.first()), Cave(it.last())) } }

    override fun part1() = visit(Cave.START) { cave, visited, _ -> cave.big || cave !in visited }

    override fun part2() = visit(Cave.START, emptyList(), true) { cave, visited, wildcard -> wildcard || cave.big || cave !in visited }

    private fun visit(
        cave: Cave,
        visited: List<Cave> = emptyList(),
        wildcard: Boolean = true,
        notVisited: (Cave, List<Cave>, Boolean) -> Boolean,
    ): Int {
        val allVisited = visited + cave
        if (cave == Cave.END) return 1
        return input
            .filter { p -> p.start == cave && p.end != Cave.START && notVisited(p.end, allVisited, wildcard) }
            .fold(0) { a, c -> a + visit(c.end, allVisited, if (notVisited(c.end, allVisited, false)) wildcard else false, notVisited) }
    }

    data class Cave(val name: String) {

        companion object {

            val START = Cave("start")
            val END = Cave("end")
        }

        val big = name.uppercase() == name
    }

    class Path(val start: Cave, val end: Cave) {
        companion object {

            fun from(start: Cave, end: Cave) = listOf(Path(start, end), Path(end, start))
        }
    }
}
