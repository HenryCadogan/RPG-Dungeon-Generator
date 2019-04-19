package grammar.operators

import grammar.Factories
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.Placeholder
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.TrappedRoom
import grammar.grammarItems.factories.Item
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class OneOrMore(private val rnd: Random) {

    fun oneOrMore(item: GrammarItem, limit: Int, probability: Float): List<GrammarItem> {
        val output = mutableListOf<GrammarItem>()
        for (x in 0 until limit) {
            if (rnd.nextFloat() < probability) {
                output.add(makeNewItem(item))
            }
        }
        if (output.isEmpty()) {
            output.add(item)
        }
        return output
    }

    fun oneOrMore(item: GrammarItem, limit: Int, min:Int, probability: Float): List<GrammarItem> {
        val output = mutableListOf<GrammarItem>()
        for (y in 0 until min){
            output.add(makeNewItem(item))
        }
        for (x in 0 until limit-min) {
            if (rnd.nextFloat() < probability) {
                output.add(makeNewItem(item))
            }
        }
        if (output.isEmpty()) {
            output.add(item)
        }
        return output
    }


    private fun makeNewItem(item: GrammarItem): GrammarItem {
        if(item is Placeholder) return item::class.createInstance()
        val factory =  when (item) {
            is DungeonRoom -> Factories.roomFactory
            is Enemy -> Factories.enemyFactory
            is TrappedRoom -> Factories.trappedRoomFactory
            is Item -> Factories.itemFactory
            else -> throw FactoryNotFoundException(item::class)
        }
        return if (item.terminal){
            factory.terminal()
        } else {
            factory.nonTerminal()
        }
    }
}


fun GrammarItem.oneOrMore(limit: Int): List<GrammarItem> {
    return OneOrMore(Random).oneOrMore(this, limit, 0.5f)
}

class FactoryNotFoundException(clazz: KClass<out GrammarItem>) : RuntimeException("Could not find factory for $clazz")
