import kotlin.math.abs

fun main() {
    //parse
    val (left, right) = readInput("Day01").map { line ->
        line.split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .let { it.first() to it.last() }
    }.unzip()

    //solve 1
    left.sorted().zip(right.sorted()).sumOf { (a, b) -> abs(a - b) }
        .let { println("PART 1: $it") }

    //solve 2
    val histogram = right.groupBy { it }.mapValues { it.value.size }
    left.sumOf { histogram.getOrDefault(it, 0) * it }
        .let { println("PART 2: $it") }

}
