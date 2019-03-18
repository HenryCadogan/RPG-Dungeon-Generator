package grammar

import grammar.Factories.enemyFactory
import grammar.Factories.itemFactory
import grammar.Factories.roomFactory
import grammar.Factories.trappedRoomFactory
import grammar.grammarItems.and
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.StartItem
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.enemies.EnemyFactory
import grammar.grammarItems.enemies.EnemyPlaceholder
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.DungeonRoomFactory
import grammar.grammarItems.rooms.TrappedRoomFactory
import grammar.grammarItems.treasure.*
import grammar.grammarItems.treasure.items.*
import grammar.grammarItems.treasure.money.Money
import grammar.operators.GrammarOperators
import grammar.operators.oneOf
import grammar.operators.or
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
    val const = Constraints

    Factories.enemyFactory = EnemyFactory(const.theme)
    Factories.itemFactory = ItemsFactory(rnd)
    Factories.trappedRoomFactory = TrappedRoomFactory()
    Factories.roomFactory = DungeonRoomFactory(const.theme)

    const.rooms.maxRoomCount = 20
    const.rooms.roomSparsity = 0.5f
    const.rooms.trappedRoomPercentage = 20
    const.enemies.maxEnemiesPerRoom = 5
    const.enemies.enemySparcity = 0.2f

    val roomRules = listOf(
            ProductionRule(
                    lhs = DungeonRoom::class,
                    rhs = {
                        ops.oneOf.oneOf(mapOf(
                                roomFactory.terminal() to 100 - const.rooms.trappedRoomPercentage,
                                trappedRoomFactory.terminal() to const.rooms.trappedRoomPercentage
                        ))
                    }
            ),
            ProductionRule(
                    lhs = StartItem::class,
                    rhs = {
                        roomFactory.entranceRoomToDungeon() and ops.oneOrMore.oneOrMore(
                                roomFactory.nonTerminal(), const.rooms.maxRoomCount, const.rooms.roomSparsity)
                    }
            )
    )

    val itemRules = listOf(
            ProductionRule(
                    lhs = ItemPlaceholder::class,
                    rhs = {
                        itemFactory.generateContainer(
                                size = ItemSize.values().toList().oneOf(),
                                type = ContainerCategory.CHEST) /*.oneOrMore(2)*/ or
                                itemFactory.generateWeapon(
                                        size = ItemSize.SMALL,
                                        type = WeaponCategory.WAND
                                )
                    }
            )
    )

    val enemyRules = listOf(
            ProductionRule(
                    lhs = EnemyPlaceholder::class,
                    rhs = {
                        ops.oneOrMore.oneOrMore(
                                item = (enemyFactory.terminal() or enemyFactory.nonTerminal()).first(),
                                limit = const.enemies.maxEnemiesPerRoom,
                                probability = const.enemies.enemySparcity
                        )
                    }
            ),
            ProductionRule(
                    lhs = Enemy::class,
                    rhs = {
                        listOf(enemyFactory.terminal()) or emptyList()
                    }
            )
    )

    val lootRules = listOf(
            ProductionRule(
                    lhs = TreasurePlaceholder::class,
                    rhs = {
                        ((Money.lowValue() and MiscItemPlaceholder()) or (ops.oneOrMore.oneOrMore(MiscItemPlaceholder(), 3, 70f)) or emptyList())
                    }
            ),
            ProductionRule(
                    lhs = MiscItemPlaceholder::class,
                    rhs = {
                        (itemFactory.generateMiscItem(MiscItemCategory.values().toList().oneOf()) or emptyList() or Money.moderateValue())
                    }
            )
    )


    val roomGrammar = Grammar(roomRules)
    val itemGrammar = Grammar(itemRules)
    val enemyGrammar = Grammar(enemyRules)
    val lootGrammar = Grammar(lootRules)
    Dungeon.rooms = roomGrammar.generate(listOf(StartItem())).toMutableList()

    println("Components are")
    for (d in Dungeon.rooms) {
        if (d is DungeonRoom) {
            println(d.description)
            d.roomObjects = itemGrammar.generate(listOf(ItemPlaceholder()))
            val roomItems = mutableListOf<Item>()
            for (o in d.roomObjects) {
                if (o is Item)
                    when {
                        o.variantData is WeaponVariant ||
                                o.variantData is ContainerVariant ||
                                o.variantData is MiscItemVariant
                        -> roomItems.add(o)
                    }
            }
            val enemiesForRoom = enemyGrammar.generate(listOf(EnemyPlaceholder())).map{it as Enemy}
            d.roomEnemies = enemiesForRoom
            Dungeon.enemies.addAll(enemiesForRoom)
            println("   Enemies: ")
            enemiesForRoom.forEach {
                println("       ${it.data.name}")
            }
            if (roomItems.isNotEmpty()) {
                println("   Items:")
                roomItems.forEach {
                    when (it) {
                        is Weapon -> println("      ${it.name}")
                        is Container -> {
                            it.contents = lootGrammar.generate(it.contents)
                            val name = it.data.name
                            if (!it.contents.isEmpty()) {
                                println("       $name")

                                val items = it.contents.map { i ->
                                    when (i) {
                                        is Item -> i.name
                                        is Money -> i.toPrettyString()
                                        else -> ""
                                    }
                                }
                                println("           Containing: $items")
                            } else {
                                println("       An empty $name")
                            }
                        }
                    }
                }
            }
        }
    }


    //todo make function to write out description of a grammar item (could be railrec)


}


