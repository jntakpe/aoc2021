package com.github.jntakpe.aoc2021.days.day16

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInput

object Day16 : Day {

    override var input = readInput(16)

    override fun part1() = PacketFactory(input).build().subVersions.sum()

    override fun part2() = PacketFactory(input).build().value

    sealed interface Packet {

        val version: Int
        val value: Long
        val subVersions: List<Int>
    }

    class Literal(override val version: Int, override val value: Long) : Packet {

        override val subVersions: List<Int> = listOf(version)
    }

    class Operator(override val version: Int, override val value: Long, packets: List<Packet>) : Packet {

        companion object {

            fun from(version: Int, typeId: Int, packets: List<Packet>) = when (typeId) {
                0 -> packets.sumOf { it.value }
                1 -> packets.fold(1L) { a, c -> a * c.value }
                2 -> packets.minOf { it.value }
                3 -> packets.maxOf { it.value }
                4 -> error("Type 4 is restricted to value literals")
                5 -> packets.compare { first, second -> first > second }
                6 -> packets.compare { first, second -> first < second }
                7 -> packets.compare { first, second -> first == second }
                else -> error("Unsupported operator type '$typeId'")
            }.run { Operator(version, this, packets) }

            private fun List<Packet>.compare(comparison: (Long, Long) -> Boolean): Long {
                return take(2).map { it.value }.let { (first, last) -> if (comparison(first, last)) 1 else 0 }
            }
        }

        override val subVersions = packets.flatMap { it.subVersions } + listOf(version)
    }

    class PacketFactory(raw: String) {

        var unprocessed = raw.map { it.toString().toInt(16) }.joinToString("") { it.toString(2).padStart(4, '0') }

        fun build(): Packet {
            val version = process(3).toInt(2)
            val typeId = process(3).toInt(2)
            return if (typeId == 4) Literal(version, literalValue()) else Operator.from(version, typeId, subPackets())
        }

        private fun process(bits: Int) = unprocessed.take(bits).apply { unprocessed = unprocessed.drop(bits) }

        private fun literalValue(): Long {
            return buildString {
                while (unprocessed.first() == '1') {
                    append(process(5).drop(1))
                }
                append(process(5).drop(1))
            }.toLong(2)
        }

        private fun subPackets(): List<Packet> {
            return when (val id = process(1).single()) {
                '0' -> buildList {
                    val length = process(15).toLong(2)
                    val end = unprocessed.length - length
                    while (unprocessed.length > end) {
                        add(build())
                    }
                }
                '1' -> buildList { repeat((1..process(11).toLong(2)).count()) { add(build()) } }
                else -> error("Length type ID should be either '0' or '1' not '$id'")
            }
        }
    }
}
