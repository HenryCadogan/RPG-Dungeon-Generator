package grammar

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.operators.GrammarOperators
import kotlin.random.Random
import kotlin.reflect.KClass



class Grammar(private val rules:List<ProductionRule>) {

    fun generate(grammarItems: List<GrammarItem>): MutableList<GrammarItem> {
        val outputItems = mutableListOf<GrammarItem>()
        var finished = false
        while (!finished) {
            for (x in 0 until grammarItems.size) {
                if (!grammarItems[x].terminal) {
                    val clazz = grammarItems[x]::class
                    val rule = rules.find { it.lhs == clazz }?: throw RuleNotFoundException(clazz)
                    val elementsToAdd = rule.rhs.invoke()
                    outputItems.addAll(elementsToAdd)
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
    val rnd = Random
    val ops = GrammarOperators(rnd)
//    val constraints = Constraints
//    constraints.maxRoomCount = 20
//    constraints.roomSparsity = 0.5f
//    constraints.trapPercentage = 100
//    val roomFactory = DungeonRoomFactory()
//    val roomRules = listOf(
//            ProductionRule(
//                    lhs = DungeonRoom::class,
//                    rhs = {
//                        ops.oneOrMore.oneOrMore(
//                                {ops.oneOf.oneOf(listOf(
//                                        roomFactory.nonTerminal(),
//                                        DungeonRoom.trapped()
//                                ))},
//                                Constraints.maxRoomCount, Constraints.roomSparsity)
//                    }
//            )
//    )



    val rules = listOf(
            ProductionRule(
                    lhs = a::class,
                    rhs = {ops.oneOrMore.oneOrMore(
                            {ops.oneOf.oneOf(listOf(
                            a("firstone",true),
                            a("secondOne",true)
                    ))},5,100f)}
            )
    )

    val g = Grammar(rules)
    val b = g.generate(listOf(a("start",false)))
    for (item in b){
        if (item is a)
        println(item.name)
    }

}

class a(val name:String, terminal:Boolean):GrammarItem(terminal)
