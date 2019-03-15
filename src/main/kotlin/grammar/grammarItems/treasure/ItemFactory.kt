package grammar.grammarItems.treasure

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import grammar.grammarItems.GrammarItem
import grammar.operators.OneOf
import grammar.grammarItems.treasure.items.*
import kotlin.random.Random

class ItemsFactory(private val rnd: Random) {
    private val containersFile = this::class.java.getResource("/Items/containers.json")
    private val weaponsFile = this::class.java.getResource("/Items/weapons.json")
    private val miscFile = this::class.java.getResource("/Items/miscItems.json")
    private val mapper = jacksonObjectMapper().enable(
            MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)

    private val containers = mapper.readValue(containersFile, Containers::class.java)
    private val weapons = mapper.readValue(weaponsFile, Weapons::class.java)
    private val miscItems = mapper.readValue(miscFile,MiscItems::class.java)

    fun generateWeapon(size: ItemSize, type: WeaponCategory): Item {
        val items = weapons.weaponTypes.find { it.name == type }
                ?: throw ItemTypeNotFoundException(type.toString())
        val possibleWeapons = items.variants.filter { it.size == size }
        if (possibleWeapons.isEmpty()) throw ItemNotFoundException(size, type.toString())
        return Weapon(OneOf(rnd).oneOf(possibleWeapons).first())
    }

    fun generateContainer(size: ItemSize, type: ContainerCategory): Item {
        val items = containers.containerTypes.find { it.name == type }
                ?: throw ItemTypeNotFoundException(type.toString())
        val possibleContainers = items.variants.filter { it.size == size }
        if (possibleContainers.isEmpty()) throw ItemNotFoundException(size, type.toString())
        return Container(OneOf(rnd).oneOf(possibleContainers).first())
    }

    fun generateMiscItem(type:MiscItemCategory):Item {
        val items = miscItems.miscItemTypes.find{it.name == type}
                ?: throw ItemTypeNotFoundException(type.toString())
        return MiscItem(OneOf(rnd).oneOf(items.variants).first())
    }
}

data class Container(val data: ContainerVariant, var contents: List<GrammarItem> = listOf(TreasurePlaceholder())) : Item(ItemType.CONTAINER, data, data.name)
data class Weapon(val data: WeaponVariant) : Item(ItemType.WEAPON, data, data.name)
data class MiscItem(val data: MiscItemVariant) : Item(ItemType.MISC, data, data.name)


class TreasurePlaceholder:GrammarItem(false)
class ItemPlaceholder : GrammarItem(false)

open class Item(
        val itemType: ItemType,
        val variantData: Any,
        val name: String
) : GrammarItem(true)

enum class ItemType {
    WEAPON,
    CONTAINER,
    MISC
}

enum class ItemSize {
    LARGE,
    MEDIUM,
    SMALL
}

class ItemTypeNotFoundException(itemType: String) : RuntimeException("Could not find an item in the collection with type $itemType, please add it and populate it with an item of each size")
class ItemNotFoundException(size: ItemSize, type: String) : RuntimeException("Could not find a $size $type in the collection, please add it or change the production rules")
