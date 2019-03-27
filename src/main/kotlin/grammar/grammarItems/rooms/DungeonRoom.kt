package grammar.grammarItems.rooms

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.Enemy
import kotlin.random.Random
import kotlin.random.nextInt

open class DungeonRoom(
        terminal: Boolean = false
) : GrammarItem(terminal) {
    var size = RoomSize(
            height = Random.nextInt((70..100)),
            width = Random.nextInt((70..100 ))
    )
    var roomEnemies = listOf<Enemy>()
    internal var trapped: Boolean = false
    var roomObjects = listOf<GrammarItem>()
    open lateinit var description: String
}

data class RoomSize(
        val width: Int,
        val height: Int
)

data class MapPosition(
        val x: Int,
        val y: Int
)






