package grammar

<<<<<<< HEAD
import grammar.grammarItems.DungeonRoom
import grammar.grammarItems.DungeonRoomFactory
import grammar.grammarItems.GrammarItem
=======
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.grammarItems.treasure.*
>>>>>>> a488bd547116d9ed726abb15d36d84fed6fc6238
import grammar.operators.GrammarOperators
import grammar.operators.OneOf
import java.awt.Container
import kotlin.random.Random
import kotlin.reflect.KClass


data class GrammarRules(
        val rules: List<ProductionRule>
)

class Grammar(private val rules:GrammarRules) {

    fun generate(grammarItems: List<GrammarItem>): MutableList<GrammarItem> {
        val outputItems = mutableListOf<GrammarItem>()
        var finished = false
        while (!finished) {
            for (x in 0 until grammarItems.size) {
                if (!grammarItems[x].terminal) {
                    val clazz = grammarItems[x]::class
                    val rule = rules.rules.find { it.lhs == clazz }?: throw RuleNotFoundException(clazz)
                    val elementsToAdd = rule.rhs.invoke()
                    outputItems.addAll(elementsToAdd)
                    println("Adding $elementsToAdd")
                }else{
                    outputItems.add(grammarItems[x])
                }
                finished = grammarItems.any { !it.terminal }
            }
        }
        return outputItems
    }
}


class RuleNotFoundException(clazz: KClass<out GrammarItem>):RuntimeException("Could not get a rule for $clazz")


fun main(args: Array<String>) {

<<<<<<< HEAD
    val g = Grammar(rules)
    val output = g.generate(listOf(roomFactory.nonTerminal()))
    print(output)
}
=======
    val itemsFactory = ItemsFactory(Random)
>>>>>>> a488bd547116d9ed726abb15d36d84fed6fc6238

    val ops = GrammarOperators(Random)

    for (x in 1..10) {
        val size = ops.oneOf.oneOf(ItemSize.values().toList())
        val weaponType = ops.oneOf.oneOf(WEAPON.values().toList())
        println(itemsFactory.generateWeapon(size, weaponType))
    }
}
