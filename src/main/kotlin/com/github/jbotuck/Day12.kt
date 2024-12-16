package com.github.jbotuck

import readInput

fun main() {
    val grid = readInput("Day12")
    val visited = Array(grid.size) { BooleanArray(grid.first().length) }
    val countedEdges = mutableSetOf<Triple<Shift, Int, Int>>()
    fun findEntireSideAndMarkAsCounted(y: Int, x: Int, shift: Shift) {
        countedEdges.add(Triple(shift, y, x))
        val directionsToCheck = Shift.entries.minus(setOf(shift, shift.opposite()))
        for (direction in directionsToCheck) {
            var newY = y + direction.y
            var newX = x + direction.x
            while (grid.getOrNull(newY)?.getOrNull(newX) == grid[y][x] && grid.getOrNull(newY + shift.y)
                    ?.getOrNull(newX + shift.x) != grid[y][x]
            ) {
                countedEdges.add(Triple(shift, newY, newX))
                newY += direction.y
                newX += direction.x
            }
        }
    }

    fun isNewSide(y: Int, x: Int, shift: Shift): Boolean {
        if (Triple(shift, y, x) in countedEdges) return false
        findEntireSideAndMarkAsCounted(y, x, shift)
        return true
    }

    fun collectCrop(y: Int, x: Int): Pair<Long, Long> {
        if (visited[y][x]) return 0L to 0L
        visited[y][x] = true
        val (inRegion, outOfRegion) = Shift.entries.map { Triple(it, y + it.y, x + it.x) }
            .partition { (_, newY, newX) -> grid.getOrNull(newY)?.getOrNull(newX) == grid[y][x] }
        val fencing = outOfRegion.map { it.first }.count { isNewSide(y, x, it) }.toLong()
        return inRegion
            .map { (_, y, x) -> collectCrop(y, x) }
            .fold(1L to fencing) { a, b ->
                a.first + b.first to a.second + b.second
            }
    }
    sequence {
        for ((y, x) in grid.indices.flatMap { y -> grid[y].indices.map { x -> y to x } }) {
            if (visited[y][x]) continue
            yield(collectCrop(y, x))
            countedEdges.clear()
        }
    }.sumOf { it.toList().reduce(Long::times) }.also(::println)
}

private enum class Shift(val y: Int, val x: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    fun component1() = y
    fun component2() = x

    fun opposite() = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }
}