
package com.github.jbotuck.parallel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.stream.consumeAsFlow
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.math.abs
import kotlin.time.measureTime

private suspend fun parseToLists(): Pair<List<Int>, List<Int>> {
    val left = Channel<Int>(Channel.UNLIMITED)
    val right = Channel<Int>(Channel.UNLIMITED)
    coroutineScope {
        val lines = Files.newBufferedReader(Path("src/Day01.txt"))
            .lines()
            .consumeAsFlow()
            .flowOn(Dispatchers.IO)
            .produceIn(this)

        repeat(4) {
            launch {
                for (line in lines) {
                    line.split(" ")
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }
                        .let {
                            left.send(it.first())
                            right.send(it.last())
                        }
                }
            }
        }
    }
    left.close()
    right.close()
    return left.toList() to right.toList()
}

fun main() {
    measureTime {
        runBlocking(Dispatchers.Default) {
            val (left, right) = parseToLists()
            //solve 1
            left.toList().sorted().zip(right.toList().sorted()).map { (a, b) -> async { abs(a - b) } }.awaitAll().sum()
                .let { println("PART 1: $it") }
            //solve 2
            val histogram = right.groupBy { it }.mapValues { it.value.size }
            left.map { async { histogram.getOrDefault(it, 0) * it } }.awaitAll().sum()
                .let { println("PART 2: $it") }
        }
    }.also { println("took $it to get done") }
}
