package com.github.jbotuck

import lineStream
import org.apache.commons.math4.legacy.linear.Array2DRowRealMatrix
import org.apache.commons.math4.legacy.linear.ArrayRealVector
import org.apache.commons.math4.legacy.linear.LUDecomposition
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.streams.asSequence

fun main() {
    lineStream("Day13").use { stream ->
        stream.asSequence().chunked(4).sumOf { it.parse().minTokensA() }
    }.also { println(it) }

    lineStream("Day13").use { stream ->
        stream.asSequence().chunked(4).sumOf { it.parse().minTokensB() }
    }.also { println(it) }
}

private val line1Regex = Regex("""Button A: X\+(\d+), Y\+(\d+)""")
private val line2Regex = Regex("""Button B: X\+(\d+), Y\+(\d+)""")
private val line3Regex = Regex("""Prize: X=(\d+), Y=(\d+)""")
private fun List<String>.parse(): ClawMachine {
    val (aX, aY) = line1Regex.matchEntire(first())!!.groupValues.drop(1).map { it.toInt() }
    val (bX, bY) = line2Regex.matchEntire(this[1])!!.groupValues.drop(1).map { it.toInt() }
    val (pX, pY) = line3Regex.matchEntire(this[2])!!.groupValues.drop(1).map { it.toInt() }
    return ClawMachine(
        aX = aX,
        bX = bX,
        pX = pX,
        aY = aY,
        bY = bY,
        pY = pY
    )
}

private data class ClawMachine(
    val aX: Int,
    val bX: Int,
    val pX: Int,
    val aY: Int,
    val bY: Int,
    val pY: Int,
) {
    fun minTokensA(): Int {
        val (a, b) = LUDecomposition(
            Array2DRowRealMatrix(
                arrayOf(
                    listOf(aX, bX).map { it.toDouble() }.toDoubleArray(),
                    listOf(aY, bY).map { it.toDouble() }.toDoubleArray()
                )
            )
        ).solver.solve(ArrayRealVector(listOf(pX, pY).map { it.toDouble() }.toDoubleArray()))
            .let { it.getEntry(0).roundToInt() to it.getEntry(1).roundToInt() }
        return if (a <= 100 && b <= 100 && a * aX + b * bX == pX && a * aY + b * bY == pY) 3 * a + b else 0
    }
    fun minTokensB(): Long {
        val pX = pX + 10000000000000
        val pY = pY + 10000000000000
        val (a, b) = LUDecomposition(
            Array2DRowRealMatrix(
                arrayOf(
                    listOf(aX, bX).map { it.toDouble() }.toDoubleArray(),
                    listOf(aY, bY).map { it.toDouble() }.toDoubleArray()
                )
            )
        ).solver.solve(ArrayRealVector(listOf(pX, pY).map { it.toDouble() }.toDoubleArray()))
            .let { it.getEntry(0).roundToLong() to it.getEntry(1).roundToLong() }
        return if (a * aX + b * bX == pX && a * aY + b * bY == pY) 3 * a + b else 0
    }
}