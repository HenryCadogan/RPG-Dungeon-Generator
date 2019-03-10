package grammar.operators

import kotlin.random.Random


class OneOf(private val rnd: Random) {

    fun <T> oneOf(items: List<T>): List<T> {
        return oneOf(items.associate { it to 1 })
    }

    fun <T> oneOf(items: Map<T, Int>): List<T> {
        if (items.size == 1) return listOf(items.keys.first())
        val sortedItems = items.toList().sortedBy { (_, value) -> value }.toMap()
        val keys = sortedItems.keys.toList()
        val maxVal = sortedItems.values.toIntArray().sum()
        var pick = rnd.nextInt(1, maxVal + 1)
        var current = 0
        for (item in keys) {
            pick -= sortedItems.getValue(item)
            if (pick <= 0) break else current++
        }
        return listOf(keys[current])
    }
}