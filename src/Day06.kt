fun main() {
    val grid = readInput("Day06")

    @Suppress("NAME_SHADOWING")
    fun obstacleCreatesLoop(y: Int, x: Int, direction: Direction): Boolean {
        val grid = grid.map(String::toMutableList)
        grid[y][x] = '#'
        var (y, x) = positionRelativeToObstacle(direction).let { (yShift, xShift) -> y + yShift to x + xShift }
        var direction = direction.rotate()
        val visited = mutableSetOf<Triple<Int, Int, Direction>>()
        while (y in grid.indices && x in grid[y].indices) {
            if (grid[y][x] != '#') {
                if (!visited.add(Triple(y, x, direction))) return true
                y += direction.y
                x += direction.x
            } else {
                val (yShift, xShift) = positionRelativeToObstacle(direction)
                y += yShift
                x += xShift
                direction = direction.rotate()
            }
        }
        return false
    }

    var direction = Direction.UP
    var loopCount = 0
    var (y, x) = grid.indices
        .firstNotNullOf { y ->
            grid[y]
                .indexOfFirst { it == '^' }
                .takeUnless { it == -1 }
                ?.let { y to it }
        }
    val visited = mutableSetOf(y to x)
    while (y in grid.indices && x in grid[y].indices) {
        if (grid[y][x] != '#') {
            if (y to x !in visited && obstacleCreatesLoop(y, x, direction)) loopCount++
            visited.add(y to x)
            y += direction.y
            x += direction.x
        } else {
            val (yShift, xShift) = positionRelativeToObstacle(direction)
            y += yShift
            x += xShift
            direction = direction.rotate()
        }
    }
    println(visited.size)
    println(loopCount)
}

fun positionRelativeToObstacle(direction: Direction): Pair<Int, Int> {
    return when (direction) {
        Direction.UP -> Pair(1, 1)
        Direction.DOWN -> Pair(-1, -1)
        Direction.RIGHT -> Pair(1, -1)
        Direction.LEFT -> Pair(-1, 1)
    }
}

enum class Direction(val y: Int, val x: Int) {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    fun rotate(): Direction {
        return entries[ordinal.inc() % 4]
    }
}