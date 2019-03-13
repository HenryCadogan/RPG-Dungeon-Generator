package grammar.operators

import grammar.grammarItems.GrammarItem
import kotlin.random.Random
import kotlin.reflect.full.createInstance

class OneOrMore(private val rnd: Random) {

    fun oneOrMore(item: GrammarItem, limit:Int, probability: Float):List<GrammarItem>{
        val output = mutableListOf<GrammarItem>()
        for (x in 0 until limit) {
            if (rnd.nextFloat() < probability) {
                val clazz = item::class
                output.add(clazz.createInstance())
            }
        }
        if (output.isEmpty()){output.add(item)}
        return output
    }
}
