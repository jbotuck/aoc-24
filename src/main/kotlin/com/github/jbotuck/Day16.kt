package com.github.jbotuck

import readInput
import java.util.PriorityQueue

fun main() {
    val grid = readInput("Day16")
    val visited = mutableSetOf<Pair<Int, Int>>()
    val toVisit = PriorityQueue<Position>(Comparator.comparingInt { it.score })
        .apply {
            val (y, x) = grid
                .indices
                .firstNotNullOf { y ->
                    grid[y]
                        .indexOf('S')
                        .takeUnless { it == -1 }
                        ?.let { y to it }
                }
            add(Position(y, x, DirectionDay16.RIGHT))
        }
    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.remove()
        if (visiting.run { y to x } in visited) continue
        visited.add(visiting.run { y to x })
        if (visiting.run { grid[y][x] } == 'E') {
            println(visiting.score)
            break
        }
        toVisit.addAll(visiting.neighbors(grid).filter { it.run { y to x } !in visited })
    }
}

private class Position(val y: Int, val x: Int, val orientation: DirectionDay16, val score: Int = 0) {
    fun neighbors(grid: List<String>): Collection<Position> {
        return DirectionDay16.entries.map { Triple(y + it.y, x + it.x, it) }
            .filter { (y, x, _) -> grid.getOrNull(y)?.getOrNull(x).let { it !in setOf(null, '#') } }
            .map { (y, x, newOrientation) ->
                Position(
                    y, x, newOrientation, score.inc() + when {
                        newOrientation == orientation -> 0
                        newOrientation.x == orientation.x || newOrientation.y == orientation.y -> 2000
                        else -> 1000
                    }
                )
            }
    }
}

private enum class DirectionDay16(val y: Int, val x: Int) {
    LEFT(0, -1),
    RIGHT(0, 1),
    UP(-1, 0),
    DOWN(1, 0);
}