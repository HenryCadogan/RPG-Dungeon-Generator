package grammar

import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.grammarItems.treasure.*
import grammar.operators.GrammarOperators
import java.awt.Container
import kotlin.random.Random
import kotlin.reflect.KClass


data class GrammarRules(
        val rules: List<ProductionRule>
)

class Grammar(private val rules:GrammarRules) {

    fun generate(grammarItems: MutableList<GrammarItem>): MutableList<GrammarItem> {
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

    val itemsFactory = ItemsFactory(Random)
    println(itemsFactory.generateContainer(ItemSize.MEDIUM,CONTAINER.CHEST))
    println(itemsFactory.generateWeapon(ItemSize.MEDIUM,WEAPON.AXE))
}
