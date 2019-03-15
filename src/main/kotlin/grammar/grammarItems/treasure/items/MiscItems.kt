package grammar.grammarItems.treasure.items

import grammar.grammarItems.GrammarItem
import grammar.grammarItems.treasure.ItemSize
import grammar.grammarItems.treasure.money.Money

class MiscItemPlaceholder: GrammarItem(false)

enum class MiscItemCategory {
    POTION,
    SPELLSCROLL
}

data class MiscItems(
        val miscItemTypes: List<MiscItemType>
)

data class MiscItemType(
        val name: MiscItemCategory,
        val variants: List<MiscItemVariant>
)

data class MiscItemVariant(
        val name: String,
        val value: Money,
        val description: String,
        val size: ItemSize
)
