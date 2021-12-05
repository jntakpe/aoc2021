package com.github.jntakpe.aoc2021

import com.github.jntakpe.aoc2021.days.day1.Day1
import com.github.jntakpe.aoc2021.days.day2.Day2
import com.github.jntakpe.aoc2021.days.day3.Day3
import com.github.jntakpe.aoc2021.days.day4.Day4

fun main(args: Array<String>) {
    when (val day = args[0].toInt()) {
        1 -> Day1.run()
        2 -> Day2.run()
        3 -> Day3.run()
        4 -> Day4.run()
        else -> error("Day $day not implemented yet")
    }
}
