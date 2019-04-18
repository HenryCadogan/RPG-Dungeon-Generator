package grammar.grammarItems.factories

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.GrammarItemFactory
import grammar.grammarItems.Placeholder
import grammar.operators.OneOf
import grammar.grammarItems.treasure.items.*
import grammar.operators.oneOf
import kotlin.random.Random

class ItemsFactory(private val rnd: Random):GrammarItemFactory {

    override fun terminal(): GrammarItem {
        return randomItem()
    }

    override fun nonTerminal(): GrammarItem {
        return TreasurePlaceholder()
    }

    private val containersFile = this::class.java.getResource("/Items/containers.json")
    private val weaponsFile = this::class.java.getResource("/Items/weapons.json")
    private val miscFile = this::class.java.getResource("/Items/miscItems.json")
    private val keysAndLocksFile = this::class.java.getResource("/Items/keysandlocks.json")
    private val mapper = jacksonObjectMapper().enable(
            MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)

    private val containers = mapper.readValue(containersFile, Containers::class.java)
    private val weapons = mapper.readValue(weaponsFile, Weapons::class.java)
    private val miscItems = mapper.readValue(miscFile,MiscItems::class.java)
    private val keysAndLocks = mapper.readValue(keysAndLocksFile, KeysAndLocks::class.java)

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

    fun generateLockedContainerAndKey(size: ItemSize, type: ContainerCategory): GrammarItem {
        val container = generateContainer(size, type)

        val keyAndLock = keysAndLocks.items.oneOf()

        val vData = container.variantData
        val newData:ContainerVariant
        if (vData is ContainerVariant) {
            newData = ContainerVariant(
                    maxDC = vData.maxDC,
                    minDC = vData.minDC,
                    name = vData.name,
                    size = vData.size,
                    description = "${vData.description} This container is locked! ${keyAndLock.lock}")

        }else{
            throw RuntimeException("Variant data for Container object is not ContainerVariant, instead is ${vData::class}")
        }
        val lockedContainer = LockedContainer(data = newData )

        return LockedContainerAndKey(container = lockedContainer , key = keyAndLock.key)
    }

    fun generateMiscItem(type:MiscItemCategory): Item {
        val items = miscItems.miscItemTypes.find{it.name == type}
                ?: throw ItemTypeNotFoundException(type.toString())
        return MiscItem(OneOf(rnd).oneOf(items.variants).first())
    }

    fun randomItem():GrammarItem {
        val allItems = mutableListOf<Any>()
        containers.containerTypes.forEach { type ->
            allItems.addAll(type.variants)
        }
        weapons.weaponTypes.forEach { type ->
            allItems.addAll(type.variants)
        }
        miscItems.miscItemTypes.forEach { type ->
            allItems.addAll(type.variants)
        }
        val item = OneOf(rnd).oneOf(allItems).first()

        return when (item){
            is ContainerVariant -> Container(item)
            is WeaponVariant -> Weapon(item)
            is MiscItemVariant -> MiscItem(item)
            else -> throw RuntimeException("Generated Random Item is undefined")
        }
    }
}

data class Container(val data: ContainerVariant,
                     internal var contents: MutableList<GrammarItem> = mutableListOf(TreasurePlaceholder())
) : Item(ItemType.CONTAINER, data, data.name)

data class LockedContainer(val data: ContainerVariant,
                            internal var contents: List<GrammarItem> = listOf(TreasurePlaceholder())
) : Item(ItemType.CONTAINER, data, data.name)

data class Key(
        var description:String,
        val simpleName:String
):GrammarItem(true)

data class LockedContainerAndKey(
        val container: LockedContainer,
        val key: Key
): GrammarItem(true)

data class KeysAndLocks(
        val items:List<KeyAndLock>
) {
    data class KeyAndLock(
            val key:Key,
            val lock:String
    )
}

data class Weapon(val data: WeaponVariant) : Item(ItemType.WEAPON, data, data.name)
data class MiscItem(val data: MiscItemVariant) : Item(ItemType.MISC, data, data.name)


class TreasurePlaceholder: GrammarItem(false),Placeholder
class ItemPlaceholder: GrammarItem(false),Placeholder
class MiscItemPlaceholder: GrammarItem(false),Placeholder


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
