package com.github.jntakpe.aoc2021.shared

fun readInputLines(day: Int) = readInput(day).lines()

fun readInputNumbers(day: Int) = readInputLines(day).map { it.toInt() }

fun readInputSplitOnBlank(day: Int) = readInput(day).split("(?m)^\\s*$".toRegex()).map { it.trim() }

fun readInput(day: Int) = "/day_$day.txt".run { resource()?.readText()?.trim() ?: error("Unable to read file: '$this'") }

private fun String.resource() = object {}::class.java.getResource(this)
