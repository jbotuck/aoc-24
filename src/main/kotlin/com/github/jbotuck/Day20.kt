package com.github.jbotuck

import readInput

fun main() {
    val grid = readInput("Day20")
    val start = grid.indices.firstNotNullOf { y -> grid[y].indexOf('S').takeUnless { it == -1 }?.let { y to it } }
    val positionSequence = sequence {
        var (previousY, previousX) = (-1 to -1)
        var (y, x) = start
        yield(start)
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
    }.toList()
    val positionToIndex = positionSequence.withIndex().associate { it.value to it.index }

    fun countCheats(cheatModeDuration: Int): Int{
        return positionSequence.sumOf { (startingY, startingX) ->
            sequence<Pair<Int, Int>> {
                val visited = mutableSetOf<Pair<Int, Int>>()
                val toVisit = ArrayDeque<Triple<Int, Int, Int>>().apply { add(Triple(startingY, startingX, 0)) }
                while (toVisit.isNotEmpty()) {
                    val (y, x, movesUsed) = toVisit.removeFirst()
                    if (y to x in visited) continue
                    visited.add(y to x)
                    if (
                        grid[y][x] in emptySpaces &&
                        positionToIndex[y to x]!! - positionToIndex[startingY to startingX]!! - movesUsed >= 100
                    ) yield(y to x)
                    if (movesUsed == cheatModeDuration) continue
                    shifts.map { (yShift, xShift) -> y + yShift to x + xShift }
                        .filter { it !in visited }
                        .filter { (y, x) -> y in grid.indices && x in grid[y].indices }
                        .map { (y, x) -> Triple(y, x, movesUsed.inc()) }
                        .let { toVisit.addAll(it) }

                }
            }.toSet().count()
        }
    }
    println(countCheats(2))
    println(countCheats(20))
}

val emptySpaces = ".E".toSet()
private val shifts = listOf(
    0 to 1,
    0 to -1,
    -1 to 0,
    1 to 0
)