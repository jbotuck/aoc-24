package com.github.jbotuck

import readInput

fun main() {
    val lines = readInput("Day19")
    val trie = mutableMapOf<Char, TrieNode>()
    lines.first().split(", ").forEach {
        trie.computeIfAbsent(it.first()) { TrieNode() }.add(it.drop(1))
    }
    fun isFormable(s: String, index: Int = 0): Boolean {
        if (index == s.length) return true
        return trie[s[index]]?.matchingPrefixLengths(s, index)?.any {
            isFormable(s, index + it)
        } == true
    }
    val waysToForm = mutableMapOf<String, Long>("" to 1)
    fun waysToForm(s: String): Long {
        waysToForm[s]?.let { return it }
        return (trie[s.first()]?.matchingPrefixLengths(s, 0)?.sumOf {
            waysToForm(s.drop(it))
        } ?: 0).also { waysToForm[s] = it }
    }
    lines.drop(2).count { isFormable(it, 0) }.also { println(it) }
    lines.drop(2).sumOf { waysToForm(it) }.also { println(it) }
}

private class TrieNode(val children: MutableMap<Char, TrieNode> = mutableMapOf()) {
    fun matchingPrefixLengths(s: String, index: Int): Set<Int> = sequence {
        if ('X' in children.keys) yield(1)
        if (index < s.lastIndex) yieldAll(
            children[s[index.inc()]]
                ?.matchingPrefixLengths(s, index.inc())
                ?.map { it.inc() }
                .orEmpty()
        )
    }.toSet()

    fun add(s: String) {
        if (s.isEmpty()) {
            children['X'] = TrieNode()
            return
        }
        children.computeIfAbsent(s.first()) { TrieNode() }.add(s.drop(1))
    }
}