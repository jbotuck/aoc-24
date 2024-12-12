fun main() {
    //val compactDiskMap = "2333133121414131402"
    val compactDiskMap = readInput("Day09").first()
    val (fileList, fileMap, freeSpace) = run {
        val (files, freeSpace) = sequence {
            var id = 0
            var fileMode = true
            var index = 0

            @Suppress("NAME_SHADOWING")
            val compactDiskMap = ArrayDeque(compactDiskMap.map { it.digitToInt() })
            while (compactDiskMap.isNotEmpty()) {
                val blockSize = compactDiskMap.removeFirst()
                if (blockSize != 0)
                    yield(Pair(if (fileMode) id else -1, index until index + blockSize))
                if (fileMode) id++
                fileMode = !fileMode
                index += blockSize
            }
        }.partition { it.first >= 0 }
        val fileMap = files.groupBy({ it.first }, { it.second }).toMutableMap()
        val queue = ArrayDeque(freeSpace.map { it.second })
        Triple(files.map { it.first }, fileMap, queue)
    }

    for (file in fileList.reversed()) {
        if (freeSpace.isEmpty()) break
        var fileBlock = fileMap[file]!!.single()
        val newFileBlocks = mutableListOf<IntRange>()
        while (!fileBlock.isEmpty()) {
            val freeBlock = freeSpace.removeFirstOrNull()
            if (freeBlock == null || freeBlock.first > fileBlock.last) {
                freeSpace.clear()
                newFileBlocks.add(fileBlock)
                break
            }
            val fileBlockSize = fileBlock.size()
            newFileBlocks.add(freeBlock.takeFirst(fileBlockSize))
            freeBlock.dropFirst(fileBlockSize).takeUnless { it.isEmpty() }?.let { freeSpace.addFirst(it) }
            fileBlock = fileBlock.dropLast(freeBlock.size())
        }
        fileMap[file] = newFileBlocks
    }
    println(fileMap)
    fileMap.map { (id, blocks) ->
        blocks.sumOf { block -> block.sumOf { it * id.toLong() } }
    }.sum().also { println(it) }

}

private fun IntRange.size() = last.inc().minus(first).coerceAtLeast(0)
private fun IntRange.takeFirst(n: Int): IntRange = start until start.plus(n).coerceAtMost(endInclusive.inc())
private fun IntRange.dropFirst(n: Int): IntRange = start.plus(n)..endInclusive
private fun IntRange.dropLast(n: Int): IntRange = start..endInclusive.minus(n)

