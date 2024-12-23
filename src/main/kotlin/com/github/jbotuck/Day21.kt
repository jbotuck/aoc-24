package com.github.jbotuck

import com.github.jbotuck.DButton.*
import readInput
import kotlin.math.absoluteValue

/*
    +---+---+
    | ^ | A |
+---+---+---+
| < | v | > |
+---+---+---+

+---+---+---+
| 7 | 8 | 9 |
+---+---+---+
| 4 | 5 | 6 |
+---+---+---+
| 1 | 2 | 3 |
+---+---+---+
    | 0 | A |
    +---+---+
 */
fun main() {
    readInput("Day21").sumOf {
        it.dropLast(1).toLong() * numMoves(it)
    }.also { println(it) }
    readInput("Day21").sumOf {
        it.dropLast(1).toLong() * numMoves(it, 25)
    }.also { println(it) }
}

private fun numMoves(code: String, level: Int = 2) = sequence {
    var currentPosition = 'A'
    for (c in code) {
        yield(dpadSequences(currentPosition, c).minOf { numMoves(it, level) })
        currentPosition = c
    }
}.sum()

private fun dpadSequences(start: Char, end: Char): List<List<DButton>> {
    val (yStart, xStart) = nButtonToIndex[start]!!
    val (yEnd, xEnd) = nButtonToIndex[end]!!
    val (yShift, xShift) = yEnd - yStart to xEnd - xStart
    return when {
        xShift == 0 -> listOf(yShift(yShift))
        yShift == 0 -> listOf(xShift(xShift))
        else -> listOf(
            yShift(yShift) + xShift(xShift),
            xShift(xShift) + yShift(yShift)
        ).filterNot { panics(start, it) }
    }.map { it + ACTIVATE }
}

private fun xShift(n: Int) = absoluteNButtons(n, if (n < 0) LEFT else RIGHT)
private fun yShift(n: Int) = absoluteNButtons(n, if (n < 0) UP else DOWN)
private fun absoluteNButtons(n: Int, b: DButton): List<DButton> {
    return Array(n.absoluteValue) { b }.toList()
}

private fun panics(position: Char, dButtonSequence: List<DButton>): Boolean {
    @Suppress("NAME_SHADOWING")
    var position = position
    for (b in dButtonSequence) {
        var (y, x) = nButtonToIndex[position]!!
        y += b.shift!!.first
        x += b.shift.second
        position = nPad[y][x]
        if (position == 'E') return true
    }
    return false
}

private fun numMoves(dpadSequence: List<DButton>, level: Int): Long {
    if (level == 0) return dpadSequence.size.toLong()
    return sequence {
        var current = ACTIVATE
        for (b in dpadSequence) {
            yield(numMoves(current, b, level))
            current = b
        }
    }.sum()
}

val numMovesBetweenDbuttonsMap = mutableMapOf<Triple<DButton, DButton, Int>, Long>()
private fun numMoves(start: DButton, end: DButton, level: Int): Long {
    return numMovesBetweenDbuttonsMap.getOrPut(
        Triple(start, end, level)
    ) { dpadSequences(start, end).minOf { numMoves(it, level.dec()) } }
}

private fun dpadSequences(start: DButton, end: DButton): List<List<DButton>> {
    val (yStart, xStart) = dButtonToIndex[start]!!
    val (yEnd, xEnd) = dButtonToIndex[end]!!
    val (yShift, xShift) = yEnd - yStart to xEnd - xStart
    return when {
        xShift == 0 -> listOf(yShift(yShift))
        yShift == 0 -> listOf(xShift(xShift))
        else -> listOf(
            yShift(yShift) + xShift(xShift),
            xShift(xShift) + yShift(yShift)
        ).filterNot { panics(start, it) }
    }.map { it + ACTIVATE }
}

private fun panics(position: DButton, dButtonSequence: List<DButton>): Boolean {
    @Suppress("NAME_SHADOWING")
    var position = position
    for (b in dButtonSequence) {
        var (y, x) = dButtonToIndex[position]!!
        y += b.shift!!.first
        x += b.shift.second
        position = dPad[y][x]
        if (position == PANIC) return true
    }
    return false
}

enum class DButton(val shift: Pair<Int, Int>? = null) {
    RIGHT(0 to 1),
    UP(-1 to 0),
    DOWN(1 to 0),
    LEFT(0 to -1),
    PANIC,
    ACTIVATE;
}

private val nPad = listOf(
    "789",
    "456",
    "123",
    "E0A"
)
private val dPad = listOf(
    listOf(PANIC, UP, ACTIVATE),
    listOf(LEFT, DOWN, RIGHT)
)
private val nButtonToIndex = nPad.flatMapIndexed { y, s ->
    s.mapIndexed { x, c -> c to Pair(y, x) }
}.toMap()

private val dButtonToIndex = dPad.flatMapIndexed { y, l ->
    l.mapIndexed { x, b -> b to Pair(y, x) }
}.toMap()