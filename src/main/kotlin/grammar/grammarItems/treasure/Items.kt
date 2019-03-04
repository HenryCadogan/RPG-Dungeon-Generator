package grammar.grammarItems.treasure

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import grammar.operators.OneOf
import kotlin.random.Random


class ItemsFactory(rnd: Random){
    private val itemsFile = this::class.java.getResource("Items/items.json")
    private val mapper = jacksonObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private val items = mapper.readValue(itemsFile,Items::class.java)
    private val rnd = Random

    fun generateWeapon(size:ItemSize?,type:WeaponType?):WeaponVariant{
        val randType = OneOf<WeaponType>(rnd).oneOf(WeaponType.values().toList())
        val randSize = OneOf<ItemSize>(rnd).oneOf(ItemSize.values().toList())
        val typeToUse = type?:randType
        val sizeToUse = size?:randSize
        val itemType = items.Weapons.types.find{
            it.name == typeToUse.toString()}?: throw ItemTypeNotFoundException(typeToUse.toString())
        val possibleWeapons = itemType.variants.filter { it.size == sizeToUse}
        if (possibleWeapons.isEmpty()) throw ItemNotFoundException(sizeToUse,typeToUse.toString())
        return OneOf<WeaponVariant>(rnd).oneOf(possibleWeapons)
    }

    fun generateContainer(size:ItemSize?,type:ContainerType?):ContainerVariant{
        val randType = OneOf<ContainerType>(rnd).oneOf(ContainerType.values().toList())
        val randSize = OneOf<ItemSize>(rnd).oneOf(ItemSize.values().toList())
        val typeToUse = type?:randType
        val sizeToUse = size?:randSize
        val containerType = items.Containers.types.find{
            it.name == typeToUse.toString()}?: throw ItemTypeNotFoundException(typeToUse.toString())
        val possibleContainers = containerType.variants.filter { it.size == sizeToUse }
        return OneOf<ContainerVariant>(rnd).oneOf(possibleContainers)
    }
}

enum class ContainerType{
    CHEST
}

enum class WeaponType{
    SWORD,
    AXE
}

enum class ItemSize {
    LARGE,
    MEDIUM,
    SMALL
}

data class Items(
        val Containers: Containers,
        val Weapons:Weapons
)

data class Containers(
        val types: List<Type<ContainerVariant>>
)

data class Type<T>(
        val name: String,
        val variants: List<T>
)

data class ContainerVariant(
        val maxDC: Int,
        val minDC: Int,
        val name: String,
        val size: ItemSize
)

data class Weapons(
        val types: List<Type<WeaponVariant>>
)

data class WeaponVariant(
        val name: String,
        val value:Int,
        val size:ItemSize,
        val description:String
)

class ItemTypeNotFoundException(itemType:String):RuntimeException("Could not find an item in the collection with type $itemType, please add it and populate it with an item of each size")
class ItemNotFoundException(size:ItemSize,type:String):RuntimeException("Could not find a $size $type in the collection, please add it")
