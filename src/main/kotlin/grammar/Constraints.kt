package grammar

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.enemies.Enemy
import grammar.grammarItems.enemies.Theme
import grammar.grammarItems.treasure.Item

object Constraints {
    val rooms = Rooms
    val enemies = Enemies

    object Rooms {
        var maxRoomCount = 0
        var roomSparsity = 0f
        var trappedRoomPercentage = 0
        var trappedContainerPercentage = 0
        var theme: Theme = Theme.RANDOM
    }

    object Enemies {
        var maxEnemiesPerRoom = 0
        var enemySparcity = 0f
    }
}
object Dungeon{
    val rooms = mutableListOf<GrammarItem>()
    val enemies = mutableListOf<Enemy>()
    val treasure = mutableListOf<Item>()
}