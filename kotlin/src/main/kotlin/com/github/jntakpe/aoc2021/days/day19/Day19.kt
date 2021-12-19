package com.github.jntakpe.aoc2021.days.day19

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputSplitOnBlank
import kotlin.math.abs

object Day19 : Day {

    override val input = readInputSplitOnBlank(19).mapIndexed { i, l -> Scanner.from(i, l.lines().drop(1)) }

    override fun part1() = resolve().flatMap { it.beacons }.toSet().count()

    override fun part2() = resolve().mapNotNull { it.position }.run { flatMap { a -> map { a.dist(it) } } }.maxOrNull() ?: 0

    private fun resolve(): List<Scanner> {
        val scanners = input.toMutableList()
        while (scanners.any { it.position == null }) {
            for (i in scanners.indices)
                for (j in scanners.indices)
                    if (scanners[i].position != null && scanners[j].position == null)
                        scanners[i].align(scanners[j])?.apply { scanners[j] = this }
        }
        return scanners.toList()
    }

    data class Point(val x: Int, val y: Int, val z: Int) {

        companion object {

            val ZERO = Point(0, 0, 0)
        }

        operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
        operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
        fun dist(other: Point) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    data class Scanner(val id: Int, val beacons: List<Point>, val position: Point?) {
        companion object {

            private val rotations = listOf<Point.() -> Point>(
                { Point(x, -z, y) },
                { Point(-y, -z, x) },
                { Point(z, -y, x) },
                { Point(y, z, x) },
                { Point(-z, y, x) },
                { Point(-x, -z, -y) },
                { Point(z, -x, -y) },
                { Point(x, z, -y) },
                { Point(-z, x, -y) },
                { Point(y, -z, -x) },
                { Point(z, y, -x) },
                { Point(-y, z, -x) },
                { Point(-z, -y, -x) },
                { Point(z, x, y) },
                { Point(-x, z, y) },
                { Point(-z, -x, y) },
                { Point(x, -y, -z) },
                { Point(-x, -y, z) },
                { Point(y, -x, z) },
                { Point(x, y, z) },
                { Point(-y, x, z) },
                { Point(y, x, -z) },
                { Point(-x, y, -z) },
                { Point(-y, -x, -z) },
            )

            fun from(id: Int, lines: List<String>) = Scanner(id, lines.map { it.point() }, Point.ZERO.takeIf { id == 0 })

            private fun String.point() = split(',').map { it.toInt() }.let { (x, y, z) -> Point(x, y, z) }
        }

        fun align(other: Scanner): Scanner? {
            repeat(24) { i ->
                val rotated = other.rotate(i)
                beacons.flatMap { b -> rotated.beacons.map { b to it } }.forEach { (b, r) ->
                    val diff = b - r
                    val aligned = rotated.beacons.map { it + diff }
                    if (overlap(aligned)) {
                        return other.copy(beacons = aligned, position = diff)
                    }
                }
            }
            return null
        }

        private fun rotate(index: Int) = copy(beacons = beacons.map { rotations[index](it) })

        private fun overlap(other: Iterable<Point>) = (beacons.toSet() intersect other.toSet()).size >= 12
    }
}
