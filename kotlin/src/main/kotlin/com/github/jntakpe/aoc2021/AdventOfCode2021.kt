package com.github.jntakpe.aoc2021

import com.github.jntakpe.aoc2021.days.day1.Day1
import com.github.jntakpe.aoc2021.days.day10.Day10
import com.github.jntakpe.aoc2021.days.day11.Day11
import com.github.jntakpe.aoc2021.days.day12.Day12
import com.github.jntakpe.aoc2021.days.day2.Day2
import com.github.jntakpe.aoc2021.days.day3.Day3
import com.github.jntakpe.aoc2021.days.day4.Day4
import com.github.jntakpe.aoc2021.days.day5.Day5
import com.github.jntakpe.aoc2021.days.day6.Day6
import com.github.jntakpe.aoc2021.days.day7.Day7
import com.github.jntakpe.aoc2021.days.day8.Day8
import com.github.jntakpe.aoc2021.days.day9.Day9

fun main(args: Array<String>) {
    when (val day = args[0].toInt()) {
        1 -> Day1.run()
        2 -> Day2.run()
        3 -> Day3.run()
        4 -> Day4.run()
        5 -> Day5.run()
        6 -> Day6.run()
        7 -> Day7.run()
        8 -> Day8.run()
        9 -> Day9.run()
        10 -> Day10.run()
        11 -> Day11.run()
        12 -> Day12.run()
        else -> error("Day $day not implemented yet")
    }
}
