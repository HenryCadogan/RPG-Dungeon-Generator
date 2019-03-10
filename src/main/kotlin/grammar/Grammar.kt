package grammar

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.StartItem
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomGenerator
import grammar.grammarItems.rooms.TrappedRoomGenerator
import grammar.grammarItems.treasure.Item
import grammar.grammarItems.treasure.ItemSize
import grammar.grammarItems.treasure.ItemsFactory
import grammar.grammarItems.treasure.WEAPON
import grammar.operators.GrammarOperators
import grammar.operators.OneOf
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses


class Grammar(private val rules: List<ProductionRule>) {

    fun generate(grammarItems: List<GrammarItem>): List<GrammarItem> {
        println("Generating")
        var outputItems = mutableListOf<GrammarItem>()
        var finished = false
        var workingSet = grammarItems
        while (!finished) {
            for (x in 0 until workingSet.size) {
                if (!workingSet[x].terminal) {
                    val clazz = workingSet[x]::class
                    val rule = rules.find { it.lhs == clazz } ?: throw RuleNotFoundException(clazz)
                    val elementsToAdd = rule.rhs.invoke()
                    outputItems.addAll(elementsToAdd)
                } else {
                    outputItems.add(workingSet[x])
                }
            }
            finished = !workingSet.any { !it.terminal }
            workingSet = outputItems
            outputItems = mutableListOf()
        }
        println("Generation Complete")
        return workingSet
    }
}


class RuleNotFoundException(clazz: KClass<out GrammarItem>) : RuntimeException("Could not get a rule for $clazz")


fun main(args: Array<String>) {
    println("Starting")
    val rnd = Random
    val ops = GrammarOperators(rnd)
    val constraints = Constraints

    val roomGenerator = DungeonRoomGenerator()
    val trappedRoomGen = TrappedRoomGenerator()

    constraints.maxRoomCount = 20
    constraints.roomSparsity = 0.5f
    constraints.trapPercentage = 100
    val roomRules = listOf(
            ProductionRule(
                    lhs = DungeonRoom::class,
                    rhs = {
                        ops.oneOf.oneOf(listOf(
                                roomGenerator.terminal(),
                                trappedRoomGen.terminal()
                        ))
                    }
            ),
            ProductionRule(
                    lhs = StartItem::class,
                    rhs = {
                        ops.oneOrMore.oneOrMore(roomGenerator.nonTerminal(),constraints.maxRoomCount, constraints.roomSparsity)
                    }
            )
    )

    val g = Grammar(roomRules)
    val dungeon = g.generate(listOf(StartItem()))

    println("Components are")
    for (d in dungeon){
        if (d is DungeonRoom){
            println("${d::class.simpleName} trapped=${d.trapped}")
        }
    }

}


