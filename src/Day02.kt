import kotlin.math.abs
import kotlin.streams.asSequence
import kotlin.time.measureTime

fun main() {
    measureTime {
        //parse
        val reports = lineStream("Day02")
            .use { lines ->
                lines.asSequence().map { line -> line.split(" ").map { it.toInt() } }.toList()
            }
        //solve1
        reports.count { report ->
            isSafe(report)
        }.also { println("Part 1: $it") }

        //solve 2
        reports.count { report ->
            isSafe(report) || report.indices.any {
                @Suppress("NAME_SHADOWING")
                val report = report.toMutableList()
                report.removeAt(it)
                isSafe(report)
            }
        }.also { println("Part 2: $it") }
    }.also { println("finished in $it") }
}

private fun isSafe(report: List<Int>): Boolean {
    val changes = report.windowed(2).map { (a, b) -> a - b }
    return changes.all { abs(it) in 1..3 } && (changes.all { it < 0 } || changes.all { it > 0 })
}
//Test data
//        Stream.of(
//            "7 6 4 2 1",
//            "1 2 7 8 9",
//            "9 7 6 2 1",
//            "1 3 2 4 5",
//            "8 6 4 4 1",
//            "1 3 6 7 9"
//        )