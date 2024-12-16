package com.github.jbotuck

import readInput

fun main() {
    val grid = readInput("Day08")
    fun antiNodes1(a: Pair<Int, Int>, b: Pair<Int, Int>): List<Pair<Int, Int>> {
        val deltaY = a.first - b.first
        val deltaX = a.second - b.second
        return listOf(a.first + deltaY to a.second + deltaX, b.first - deltaY to b.second - deltaX)
            .filter { (y, x) -> grid.getOrNull(y)?.getOrNull(x) != null }

    }

    fun antiNodes2(a: Pair<Int, Int>, b: Pair<Int, Int>) = sequence {
        val deltaY = a.first - b.first
        val deltaX = a.second - b.second
        var y = a.first
        var x = a.second
        while (grid.getOrNull(y)?.getOrNull(x) != null) {
            yield(y to x)
            y += deltaY
            x += deltaX
        }
        y = b.first
        x = b.second
        while (grid.getOrNull(y)?.getOrNull(x) != null) {
            yield(y to x)
            y -= deltaY
            x -= deltaX
        }
    }.toSet()

    fun antiNodes(antennas: List<Pair<Int, Int>>) = sequence {
        for (i in antennas.indices) {
            for (j in i.inc()..antennas.lastIndex) {
                yieldAll(antiNodes2(antennas[i], antennas[j]))
            }
        }
    }.toList()

    grid.withIndex().flatMap { (y, row) ->
        row.withIndex().mapNotNull { (x, c) -> if (c == '.') null else c to (y to x) }
    }.groupBy({ it.first }) { it.second }
        .flatMap {
            antiNodes(it.value)
        }.toSet().size.also { println(it) }
}