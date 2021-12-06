package com.github.jntakpe.aoc2021.shared

interface Day {

    val input: Any

    fun part1(): Number

    fun part2(): Number

    fun run() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}
