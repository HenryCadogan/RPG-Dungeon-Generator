package grammar.grammarItems.treasure.items

import grammar.grammarItems.factories.ItemSize
import grammar.grammarItems.treasure.money.Money


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
