import grammar.Grammar
import grammar.Rules.enemyRules
import grammar.Rules.itemRules
import grammar.Rules.lootRules
import grammar.Rules.roomRules
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.StartItem
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.enemies.EnemyPlaceholder
import grammar.grammarItems.factories.*
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.treasure.money.Money

class Dungeon {

    private var rooms = mutableListOf<GrammarItem>() 
    private val enemies = mutableListOf<Enemy>()
    private val locksAndKeys = mutableListOf<LockedContainerAndKey>()


    private val roomGrammar = Grammar(roomRules)
    private val itemGrammar = Grammar(itemRules)
    private val enemyGrammar = Grammar(enemyRules)
    private val lootGrammar = Grammar(lootRules)

    fun getRooms() = rooms

    fun generate(): String {
        //todo split this into generation and then description in two functions
        val sb = StringBuilder()
        rooms = roomGrammar.generate(listOf(StartItem())).toMutableList()

        sb.appendln("Components are")
        var dungeonRoomCount = 0
        for (d in rooms) {
            dungeonRoomCount++
            d as DungeonRoom
            sb.appendln("Room $dungeonRoomCount: ${d.description}")
            d.roomObjects = itemGrammar.generate(listOf(ItemPlaceholder()))

            val enemiesForRoom = enemyGrammar.generate(listOf(EnemyPlaceholder())).map { it as Enemy }
            d.roomEnemies = enemiesForRoom
            enemies.addAll(enemiesForRoom)
            if (enemiesForRoom.isNotEmpty()) {
                sb.appendln("   Enemies:")
                enemiesForRoom.forEach {
                    sb.appendln("       ${it.data.name}")
                }
            }
            if (d.roomObjects.isNotEmpty()) {
                sb.appendln("   Items:")
                d.roomObjects.forEach {
                    when (it) {
                        is Container -> {
                            it.contents = lootGrammar.generate(it.contents)
                            val name = it.data.name
                            if (!it.contents.isEmpty()) {
                                sb.appendln("       This room has $name in it")

                                val items = it.contents.map { i ->
                                    when (i) {
                                        is Item -> i.name
                                        is Money -> i.toPrettyString()
                                        else -> ""
                                    }
                                }
                                sb.appendln("         Containing: $items")
                            } else {
                                sb.appendln("         An empty $name")
                            }
                        }
                        is LockedContainerAndKey -> {
                            locksAndKeys.add(it)
                            //todo place key somewhere else and reference it here...
                            sb.appendln("         ${it.container.data.description}")
                            sb.appendln("         The containers Key is ${it.key.description}")
                        }
                        is Weapon -> {
                            sb.appendln("         ${it.data.name}")
                            sb.appendln("               Damage: ${it.data.damage}")
                            sb.appendln("               Value: ${it.data.value.toPrettyString()}")
                            sb.appendln("               Description: ${it.data.description}")
                        }
                    }
                }
            }
            sb.appendln("----------------------------------------------------------------")
        }
        val monsterAppendix = makeEnemyAppendix()
        return sb.appendln(monsterAppendix).toString()
    }

    private fun makeEnemyAppendix(): String {
        val sb = StringBuilder()
        sb.appendln("Details for enemies in this dungeon:")
        enemies.distinctBy { it.data.name }.forEach {
            val data = it.data
            sb.appendln("Creature Name = ${data.name}\n")
            sb.appendln("Full information can be found on page ${data.pageNum} of the monster manual\n")
            sb.appendln("Armour Rating = ${data.armour}\n")
            sb.appendln("Max Health = ${data.health}\n")
            //todo change enemies to be single constructor that takes a list of Attack objects then change this to iterate over the list
            sb.appendln("Primary Attack = ${data.firstAttackName} dealing: ${data.firstAttackDamage}\n")
            if (data.secondAttackName != null) {
                sb.appendln("Secondary Attack = ${data.secondAttackName} dealing: ${data.secondAttackDamage}\n")
            }
            sb.appendln("----------------------------------------------------------------")
        }
        return sb.toString()
    }


}

