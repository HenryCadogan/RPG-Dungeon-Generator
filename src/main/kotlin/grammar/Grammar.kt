package grammar

import Dungeon
import display.dungeon.DungeonDrawer
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.EnemyFactory
import grammar.grammarItems.factories.DungeonRoomFactory
import grammar.grammarItems.factories.ItemsFactory
import grammar.grammarItems.factories.TrappedRoomFactory
import theme.Theme
import java.io.File
import javax.imageio.ImageIO
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
    Constraints.theme = Theme.AQUATIC
    Factories.enemyFactory = EnemyFactory(Constraints.theme)
    Factories.itemFactory = ItemsFactory(rnd)
    Factories.trappedRoomFactory = TrappedRoomFactory()
    Factories.roomFactory = DungeonRoomFactory(Constraints.theme)
    Constraints.rooms.maxRoomCount = 20
    Constraints.rooms.roomSparsity = 0.5f
    Constraints.rooms.trappedRoomPercentage = 20
    Constraints.enemies.maxEnemiesPerRoom = 5
    Constraints.enemies.enemySparsity = 0.2f
    val dungeon = Dungeon()
    val drawer = DungeonDrawer(Constraints.theme)
    val output = dungeon.generate()

    ImageIO.write(drawer.drawDungeon(dungeon),"png",File("generator.Dungeon.png"))
    println(output)
    //todo make function to write out description of a grammar item (could be railrec)

}


