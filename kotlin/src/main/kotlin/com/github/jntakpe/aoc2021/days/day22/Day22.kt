package com.github.jntakpe.aoc2021.days.day22

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day22 : Day {

    override val input = readInputLines(22).map { Step.from(it) }

    override fun part1(): Number {
        return input
            .filter { it.cuboid.inRange(-50..50) }
            .fold(emptySet<Triple<Int, Int, Int>>()) { a, c ->
                when (c.state) {
                    State.OFF -> a.minus(c.cuboid.combinations())
                    State.ON -> a.plus(c.cuboid.combinations())
                }
            }.count()
    }

    override fun part2(): Number {
        val cubes = mutableListOf<Step>()
        input.forEach {
            cubes.addAll(buildList { cubes.forEach { step -> it.cuboid.overlap(step.cuboid)?.apply { add(Step(!step.state, this)) } } })
            if (it.state == State.ON) cubes.add(it)
        }
        return cubes.sumOf { it.state.sign * it.cuboid.size() }
    }

    enum class State(val sign: Int) {
        ON(1),
        OFF(-1);

        operator fun not() = when (this) {
            ON -> OFF
            OFF -> ON
        }
    }

    data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {

        fun combinations() = buildSet { for (x in x) for (y in y) for (z in z) add(Triple(x, y, z)) }

        fun inRange(range: IntRange) = listOf(x, y, z).all { range.contains(it.first) && range.contains(it.last) }

        fun size() = x.size() * y.size() * z.size()

        fun overlap(other: Cuboid): Cuboid? {
            return listOf<Cuboid.() -> IntRange>({ x }, { y }, { z })
                .map { it(this).overlap(it(other)) }
                .takeIf { r -> r.all { it.first <= it.second } }
                ?.map { it.first..it.second }
                ?.let { (x, y, z) -> Cuboid(x, y, z) }
        }

        private fun IntRange.overlap(other: IntRange) = maxOf(first, other.first) to minOf(last, other.last)

        private fun IntRange.size(): Long = (last - first + 1).toLong()
    }

    data class Step(val state: State, val cuboid: Cuboid) {
        companion object {

            fun from(line: String): Step {
                val state = State.valueOf(line.substringBefore(" ").uppercase())
                val (x, y, z) = line.substringAfter(" ").split(",")
                    .map { s -> s.substringAfter("=").split("..").map { it.toInt() }.let { it.first()..it.last() } }
                return Step(state, Cuboid(x, y, z))
            }
        }
    }
}
