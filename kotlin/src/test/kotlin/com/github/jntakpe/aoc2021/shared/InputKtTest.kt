package com.github.jntakpe.aoc2021.shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

internal class InputKtTest {

    @Nested
    inner class ReadInputLines {

        @Test
        fun `should return lines`() {
            assertEquals(listOf("1-3 a: abcde", "1-3 b: cdefg", "2-9 c: ccccccccc"), readInputLines(2))
        }

        @Test
        fun `should ignore empty lines`() {
            val lines = readInputLines(3)
            assertEquals(11, lines.size)
            assertFalse(lines.contains(""))
        }
    }

    @Nested
    inner class ReadInputNumbers {

        @Test
        fun `should return numbers`() {
            assertEquals(listOf(1721, 979, 366, 299, 675, 1456), readInputNumbers(1))
        }
    }

    @Nested
    inner class ReadInputSplitOnBlank {

        @Test
        fun `should split on blank`() {
            val lines = readInputSplitOnBlank(4)
            assertEquals(4, lines.size)
            assertEquals(listOf(
                """ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm""".trimIndent(),
                """iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929""",
                """hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm""",
                """hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in"""), lines)
        }
    }
}
