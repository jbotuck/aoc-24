package com.github.jbotuck

import readInput

fun main() {
    val input = readInput("Day22").map { it.toInt() }

    //part1
    input.sumOf {
        generateSequence(it, ::nextValue).take(2001).last().toLong()
    }.also(::println)

    //part2
    input.flatMap { seed ->
        generateSequence(seed, ::nextValue)
            .map { it % 10 }
            .take(2001)
            .windowed(2)
            .map { (previous, current) -> current - previous to current }//change to bananas
            .windowed(4)
            .map { changeSequence ->
                changeSequence.map { it.first } to changeSequence.last().second
            }//change sequence to  bananas
            .groupBy { it.first }
            .mapValues { it.value.first().second }
            .entries //first occurence of changeSequence to bananas
    }.groupBy ( {it.key}, {it.value})//changeSequence to list of banana yields
        .mapValues { (_, values) -> values.sum() }//changeSequence to sum of banana yields
        .values.max().also { println(it) }
}

private fun nextValue(previousValue: Int): Int {
    var ret = previousValue
    fun mixAndPrune(i: Int) {
        ret = ret xor i
        ret = ret.mod(16777216)
    }
    mixAndPrune(ret * 64)
    mixAndPrune(ret / 32)
    mixAndPrune(ret * 2048)
    return ret
}

