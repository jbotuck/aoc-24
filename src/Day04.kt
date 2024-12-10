fun main() {
    //Read
    val grid = readInput("Day04")

    //part1
    grid.indices
        .flatMap { y ->
            grid[y].indices.map { x ->
                if (grid[y][x] != 'X') 0
                else directions.count { (yDirection, xDirection) ->
                    grid.getOrNull(y + yDirection)?.getOrNull(x + xDirection) == 'M' &&
                            grid.getOrNull(y + 2 * yDirection)?.getOrNull(x + 2 * xDirection) == 'A' &&
                            grid.getOrNull(y + 3 * yDirection)?.getOrNull(x + 3 * xDirection) == 'S'
                }
            }
        }.sum()
        .also { println(it) }

    //part2
    grid.indices.asSequence()
        .flatMap { y -> grid[y].indices.asSequence().map { x -> y to x } }
        .count { (y, x) ->
            grid[y][x] == 'A' &&
                    grid.hasMs(y, x, forwardSlash) &&
                    grid.hasMs(y, x, backSlash)
        }
        .also { println(it) }

}

private fun List<String>.hasMs(y: Int, x: Int, shifts: List<Pair<(Int) -> Int, (Int) -> Int>>): Boolean {
    val chars = shifts.map { (yFunc, xFunc) -> getOrNull(yFunc(y))?.getOrNull(xFunc(x)) }
    return chars.any { it == 'M' } && chars.any { it == 'S' }
}

private val directions = listOf(
    1 to 0,//S
    -1 to 0,//N
    0 to 1, //E
    0 to -1,//W
    1 to 1,// SE
    -1 to -1,//NW
    1 to -1,//SW
    -1 to 1,//NE
)
private val forwardSlash = listOf(Int::inc to Int::dec, Int::dec to Int::inc)
private val backSlash = listOf(Int::dec to Int::dec, Int::inc to Int::inc)

