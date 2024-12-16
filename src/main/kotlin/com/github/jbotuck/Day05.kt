package com.github.jbotuck

import lineStream

fun main() {
    //Read
    val (rules, updates) = lineStream("Day05").use { stream ->
        val lines = stream.toList()
        lines
            .takeWhile { it.isNotBlank() }
            .map { line -> line.split("|").map { it.toInt() } }
            .map { (a, b) -> a to b }
            .groupBy({ it.first }) { it.second }
            .mapValues { it.value.toSet() } to
                lines
                    .takeLastWhile { it.isNotBlank() }
                    .map { line -> line.split(",").map { it.toInt() } }
    }
    fun score(order: List<Int>) = order[order.size / 2]

    fun List<Int>.part2Score(): Int {
        val order = toMutableList()
        fun swap(a: Int, b: Int) {
            val tmp = order[a]
            order[a] = order[b]
            order[b] = tmp
        }

        fun violatingIndex(i: Int): Int? {
            return order.subList(0, i)
                .indexOfFirst { it in rules[order[i]].orEmpty() }.takeUnless { it == -1 }
        }

        var modified = false
        for (i in order.indices) {
            var violatingIndex = violatingIndex(i)
            while (violatingIndex != null) {
                modified = true
                swap(violatingIndex, i)
                violatingIndex = violatingIndex(i)
            }
        }
        return if (modified) score(order) else 0
    }
    val (part1, part2) = updates.map { it to it.part2Score() }.partition { it.second == 0 }
    println(part1.sumOf { score(it.first) })
    println(part2.sumOf { it.second })
}

