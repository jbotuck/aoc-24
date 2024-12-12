fun main() {
    //val input = "2333133121414131402"
    val input = readInput("Day09").first()
    val (files, freeSpace) = sequence {
        var id = 0
        var fileMode = true
        var index = 0

        val compactDiskMap = ArrayDeque(input.map { it.digitToInt() })
        while (compactDiskMap.isNotEmpty()) {
            val blockSize = compactDiskMap.removeFirst()
            if (blockSize != 0)
                yield(Triple(if (fileMode) id else -1, index, blockSize))
            if (fileMode) id++
            fileMode = !fileMode
            index += blockSize
        }
    }.partition { it.first >= 0 }
    //part1
    run {
        val fileMap = files.groupBy({ it.first }, { it.second to it.third }).toMutableMap()
        val queue = ArrayDeque(freeSpace.map { it.second to it.third })

        for (file in files.map { it.first }.reversed()) {
            if (queue.isEmpty()) break
            var fileBlock = fileMap[file]!!.single()
            val newFileBlocks = mutableListOf<Pair<Int, Int>>()
            while (fileBlock.second > 0) {
                val freeBlock = queue.removeFirstOrNull()
                if (freeBlock == null || freeBlock.first > fileBlock.first) {
                    queue.clear()
                    newFileBlocks.add(fileBlock)
                    break
                }
                newFileBlocks.add(freeBlock.first to freeBlock.second.coerceAtMost(fileBlock.second))
                if (freeBlock.second > fileBlock.second)
                    queue.addFirst(freeBlock.first + fileBlock.second to freeBlock.second - fileBlock.second)
                fileBlock = fileBlock.first to fileBlock.second - freeBlock.second
            }
            fileMap[file] = newFileBlocks
        }

        fileMap.map { (id, blocks) ->
            id * blocks.sumOf { (start, size) -> start.toLong().until(start + size).sum() }
        }.sum().also { println(it) }
    }
    //part 2
    val fileMap: MutableMap<Int, Pair<Int, Int>> =
        files.associateBy({ it.first }, { it.second to it.third }).toMutableMap()
    val freeSpaceMap: MutableList<Pair<Int, Int>> = freeSpace.map { it.second to it.third }.toMutableList()
    for (file in files.map { it.first }.reversed()) {
        val fileBlock = fileMap[file]!!
        freeSpaceMap.indexOfFirst { it.first > fileBlock.first }
            .takeUnless { it == -1 }
            ?.let { freeSpaceMap.subList(it, freeSpaceMap.size).clear() }
        val freeIndex = freeSpaceMap.indexOfFirst { it.second >= fileBlock.second }
            .takeUnless { it == -1 } ?: continue
        val freeBlock = freeSpaceMap[freeIndex]
        fileMap[file] = freeBlock.first to fileBlock.second
        if (freeBlock.second == fileBlock.second) freeSpaceMap.removeAt(freeIndex)
        else freeSpaceMap[freeIndex] = freeBlock.first + fileBlock.second to freeBlock.second - fileBlock.second
    }

    fileMap.map { (id, block) ->
        id * block.let { (start, size) -> start.toLong().until(start + size).sum() }
    }.sum().also { println(it) }
}