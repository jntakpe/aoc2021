package com.github.jntakpe.aoc2021.days.day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {

    @Test
    fun `part 1 should sum versions`() {
        assertEquals(16, Day16.apply { input = "8A004A801A8002F478" }.part1())
        assertEquals(12, Day16.apply { input = "620080001611562C8802118E34" }.part1())
        assertEquals(23, Day16.apply { input = "C0015000016115A2E0802F182340" }.part1())
        assertEquals(31, Day16.apply { input = "A0016C880162017C3686B18A3D4780" }.part1())
    }

    @Test
    fun `part 2 should sum values`() {
        assertEquals(3, Day16.apply { input = "C200B40A82" }.part2())
    }

    @Test
    fun `part 2 should find values product`() {
        assertEquals(54, Day16.apply { input = "04005AC33890" }.part2())
    }

    @Test
    fun `part 2 should find values minimum`() {
        assertEquals(7, Day16.apply { input = "880086C3E88112" }.part2())
    }

    @Test
    fun `part 2 should find values maximum`() {
        assertEquals(9, Day16.apply { input = "CE00C43D881120" }.part2())
    }

    @Test
    fun `part 2 should return second less than first`() {
        assertEquals(1, Day16.apply { input = "D8005AC2A8F0" }.part2())
    }

    @Test
    fun `part 2 should return second greater than first`() {
        assertEquals(0, Day16.apply { input = "F600BC2D8F" }.part2())
    }

    @Test
    fun `part 2 should return both not equal`() {
        assertEquals(0, Day16.apply { input = "F600BC2D8F" }.part2())
    }

    @Test
    fun `part 2 should return both equal`() {
        assertEquals(1, Day16.apply { input = "9C0141080250320F1802104A08" }.part2())
    }
}
