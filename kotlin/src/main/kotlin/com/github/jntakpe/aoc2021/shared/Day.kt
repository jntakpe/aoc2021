package com.github.jntakpe.aoc2021.shared

import kotlin.system.measureTimeMillis

interface Day {

    val input: Any

    fun part1(): Number

    fun part2(): Number

    fun run() {
        measureTimeMillis {
            print("Part 1: ${part1()}")
        }.let { println(" in $it ms") }
        measureTimeMillis {
            print("Part 2: ${part2()}")
        }.let { println(" in $it ms") }
    }
}
