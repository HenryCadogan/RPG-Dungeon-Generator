package grammar.grammarItems.treasure.items

import grammar.grammarItems.treasure.ItemSize

enum class ContainerCategory {
    CHEST
}

data class Containers(
        val containerTypes: List<ContainerType>
)

data class ContainerType(
        val name: ContainerCategory,
        val variants: List<ContainerVariant>
)

data class ContainerVariant(
        val maxDC: Int,
        val minDC: Int,
        val name: String,
        val size: ItemSize,
        val description: String
)
