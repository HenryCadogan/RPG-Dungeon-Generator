package grammar.operators

import grammar.GrammarItem
import kotlin.random.Random
import kotlin.reflect.full.createInstance

class OneOrMore(private val rnd: Random) {
    fun oneOrMore(item: GrammarItem, limit: Int, probability: Float): List<GrammarItem> {
        val output = mutableListOf<GrammarItem>()
        val clazz = item::class
        for (x in 0 until limit) {
            if (rnd.nextFloat() < probability)
                output.add(clazz.createInstance())
        }
        return output
    }
}
