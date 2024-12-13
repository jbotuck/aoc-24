fun main() {
    val grid = readInput("Day10").map { line -> line.map { char -> char.digitToInt() } }
    fun indicesOfZeros() = grid.indices.flatMap { y ->
        grid[y].indices.filter { x -> grid[y][x] == 0 }.map { x -> y to x }
    }

    fun reachableNeighborsOf(y: Int, x: Int): Set<Pair<Int, Int>> {
        return shifts.map { (yShift, xShift) -> y + yShift to x + xShift }
            .filter { (newY, newX) -> grid.getOrNull(newY)?.getOrNull(newX) == grid[y][x].inc() }
            .toSet()
    }

    fun reachableNines(y: Int, x: Int): Set<Pair<Int, Int>> {
        if (grid[y][x] == 9) return setOf(Pair(y, x))
        return reachableNeighborsOf(y, x)
            .map { (y, x) -> reachableNines(y, x).toSet() }
            .reduceOrNull(Set<Pair<Int, Int>>::union).orEmpty()
    }
    indicesOfZeros().sumOf { (y, x) -> reachableNines(y, x).size }.also(::println)

    fun pathsToNines(y: Int, x: Int): Int {
        if (grid[y][x] == 9) return 1
        return reachableNeighborsOf(y, x).sumOf { (y, x) -> pathsToNines(y, x) }
    }
    indicesOfZeros().sumOf { (y, x) -> pathsToNines(y, x) }.also(::println)
}

private val shifts = listOf(
    1 to 0,
    -1 to -0,
    0 to 1,
    0 to -1,
)

