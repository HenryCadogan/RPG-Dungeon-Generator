package grammar

import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.grammarItems.treasure.LockedTreasureItem
import grammar.operators.GrammarOperators
import kotlin.random.Random
import kotlin.reflect.KClass

object Constraints{

}

object Dungeon{
    val rooms = listOf<GrammarItem>()
}

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
                }
                finished = grammarItems.any { !it.terminal }
            }
        }
        return outputItems
    }
}


fun main(args: Array<String>) {
    val ops = GrammarOperators(Random)

    val roomFactory = DungeonRoomFactory()

    val a = {ops.oneOrMore.oneOrMore(roomFactory.terminal(),3,0.5f)}

    val rules = GrammarRules(listOf(
            ProductionRule(lhs = DungeonRoom::class, rhs= a )
    ))

    val g = Grammar(rules)
    val output = g.generate(mutableListOf(roomFactory.nonTerminal()))
    print(output)
}


class RuleNotFoundException(clazz: KClass<out GrammarItem>):RuntimeException("Could not get a rule for $clazz")