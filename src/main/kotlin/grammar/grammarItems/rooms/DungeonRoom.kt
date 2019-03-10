package grammar.grammarItems.rooms

import generator.Monster
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.GrammarItemGenerator
import grammar.grammarItems.treasure.Item
import grammar.grammarItems.treasure.ItemSize
import grammar.grammarItems.treasure.ItemsFactory
import kotlin.random.Random

open class DungeonRoom(
        terminal: Boolean = false
) : GrammarItem(terminal) {
    lateinit var position: MapPosition
    private val roomEnemies = mutableListOf<Monster>()
    var trapped: Boolean = false
    private val roomObjects = mutableListOf<RoomObject>()
    private val looseItems= mutableListOf<Item>()
    open var description: String = "A room"

    fun isTrapped() = trapped


    fun addEnemies(monsters: List<Monster>) {
        roomEnemies.addAll(monsters)
    }

    fun addObjects(objects: List<RoomObject>) {
        roomObjects.addAll(objects)
    }

    fun addLootIntoObjects(loot: List<Item>) {
        loot.forEach { li ->
            var placed = false
            roomObjects.forEach { ro ->
                if (!ro.isFull()) {
                    if (canFit(li, ro)) {
                        ro.addContents(li)
                        placed = true
                    }
                }
            }
            if (!placed){looseItems.add(li)}
        }
    }

    private fun canFit(item: Item, roomObject: RoomObject): Boolean {
        return when (roomObject.size) {
            ItemSize.SMALL -> item.size == ItemSize.SMALL
            ItemSize.MEDIUM -> item.size in listOf(ItemSize.MEDIUM, ItemSize.SMALL)
            ItemSize.LARGE -> item.size in listOf(ItemSize.LARGE, ItemSize.MEDIUM, ItemSize.SMALL)
        }
    }
}


class DungeonRoomGenerator : GrammarItemGenerator {

    //todo use objects to determine placement of room and generate some contents.

    override fun nonTerminal(): GrammarItem {
        return DungeonRoom(false)
    }

    override fun terminal(): GrammarItem {
        return DungeonRoom(true)
    }

}





