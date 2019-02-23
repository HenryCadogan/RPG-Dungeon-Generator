package grammar

import generator.Monster

abstract class GrammarItem(
        val terminal : Boolean
)

class DungeonRoom(
        terminal: Boolean,
        val position: Boolean,
        val enemies: List<Monster>
        //val loot: List<LootItem>
 ) : GrammarItem(terminal) {

}