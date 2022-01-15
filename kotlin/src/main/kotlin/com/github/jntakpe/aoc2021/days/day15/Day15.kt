package com.github.jntakpe.aoc2021.days.day15

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines
import java.util.*

object Day15 : Day {

    override val input = Instructions.from(readInputLines(15))

    override fun part1() = input.shortestPath()

    override fun part2(): Int {
        val scale = input.scale(5)
        return scale.shortestPath()
    }

    class Instructions(private val positions: Map<Position, Int>, private val end: Position) {

        companion object {

            fun from(lines: List<String>): Instructions {
                val positions = lines.map { l -> l.toCharArray().map { it.digitToInt() } }
                    .flatMapIndexed { y, i -> i.mapIndexed { x, v -> (Position(x, y) to v) } }
                    .toMap()
                return Instructions(positions, positions.keys.last())
            }
        }

        fun scale(times: Int): Instructions {
            var positions = positions
            listOf<(Position, Int) -> Position>({ p, i -> p.scaleX(end, i) }, { p, i -> p.scaleY(end, i) })
                .forEach { scale ->
                    positions = (1 until times).fold(positions.toMutableMap()) { a, c ->
                        a.apply { putAll(positions.mapKeys { (p, _) -> scale(p, c) }.mapValues { (_, v) -> ((c + v - 1) % 9) + 1 }) }
                    }
                }
            return Instructions(positions, positions.keys.last())
        }

        fun shortestPath(): Int {
            val queue = PriorityQueue<Node>().apply { add(Node(Position.START, 0)) }
            val risks = positions.mapValues { (k, _) -> if (k == Position.START) 0 else Int.MAX_VALUE }.toMutableMap()
            while (queue.isNotEmpty()) {
                val node = queue.poll()
                if (node.position == end) return risks[end]!!
                if (risks[node.position]!! < node.risk) continue
                node.position.adjacent()
                    .mapNotNull { p -> positions[p]?.let { Node(p, it + node.risk) } }
                    .filter { it.risk < risks[it.position]!! }
                    .forEach {
                        risks[it.position] = it.risk;
                        queue.add(it)
                    }
            }
            error("Shortest path not found")
        }
    }

    class Node(val position: Position, val risk: Int) : Comparable<Node> {

        override fun compareTo(other: Node) = risk - other.risk
    }

    data class Position(val x: Int, val y: Int) {

        companion object {

            val START = Position(0, 0)
        }

        fun adjacent() = listOf(copy(x = x - 1), copy(x = x + 1), copy(y = y - 1), copy(y = y + 1))

        fun scaleX(size: Position, times: Int) = copy(x = x + (size.x + 1) * times)

        fun scaleY(size: Position, times: Int) = copy(y = y + (size.y + 1) * times)
    }
}
