fun main() {
    val grid = readInput("Day12")
    val visited = Array(grid.size) { BooleanArray(grid.first().length) }
    fun collectCrop(y: Int, x: Int): Pair<Long, Long> {
        if (visited[y][x]) return 0L to 0L
        visited[y][x] = true
        val (inRegion, outOfRegion) = shifts.map { (yShift, xShift) -> y + yShift to x + xShift }
            .partition { (newY, newX) -> grid.getOrNull(newY)?.getOrNull(newX) == grid[y][x] }
        return inRegion
            .map { (y, x) -> collectCrop(y, x) }
            .fold(1L to outOfRegion.size.toLong()) { a, b ->
                a.first + b.first to a.second + b.second
            }
    }
    sequence {
        for ((y, x) in grid.indices.flatMap { y -> grid[y].indices.map { x -> y to x } }) {
            if (visited[y][x]) continue
            yield(collectCrop(y, x))
        }
    }.sumOf { it.toList().reduce(Long::times) }.also(::println)
}

private val shifts = listOf(
    1 to 0,
    -1 to -0,
    0 to 1,
    0 to -1,
)