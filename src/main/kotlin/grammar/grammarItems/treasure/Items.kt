package grammar.grammarItems.treasure

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import grammar.operators.OneOf
import kotlin.random.Random


class ItemsFactory(rnd: Random){
    private val containersFile = this::class.java.getResource("/Items/containers.json")
    private val weaponsFile= this::class.java.getResource("/Items/weapons.json")
    private val mapper = jacksonObjectMapper().enable(
            MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private val containers = mapper.readValue(containersFile,Containers::class.java)
    private val weapons = mapper.readValue(weaponsFile,Weapons::class.java)
    private val rnd = Random

    fun generateWeapon(size:ItemSize?,type:WEAPON?): WeaponVariant {
        val randType = OneOf(rnd).oneOf(WEAPON.values().toList())
        val randSize = OneOf(rnd).oneOf(ItemSize.values().toList())
        val typeToUse = type?:randType
        val sizeToUse = size?:randSize
        val items = weapons.weaponTypes.find{it.name == typeToUse}?: throw ItemTypeNotFoundException(type.toString())
        val possibleWeapons = items.variants.filter { it.size == sizeToUse}
        if (possibleWeapons.isEmpty()) throw ItemNotFoundException(sizeToUse,typeToUse.toString())
        return OneOf(rnd).oneOf(possibleWeapons)
    }

    fun generateContainer(size:ItemSize?,type:CONTAINER?):ContainerVariant{
        val randType = OneOf(rnd).oneOf(CONTAINER.values().toList())
        val randSize = OneOf(rnd).oneOf(ItemSize.values().toList())
        val typeToUse = type?:randType
        val sizeToUse = size?:randSize
        val items = containers.containerTypes.find{it.name == typeToUse}?: throw ItemTypeNotFoundException(type.toString())
        val possibleContainers = items.variants.filter{ it.size == sizeToUse }
        return OneOf(rnd).oneOf(possibleContainers)
    }
}

enum class CONTAINER{
    CHEST
}

enum class WEAPON{
    SWORD,
    AXE
}

enum class ItemSize {
    LARGE,
    MEDIUM,
    SMALL
}

data class Containers(
        val containerTypes:List<ContainerType>
)

data class ContainerType(
        val name: CONTAINER,
        val variants: List<ContainerVariant>
)

data class ContainerVariant(
        val maxDC: Int,
        val minDC: Int,
        val name: String,
        val size: ItemSize,
        val description:String
)


data class Weapons(
        val weaponTypes: List<WeaponType>
)

data class WeaponType(
        val name: WEAPON,
        val variants: List<WeaponVariant>
)

data class WeaponVariant(
        val name: String,
        val value:Int,
        val size:ItemSize,
        val damage:String,
        val description:String
)


class ItemTypeNotFoundException(itemType:String):RuntimeException("Could not find an item in the collection with type $itemType, please add it and populate it with an item of each size")
class ItemNotFoundException(size:ItemSize,type:String):RuntimeException("Could not find a $size $type in the collection, please add it")
