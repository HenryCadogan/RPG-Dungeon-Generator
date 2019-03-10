package grammar.grammarItems.rooms

import grammar.grammarItems.treasure.Item
import grammar.grammarItems.treasure.ItemSize

open class RoomObject (
        val position: MapPosition,
        val name:String,
        val description:String,
        val size: ItemSize,
        val contents: MutableList<Item>
){
    private var full = false

    fun isFull() = full

    fun addContents(item: Item){
        full = true
        contents.add(item)
    }
}

data class MapPosition(
        val x:Int,
        val y:Int
)

