package com.github.jbotuck.com.github.jbotuck

import readInput

fun main() {
    var robots = readInput("Day14").map { line ->
        Regex("""p=(\d+),(\d+) v=(-*\d+),(-*\d+)""").matchEntire(line)!!.groupValues
            .drop(1)
            .map { it.toInt() }
            .let {
                Robot(
                    y = it[1],
                    x = it[0],
                    yV = it[3],
                    xV = it[2]
                )
            }
    }

    robots
        .map(Robot::in100)
        .groupBy { it.quadrant() }
        .filterNot { it.key == 0 }
        .values
        .map { it.count() }
        .reduce(Int::times)
        .also { println(it) }

    var timePassed = 0
    while (true) {
        println("time Passed: $timePassed")
        robots.print()
        timePassed++
        robots = robots.map { it.advance(1) }
    }
}


private const val width = 101
private const val height = 103

private data class Robot(val y: Int, val x: Int, val yV: Int, val xV: Int) {
    fun in100() = advance(100)
    fun advance(n: Int) = copy(y = y + yV * n, x = x + xV * n).bringInBounds()
    private fun bringInBounds(): Robot = copy(
        y = bringInBounds(y, height),
        x = bringInBounds(x, width)
    )

    private fun bringInBounds(p: Int, endExclusive: Int) = p
        .mod(endExclusive)//bring in bounds
        .plus(endExclusive)//make positive
        .mod(endExclusive)//bring in bounds

    fun quadrant(): Int {
        return when {
            y == height / 2 || x == width / 2 -> 0
            y in 0 until height / 2 -> if (x in 0 until width / 2) 1 else 2
            else -> if (x in 0 until width / 2) 3 else 4
        }
    }
}

private fun List<Robot>.print() {
    val positions = mutableSetOf<Pair<Int, Int>>()
    for(robot in this) if(!positions.add(robot.y to robot.x)) return
    val grid = Array(height) { BooleanArray(width) }
    for (robot in this){
        grid[robot.y][robot.x] = true
    }
    println()
    for(row in grid){
        println()
        for(b in row){
            print(if (b) '#' else '.')
        }
    }
    println()
}