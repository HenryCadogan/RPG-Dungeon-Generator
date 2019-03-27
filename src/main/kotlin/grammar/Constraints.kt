package grammar

import grammar.grammarItems.enemies.EnemyFactory
import grammar.grammarItems.factories.DungeonRoomFactory
import grammar.grammarItems.factories.ItemsFactory
import grammar.grammarItems.factories.TrappedRoomFactory
import theme.Theme

object Constraints {
    val rooms = Rooms
    val enemies = Enemies
    var theme: Theme = Theme.RANDOM

    object Rooms {
        var maxRoomCount = 20
        var roomSparsity = 0.7f
        var trappedRoomPercentage = 20
        var maxRoomSize = 100
        var roomDistance = 1f
    }

    object Enemies {
        var maxEnemiesPerRoom = 7
        var enemySparsity = 0.5f
    }
}

object Factories {
    lateinit var enemyFactory: EnemyFactory
    lateinit var itemFactory: ItemsFactory
    lateinit var roomFactory: DungeonRoomFactory
    lateinit var trappedRoomFactory: TrappedRoomFactory
}
