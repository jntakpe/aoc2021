package com.github.jntakpe.aoc2021.shared

interface Day {

    val input: List<Any>

    fun part1(): Int

    fun part2(): Int

    fun run() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}
