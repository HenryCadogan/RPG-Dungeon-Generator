package grammar.operators

import grammar.GrammarItem
import kotlin.random.Random

class OneOf<T>(private val rnd:Random) {

    fun oneOf(items: List<T>): T {
        return oneOf(items.associate { it to 1 })
    }

    fun oneOf(items: Map<T, Int>): T {
        val sortedItems = items.toList().sortedBy { (_, value) -> value }.toMap()
        val keys = sortedItems.keys.toList()
        val maxVal = sortedItems.values.toIntArray().sum()
        var pick = rnd.nextInt(1,maxVal)
        var current = 0
        for (item in keys) {
            pick -= sortedItems.getValue(item)
            if (pick <= 0) break else current++
        }
        return keys[current]
    }
}