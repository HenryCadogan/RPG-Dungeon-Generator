package grammar

import grammar.grammarItems.GrammarItem

object Constraints{
    var maxRoomCount= 0
    var roomSparsity = 0f
    var trapPercentage = 0
}

object Dungeon{
    val rooms = mutableListOf<GrammarItem>()
}