fun main() {
    //val compactDiskMap = "2333133121414131402"
    val compactDiskMap = readInput("Day09").first()
    val expandedMap = sequence {
        var id = 0
        var fileMode = true

        @Suppress("NAME_SHADOWING")
        val compactDiskMap = ArrayDeque(compactDiskMap.map { it.digitToInt() })
        while (compactDiskMap.isNotEmpty()) {
            repeat(compactDiskMap.removeFirst()) {
                yield(if (fileMode) id else -1)
            }
            if (fileMode) id++
            fileMode = !fileMode
        }
    }.toMutableList()
    var indexOfFreeSpace = expandedMap.indexOf(-1)
    var indexOfLastUsedSpace = expandedMap.indexOfLast { it != -1 }
    while (indexOfLastUsedSpace > indexOfFreeSpace) {
        expandedMap[indexOfFreeSpace] = expandedMap[indexOfLastUsedSpace]
        expandedMap[indexOfLastUsedSpace] = -1
        indexOfLastUsedSpace = expandedMap.indexOfLastUsedSpace(indexOfLastUsedSpace) ?: break
        indexOfFreeSpace = expandedMap.indexOfFreeSpace(indexOfFreeSpace.inc()) ?: break
    }
    println(expandedMap)
    expandedMap.withIndex()
        .filter { it.value >= 1 }
        .sumOf { (position, id) -> position * id.toLong() }
        .also { println(it) }
}

private fun MutableList<Int>.indexOfFreeSpace(start: Int): Int? {
    var current = start
    while (current in indices) {
        if (get(current) == -1) return current
        current++
    }
    return null
}

private fun MutableList<Int>.indexOfLastUsedSpace(end: Int): Int? {
    var current = end.dec()
    while (current in indices) {
        if (get(current) != -1) return current
        current--
    }
    return null
}
