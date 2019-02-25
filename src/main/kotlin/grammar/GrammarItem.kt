package grammar

import generator.Monster


interface Generateable{
 fun generate():GrammarItem
}


abstract class GrammarItem(
        val terminal : Boolean
): Generateable

class DungeonRoom(
        terminal: Boolean,
        private val position: Boolean,
        private val enemies: List<Monster>,
        private val trapped:Boolean = false,
        private val loot:Boolean = false
 ) : GrammarItem(terminal) {
 override fun generate(): DungeonRoom {
  //todo Make this generate based on the constraints.
  return DungeonRoom(terminal, position, enemies)
 }
}