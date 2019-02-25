package grammar.operators

import grammar.GrammarItem
import kotlin.random.Random
import kotlin.random.nextInt

class OneOf(private val rnd:Random) {

    fun oneOf(grammarItems: List<GrammarItem>): GrammarItem {
        return oneOf(grammarItems.associate { it to 1 })
    }

    fun oneOf(grammarItems: Map<GrammarItem, Int>): GrammarItem {
        val sortedItems = grammarItems.toList().sortedBy { (_, value) -> value }.toMap()
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