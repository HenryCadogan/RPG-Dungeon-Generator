package grammar.grammarItems.treasure.items

import grammar.grammarItems.factories.ItemSize
import grammar.grammarItems.treasure.money.Money

enum class WeaponCategory {
    SWORD,
    AXE,
    WAND
}

data class Weapons(
        val weaponTypes: List<WeaponType>
)

data class WeaponType(
        val name: WeaponCategory,
        val variants: List<WeaponVariant>
)

data class WeaponVariant(
        val name: String,
        val value: Money,
        val size: ItemSize,
        val damage: String,
        val description: String
)