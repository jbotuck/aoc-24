package com.github.jbotuck.com.github.jbotuck

import readInput

fun main() {
    val (rawGrid, directions) = run {
        val lines = readInput("Day15")
        val blankIndex = lines.indexOfFirst { it.isBlank() }
        lines.subList(0, blankIndex) to lines.subList(blankIndex.inc(), lines.size).reduce(String::plus)
    }
    var grid = rawGrid.map { it.toMutableList() }
    fun swap(a: Pair<Int, Int>, b: Pair<Int, Int>) {
        val tmp = grid[a.first][a.second]
        grid[a.first][a.second] = grid[b.first][b.second]
        grid[b.first][b.second] = tmp
    }

    fun canMove(position: Pair<Int, Int>, shift: Shift): Boolean {
        val other = position.first + shift.y to position.second + shift.x
        return when (grid[other.first][other.second]) {
            '#' -> false
            '.' -> true
            SMALL_BOX -> canMove(other, shift)
            LEFT_BOX -> canMove(other, shift) && (
                    !shift.isVertical() ||
                            canMove(other.copy(second = other.second.inc()), shift)
                    )

            RIGHT_BOX -> canMove(other, shift) && (
                    !shift.isVertical() ||
                            canMove(other.copy(second = other.second.dec()), shift)
                    )

            else -> throw IllegalStateException()
        }
    }

    fun move(position: Pair<Int, Int>, shift: Shift) {
        val otherIndex = position.first + shift.y to position.second + shift.x
        val otherValue = grid[otherIndex.first][otherIndex.second]
        if (otherValue in setOf(SMALL_BOX, LEFT_BOX, RIGHT_BOX)) {
            move(otherIndex, shift)
            if (shift.isVertical() && otherValue != SMALL_BOX) {
                move(
                    otherIndex.copy(
                        second =
                            if (otherValue == LEFT_BOX)
                                otherIndex.second.inc()
                            else otherIndex.second.dec()
                    ),
                    shift
                )
            }
        }
        swap(position, otherIndex)
    }

    fun position() = grid.withIndex().firstNotNullOf { (y, row) ->
        row.indexOf('@').takeUnless { it == -1 }?.let { y to it }
    }

    //START OF PART 1
    var position = position()
    for (direction in directions) {
        val shift = Shift.fromSymbol(direction)
        if (canMove(position, shift)) {
            move(position, shift)
            position = position.first + shift.y to position.second + shift.x
        }
    }
    println(
        grid.indices.sumOf { y ->
            grid[y]
                .withIndex()
                .sumOf { (x, c) -> if (c == SMALL_BOX) 100 * y + x else 0 }
        }
    )

    //part2
    grid = rawGrid.map { row ->
        row.flatMap {
            when (it) {
                SMALL_BOX -> "[]"
                '@' -> "@."
                else -> "$it$it"
            }.asIterable()
        }.toMutableList()
    }
    position = position()
    for (direction in directions) {
        val shift = Shift.fromSymbol(direction)
        if (canMove(position, shift)) {
            move(position, shift)
            position = position.first + shift.y to position.second + shift.x
        }
    }
    println(
        grid.indices.sumOf { y ->
            grid[y]
                .withIndex()
                .sumOf { (x, c) -> if (c == LEFT_BOX) 100 * y + x else 0 }
        }
    )
}

private const val SMALL_BOX = 'O'
private const val LEFT_BOX = '['
private const val RIGHT_BOX = ']'

private enum class Shift(val symbol: Char, val y: Int, val x: Int) {
    LEFT('<', 0, -1),
    RIGHT('>', 0, 1),
    UP('^', -1, 0),
    DOWN('v', 1, 0);

    fun isVertical() = this in verticals

    companion object {
        fun fromSymbol(symbol: Char) = entries.first { it.symbol == symbol }
        private val verticals = setOf(UP, DOWN)
    }
}



