fun main() {
    //Read
    val text = readText("Day03")

    fun multiply(multiplication: MatchResult) = multiplication.groupValues.drop(1).map(String::toInt).reduce(Int::times)

    //part1
    Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(text).sumOf(::multiply)
        .also { println("PART 1: $it") }

    //part2
    sequence {
        var doMultiply = true
        Regex("""don't\(\)|do\(\)|mul\((\d{1,3}),(\d{1,3})\)""").findAll(text).forEach {
            when {
                doMultiply && it.value.startsWith("mul") -> yield(multiply(it))
                it.value.startsWith("do()") -> doMultiply = true
                it.value.startsWith("don't()") -> doMultiply = false
            }
        }
    }.sum()
        .also { println("Part 2: $it") }
}