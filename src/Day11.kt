fun main() {
    val stones = readInput("Day11").first().split(" ").map { it.toLong() }

    val numberOfStonesGenerated = mutableMapOf<Pair<Long, Int>, Long>()
    fun numberOfStonesGenerated(stone: Long, remainingBlinks: Int): Long {
        if (remainingBlinks == 0) return 1
        numberOfStonesGenerated[stone to remainingBlinks]?.let { return it }
        return blink(stone).sumOf { numberOfStonesGenerated(it, remainingBlinks.dec()) }
            .also { numberOfStonesGenerated[stone to remainingBlinks] = it }
    }

    println(stones.sumOf { numberOfStonesGenerated(it, 75) })
}

private fun blink(stone: Long): List<Long> {
    return when {
        stone == 0L -> listOf(1)
        stone.toString().length % 2 == 0 -> {
            @Suppress("NAME_SHADOWING")
            val stone = stone.toString()
            listOf(stone::take, stone::takeLast)
                .map { it.invoke(stone.length / 2).toLong() }
        }
        else -> listOf(stone * 2024)
    }
}