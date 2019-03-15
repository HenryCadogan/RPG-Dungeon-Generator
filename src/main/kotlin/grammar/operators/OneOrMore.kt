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
                /*
                    find factory for class
                    create new instance based off current class parameters.
                    use static object map of clazz to factory?
                */
                output.add(clazz.createInstance())
                //todo change this to be a factory method so that there isnt the need for the no arg constructor
            }
        }
        if (output.isEmpty()){output.add(item)}
        return output
    }
}

fun GrammarItem.oneOrMore(limit:Int):List<GrammarItem>{
    return OneOrMore(Random).oneOrMore(this,limit,0.5f)
}
