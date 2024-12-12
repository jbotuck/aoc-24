import kotlin.streams.asSequence

fun main() {
    lineStream("Day07").use { stream ->
        stream.asSequence()
            .sumOf { line ->
                line.split(":", " ")
                    .filter { it.isNotBlank() }
                    .map { it.toLong() }
                    .let { it.first() to it.drop(1) }
                    .takeIf { (testValue, formula) ->
                        canEqual(testValue, formula.first(), formula.subList(1, formula.size))
                    }?.first ?: 0
            }
    }.also { println(it) }
}

fun canEqual(testValue: Long, currentValue: Long, formula: List<Long>): Boolean {
        if(currentValue > testValue) return false
        if(formula.isEmpty()) return currentValue == testValue
        return sequenceOf<(Long.(Long) -> Long)>(
            Long::plus,
            Long::times,
            Long::concatenate //comment this out for part 1
        )
            .map { currentValue.it(formula.first()) }
            .any { canEqual(testValue, it, formula.subList(1, formula.size)) }
}
private fun Long.concatenate(other: Long): Long{
    return (toString() + other.toString()).toLong()
}