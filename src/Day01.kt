import kotlin.math.abs

fun main() {
    val (left, right) = readInput("Day01").map { line ->
        line.split(" ")
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .let { it.first() to it.last() }
    }.unzip()

    left.sorted().zip(right.sorted()).sumOf { (a, b) -> abs(a - b) }.let { println("PART 1: $it") }
    val histogram = right.groupBy { it }.mapValues { it.value.size }
    println("PART 2: ${left.sumOf { histogram.getOrDefault(it, 0) * it }}")

}
