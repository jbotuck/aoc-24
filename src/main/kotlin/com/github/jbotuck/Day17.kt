package com.github.jbotuck

import kotlin.math.pow

private val myProgram = listOf(2, 4, 1, 1, 7, 5, 4, 0, 0, 3, 1, 6, 5, 5, 3, 0)
fun main() {
    ThreeBit().execute().also { println(it.toList()) }
    solve().also { println(it!!.toLong(8)) }

}

fun solve(currentSolution: String = ""): String? {
    if (currentSolution.length == myProgram.size) return currentSolution
    val nextSolutions = '0'.rangeTo('7')
        .asSequence().map {
            it to ThreeBit(
                currentSolution.plus(it).toLong(8)
            ).execute().first()
        }.filter { (_, output) -> output == myProgram[myProgram.lastIndex - currentSolution.length].toLong() }
        .map { it.first }
        .toSet()
        .map { currentSolution.plus(it) }.toList()
    if (nextSolutions.isEmpty()) return null
    return nextSolutions.firstNotNullOfOrNull { solve(it) }
}

private class ThreeBit(
    var a: Long = 30899381,
) {
    private var pointer = 0
    var b = 0L
    var c = 0L
    val program: List<Int> = myProgram
    fun execute() = sequence {
        pointer = 0
        b = 0
        c = 0
        while (pointer < program.size) {
            val instruction = program[pointer++]
            when (instruction) {
                0 -> a /= 2.toDouble().pow(combo(program[pointer++]).toInt()).toLong()
                1 -> b = b xor program[pointer++].toLong()
                2 -> b = combo(program[pointer++]) % 8
                3 -> if (a != 0L) pointer = program[pointer] else pointer++
                4 -> b = (b xor c).also { pointer++ }
                5 -> yield(combo(program[pointer++]) % 8)
                6 -> b = a / 2.toDouble().pow(combo(program[pointer++]).toInt()).toLong()
                7 -> c = a / 2.toDouble().pow(combo(program[pointer++]).toInt()).toLong()
            }
        }
    }

    fun combo(operand: Int) = when (operand) {
        in 0..3 -> operand.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> throw IllegalStateException()
    }
}