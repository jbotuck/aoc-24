package com.github.jbotuck

import readInput

fun main() {
    val grid = readInput("Day20")
    val start = grid.indices.firstNotNullOf { y -> grid[y].indexOf('S').takeUnless { it == -1 }?.let { y to it } }
    val positionSequence = sequence {
        var (previousY, previousX) = (-1 to -1)
        var (y, x) = start
        while (grid[y][x] != 'E') {
            val (nextY, nextX) = shifts
                .map { (yShift, xShift) -> y + yShift to x + xShift }
                .filter { (y, x) -> y != previousY || x != previousX }
                .singleOrNull { (y, x) -> grid[y][x] in emptySpaces }
                ?: throw IllegalStateException("This map doesn't work Like I thought")
            yield(nextY to nextX)
            previousY = y
            previousX = x
            y = nextY
            x = nextX
        }
    }
    val positionToIndex = positionSequence.withIndex().associate { it.value to it.index }
    sequence {
        for ((startY, startX) in positionSequence) {
            shifts.map { (yShift, xShift) -> startY + yShift to startX + xShift }
                .filter { (y, x) -> grid[y][x] == '#' }
                .flatMap { (occupiedY, occupiedX) ->
                    shifts.map { (yShift, xShift) -> occupiedY + yShift to occupiedX + xShift }
                        .filter { (y, x) -> grid.getOrNull(y)?.getOrNull(x) in emptySpaces }
                        .filter {
                            positionToIndex[it]!! - positionToIndex[startY to startX]!! >= 102
                        }
                        .map { listOf(startY to startX, occupiedY to occupiedX, it) }
                }.let { yieldAll(it) }
        }
    }.count().also { println(it) }

}

val emptySpaces = ".E".toSet()
private val shifts = listOf(
    0 to 1,
    0 to -1,
    -1 to 0,
    1 to 0
)