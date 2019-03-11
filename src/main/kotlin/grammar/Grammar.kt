package grammar

import grammar.grammarItems.and
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.StartItem
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.grammarItems.rooms.TrappedRoomFactory
import grammar.grammarItems.treasure.*
import grammar.operators.GrammarOperators
import kotlin.random.Random
import kotlin.reflect.KClass


class Grammar(private val rules: List<ProductionRule>) {

    fun generate(grammarItems: List<GrammarItem>): List<GrammarItem> {
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
        return workingSet
    }
}


class RuleNotFoundException(clazz: KClass<out GrammarItem>) : RuntimeException("Could not get a rule for $clazz")


fun main(args: Array<String>) {
    println("Starting")
    val rnd = Random
    val ops = GrammarOperators(rnd)
    val constraints = Constraints

    val roomGen = DungeonRoomFactory()
    val trappedRoomGen = TrappedRoomFactory()
    val itemGen = ItemsFactory(rnd)

    constraints.maxRoomCount = 20
    constraints.roomSparsity = 0.5f
    constraints.trapPercentage = 20
    val roomRules = listOf(
            ProductionRule(
                    lhs = DungeonRoom::class,
                    rhs = {
                        ops.oneOf.oneOf(mapOf(
                                roomGen.terminal() to 100 -constraints.trapPercentage ,
                                trappedRoomGen.terminal() to constraints.trapPercentage
                        ))
                    }
            ),
            ProductionRule(
                    lhs = StartItem::class,
                    rhs = { roomGen.entranceRoomToDungeon() and ops.oneOrMore.oneOrMore(roomGen.nonTerminal(),constraints.maxRoomCount, constraints.roomSparsity)
                    }
            )
    )

    val itemRules = listOf(
            ProductionRule(
                    lhs = ItemPlaceholder::class,
                    rhs = { ops.oneOf.oneOf(listOf(
                            itemGen.generateContainer(ops.oneOf.oneOf(ItemSize.values().toList()).first(), ContainerCategory.CHEST))
                    )
                    }
            )
    )


    val roomGrammar = Grammar(roomRules)
    val dungeon = roomGrammar.generate(listOf(StartItem()))

    val itemGrammar = Grammar(itemRules)

    println("Components are")
    for (d in dungeon){
        if (d is DungeonRoom){
            println("${d::class.simpleName}: ${d.description}")
            d.roomObjects = itemGrammar.generate(listOf(ItemPlaceholder()))
            val roomItems= mutableListOf<String>()
            for (o in d.roomObjects){
                if (o is Item)
                    if  (o.variantData is WeaponVariant){
                        roomItems.add(o.variantData.name)
                    } else if (o.variantData is ContainerVariant){
                        roomItems.add(o.variantData.name)
                    }
            }
            roomItems.forEach{
                println("           Items: $it")
            }


        }
    }

}


