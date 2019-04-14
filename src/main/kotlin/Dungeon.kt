import display.dungeon.DungeonDrawer
import grammar.Constraints
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
import grammar.grammarItems.rooms.MapPosition
import grammar.grammarItems.rooms.getNearestRoom
import grammar.grammarItems.treasure.money.Money
import grammar.operators.oneOf
import java.awt.image.BufferedImage
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

class Dungeon {

    private var rooms = mutableListOf<GrammarItem>() 
    private val enemies = mutableListOf<Enemy>()
    private val locksAndKeys = mutableListOf<LockedContainerAndKey>()

    private val roomGrammar = Grammar(roomRules)
    private val itemGrammar = Grammar(itemRules)
    private val enemyGrammar = Grammar(enemyRules)
    private val lootGrammar = Grammar(lootRules)
    private val drawer = DungeonDrawer(Constraints.theme)

    private var plots= listOf<Plot>()
    private var plotSize = (Constraints.rooms.maxRoomSize * 1.5).roundToInt()



    fun getRooms() = rooms.map{it as DungeonRoom}

    private fun makeRoomTree(){
        val leaves = mutableListOf<DungeonRoom>()
        leaves.add(rooms.oneOf() as DungeonRoom)
        val unvisited = rooms.map { it as DungeonRoom } as MutableList<DungeonRoom>

        while(leaves.isNotEmpty()) {
            val toBeRemoved = mutableListOf<DungeonRoom>()
            for (i in 0 until leaves.size) {
                val leaf = leaves[i]
                toBeRemoved.add(leaf)
                if (leaf.rightChiLd != null && leaf.leftChiLd != null){break}

                val rc = leaf.getNearestRoom(unvisited)
                leaf.rightChiLd = rc
                leaves.add(rc)
                val unvisitedMinusRc = unvisited.filter { it.id != rc.id }
                if (unvisitedMinusRc.isEmpty()){break}

                if (Random.nextInt(1,100)<Constraints.rooms.connectivityThreshold) {
                    val lc = leaf.getNearestRoom(unvisitedMinusRc)
                    leaf.leftChiLd = lc
                    leaves.add(lc)
                }

                unvisited.remove(leaf)
            }
            leaves.removeAll(toBeRemoved)
        }
    }


    private fun determineLocations(){
        plots = calculatePlots(getRooms().size, plotSize)
        for (roomNumber in 0 until rooms.size){
            val room = rooms[roomNumber] as DungeonRoom
            val plot = plots[roomNumber]
            val buffer = Constraints.rooms.minroomDistance
            val x = plot.x + buffer + Random.nextInt((1..plot.width - room.size.width))
            val y = plot.y + buffer + Random.nextInt((1..plot.height - room.size.height))
            room.position = MapPosition(x.toInt(), y.toInt())
        }
    }

    private fun calculatePlots(roomCount: Int, plotSize: Int): List<Plot> {
        val numPlots = if (roomCount <=5) {
            roomCount
        } else {
            roomCount/2
        }

        val plots = mutableListOf<Plot>()
        var index = 0
        for (y in 0 until numPlots) {
            for (x in 0 until numPlots) {
                index++
                plots.add(Plot(index, x * plotSize, y * plotSize, plotSize, plotSize))
                println("Made plot at ${x * plotSize} ${y * plotSize} with index $index")
            }
        }

        val plotsToUse = mutableListOf<Plot>()
        for (n in 1..roomCount) {
            plots.shuffle()
            plotsToUse.add(plots[0])
            plots.remove(plots[0])
        }
        return plotsToUse.sortedBy { it.index }
    }

    fun draw(): BufferedImage {
        val imageSize = (plots.size * plotSize)
        return drawer.drawDungeon(this,imageSize)
    }



    fun generate(): String {
        //todo split this into generation and then description in two functions
        val sb = StringBuilder()

        rooms = roomGrammar.generate(listOf(StartItem())).map{it as DungeonRoom}.toMutableList()
        for (x in 0 until rooms.size){
            val r = rooms[x] as DungeonRoom
            r.id = x.toString()
        }

        determineLocations()
        makeRoomTree()

        sb.appendln("Components are")

        for (d in rooms) {
            d as DungeonRoom
            sb.appendln("Room ${d.id}: ${d.description}")
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

data class Plot(
        val index : Int,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
)

