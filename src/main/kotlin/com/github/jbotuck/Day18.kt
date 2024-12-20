package com.github.jbotuck

import lineStream
import readInput
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.streams.asSequence

fun main() {
    run { //part1
        val grid = Array(71) { BooleanArray(71) }
        for ((x, y) in readInput("Day18")
            .take(1024)
            .map { line -> line.split(",").map { it.toInt() } }
        ) {
            grid[y][x] = true
        }
        val visited = mutableSetOf<Pair<Int, Int>>()
        val toVisit = PriorityQueue<Triple<Int, Int, Int>>(Comparator.comparingInt { it.third })
        toVisit.add(Triple(0, 0, 0))
        while (toVisit.isNotEmpty()) {
            val (y, x, steps) = toVisit.remove()
            if (y to x in visited) continue
            visited.add(y to x)
            if (y == 70 && x == 70) {
                println(steps)
                break
            }
            toVisit.addAll(
                shifts
                    .map { (yShift, xShift) -> Triple(y + yShift, x + xShift, steps.inc()) }
                    .filter { (y, x, _) -> grid.getOrNull(y)?.getOrNull(x) == false }
            )
        }
    }
    //part2
    val grid = Array(71) { BooleanArray(71) }
    fun isNavigable(): Boolean {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val toVisit = ArrayDeque<Pair<Int, Int>>()
        toVisit.add(Pair(0, 0))
        while (toVisit.isNotEmpty()) {
            val (y, x) = toVisit.removeLast()
            if (Pair(y, x) in visited) continue
            visited.add(y to x)
            if (y == 70 && x == 70) return true
            shifts
                .map { (yShift, xShift) -> y + yShift to x + xShift }
                .filter { (y, x) -> grid.getOrNull(y)?.getOrNull(x) == false }
                .filter { it !in visited }
                .takeUnless { it.isEmpty() }
                ?.let { toVisit.addAll(it) }
        }
        return false
    }
    lineStream("Day18").use { stream ->
        for ((x, y) in stream.asSequence()
            .map { line -> line.split(",").map { it.toInt() } }) {
            grid[y][x] = true
            if (!isNavigable()) {
                println("$x,$y")
                break
            }
        }
    }
}

private val shifts = listOf(
    0 to 1,
    0 to -1,
    -1 to 0,
    1 to 0
)