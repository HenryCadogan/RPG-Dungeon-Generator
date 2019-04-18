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
    internal val enemies = mutableListOf<Enemy>()
    internal val locksAndKeys = mutableListOf<LockedContainerAndKey>()

    private val roomGrammar = Grammar(roomRules)
    private val itemGrammar = Grammar(itemRules)
    private val enemyGrammar = Grammar(enemyRules)
    private val lootGrammar = Grammar(lootRules)
    private val drawer = DungeonDrawer(Constraints.theme)

    private var plots = listOf<Plot>()
    private var plotSize = (Constraints.rooms.maxRoomSize * 1.5).roundToInt()

    fun getRooms() = rooms.map { it as DungeonRoom }

    private fun makeRoomTree() {
        val leaves = mutableListOf<DungeonRoom>()
        leaves.add(rooms.oneOf() as DungeonRoom)
        val unvisited = rooms.map { it as DungeonRoom } as MutableList<DungeonRoom>

        while (leaves.isNotEmpty()) {
            val toBeRemoved = mutableListOf<DungeonRoom>()
            for (i in 0 until leaves.size) {
                val leaf = leaves[i]
                toBeRemoved.add(leaf)
                if (leaf.rightChiLd != null && leaf.leftChiLd != null) {
                    break
                }

                val rc = leaf.getNearestRoom(unvisited)
                leaf.rightChiLd = rc
                leaves.add(rc)
                val unvisitedMinusRc = unvisited.filter { it.id != rc.id }
                if (unvisitedMinusRc.isEmpty()) {
                    break
                }

                if (Random.nextInt(1, 100) < Constraints.rooms.connectivityThreshold) {
                    val lc = leaf.getNearestRoom(unvisitedMinusRc)
                    leaf.leftChiLd = lc
                    leaves.add(lc)
                }

                unvisited.remove(leaf)
            }
            leaves.removeAll(toBeRemoved)
        }
    }


    private fun determineLocations() {
        plots = calculatePlots(getRooms().size, plotSize)
        for (roomNumber in 0 until rooms.size) {
            val room = rooms[roomNumber] as DungeonRoom
            val plot = plots[roomNumber]
            val buffer = Constraints.rooms.minroomDistance
            val x = plot.x + buffer + Random.nextInt((1..plot.width - room.size.width))
            val y = plot.y + buffer + Random.nextInt((1..plot.height - room.size.height))
            room.position = MapPosition(x.toInt(), y.toInt())
        }
    }

    private fun calculatePlots(roomCount: Int, plotSize: Int): List<Plot> {
        val numPlots = if (roomCount <= 5) {
            roomCount
        } else {
            roomCount / 2
        }

        val plots = mutableListOf<Plot>()
        var index = 0
        for (y in 0 until numPlots) {
            for (x in 0 until numPlots) {
                index++
                plots.add(Plot(index, x * plotSize, y * plotSize, plotSize, plotSize))
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

    private fun moveKeys() {
        getRooms().forEach { room ->
            val lockedItems = room.roomObjects.filter { it is LockedContainerAndKey }.map { it as LockedContainerAndKey }
            if (lockedItems.isNotEmpty()) {
                var newRoomID: String
                for (item in lockedItems) {
                    val roomToMoveTo = getRooms().filter { it != room }.oneOf()
                    val roomContainers = roomToMoveTo.roomObjects.filter { it is Container }
                    if (roomContainers.isNotEmpty()) {
                        val c = roomContainers.oneOf() as Container
                        c.contents.add(item.key)
                        item.key.description = item.key.description.plus("This unlocks the ${item.container.data.name} in room ${room.id}. ")

                    } else {
                        //todo make a method to hide things randomly or some such
                        roomToMoveTo.roomObjects.add(item.key)
                        item.key.description = item.key.description.plus("This unlocks the ${item.container.data.name} in room ${room.id}, it is hidden somewhere in this room. Use your imagination as to where and the difficulty. ")

                    }
                    newRoomID = roomToMoveTo.id
                    item.container.data.description = item.container.data.description.plus("The key for this is the ${item.key.simpleName} in room $newRoomID. ")
                }

            }

        }
    }

    fun draw(): BufferedImage {
        val imageSize = (plots.size * plotSize)
        return drawer.drawDungeon(this, imageSize)
    }


    fun generate(): String {
        //todo split this into generation and then description in two functions
        generateContents()
        determineLocations()
        makeRoomTree()
        moveKeys()
        val descriptionCreator = DescriptionCreator(this)
        return descriptionCreator.generateDescription()
    }


    private fun generateContents() {
        rooms = roomGrammar.generate(listOf(StartItem())).map { it as DungeonRoom }.toMutableList()
        for (x in 0 until rooms.size) {
            val r = rooms[x] as DungeonRoom
            r.id = x.toString()
        }

        for (d in getRooms()) {
            d.roomObjects = itemGrammar.generate(listOf(ItemPlaceholder())).toMutableList()

            if (d.roomObjects.isNotEmpty()) {
                d.roomObjects.forEach {
                    when (it) {
                        is Container -> {
                            it.contents = lootGrammar.generate(it.contents).toMutableList()
                        }
                        is LockedContainerAndKey -> {
                            it.container.contents = lootGrammar.generate(it.container.contents)
                        }
                    }
                }
            }

            val enemiesForRoom = enemyGrammar.generate(listOf(EnemyPlaceholder())).map { it as Enemy }
            d.roomEnemies = enemiesForRoom
            enemies.addAll(enemiesForRoom)
        }
    }

}

data class Plot(
        val index: Int,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
)

