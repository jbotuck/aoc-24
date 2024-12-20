package com.github.jbotuck

import readInput
import java.util.PriorityQueue

fun main() {
    val grid = readInput("Day16")
    val visited = mutableMapOf<Triple<Int, Int, DirectionDay16>, Int>()
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
            add(Position(y, x, DirectionDay16.RIGHT, 0, emptyList()))
        }
    var score: Int? = null
    val goodSeats = mutableSetOf<Pair<Int, Int>>()
    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.remove()
        if (visited[visiting.run { Triple(y, x, orientation) }]?.let { it < visiting.score } == true) continue
        visited[visiting.run { Triple(y, x, orientation) }] = visiting.score
        if (visiting.run { grid[y][x] } == 'E') {
            if (score == null) {
                score = visiting.score
                goodSeats.add(visiting.y to visiting.x)
                println(visiting.score)
            }
            if (visiting.score > score) break
            goodSeats.addAll(visiting.path)
        }
        toVisit.addAll(
            visiting.neighbors(grid)
                .filterNot { neighbor ->
                    visited[neighbor.run { Triple(y, x, orientation)}]?.let { it < neighbor.score } == true
                }
        )
    }
    println(goodSeats.size)
}


private class Position(
    val y: Int,
    val x: Int,
    val orientation: DirectionDay16,
    val score: Int = 0,
    val path: List<Pair<Int, Int>>
) {
    fun neighbors(grid: List<String>): Collection<Position> {
        return DirectionDay16.entries.map { Triple(y + it.y, x + it.x, it) }
            .filter { (y, x, _) -> grid.getOrNull(y)?.getOrNull(x).let { it !in setOf(null, '#') } }
            .map { (newY, newX, newOrientation) ->
                Position(
                    y = newY,
                    x = newX,
                    orientation = newOrientation,
                    score = score.inc() + when {
                        newOrientation == orientation -> 0
                        newOrientation.x == orientation.x || newOrientation.y == orientation.y -> 2000
                        else -> 1000
                    },
                    path = path + Pair(y, x)
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