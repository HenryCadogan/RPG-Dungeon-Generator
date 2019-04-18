import grammar.grammarItems.factories.*
import grammar.grammarItems.treasure.money.Money

class DescriptionCreator(val dungeon:Dungeon) {

    fun generateDescription(): String {
        val sb = StringBuilder()
        for (d in dungeon.getRooms()) {
            sb.appendln("Room ${d.id}: ${d.description}")

            if (d.roomEnemies.isNotEmpty()) {
                sb.appendln("   Enemies:")
                d.roomEnemies.forEach {
                    sb.appendln("       ${it.data.name}")
                }
            }
            if (d.roomObjects.isNotEmpty()) {
                sb.appendln("   Items:")
                d.roomObjects.forEach {
                    when (it) {

                        is Container -> {
                            if (it.contents.isNotEmpty()) {
                                sb.appendln("       This room has ${it.data.name} in it")

                                val items = it.contents.map { i ->
                                    when (i) {
                                        is Item -> i.name
                                        is Money -> i.toPrettyString()
                                        else -> ""
                                    }
                                }
                                //todo collate money and identical items into single line
                                sb.appendln("         Containing: $items")
                            } else {
                                sb.appendln("         An empty ${it.data.name}")
                            }
                        }

                        is LockedContainerAndKey -> {
                            dungeon.locksAndKeys.add(it)
                            sb.appendln("         ${it.container.data.description}")
                        }
                        is Key -> {
                            sb.appendln("           ${it.description}")
                        }

                        is Weapon -> {
                            sb.appendln("         ${it.data.name}")
                            sb.appendln("               Damage: ${it.data.damage}")
                            sb.appendln("               Value: ${it.data.value.toPrettyString()}")
                            sb.appendln("               Description: ${it.data.description}")
                        }
                    }
                }
            }
            sb.appendln("----------------------------------------------------------------")
        }
        val monsterAppendix = makeEnemyAppendix()
        return sb.appendln(monsterAppendix).toString()
    }


   private fun makeEnemyAppendix(): String {
        val sb = StringBuilder()
        sb.appendln("Details for enemies in this dungeon:")
        dungeon.enemies.distinctBy { it.data.name }.forEach {
            val data = it.data
            sb.appendln("Creature Name = ${data.name}\n")
            sb.appendln("Full information can be found on page ${data.pageNum} of the monster manual\n")
            sb.appendln("Armour Rating = ${data.armour}\n")
            sb.appendln("Max Health = ${data.health}\n")
            //todo change enemies to be single constructor that takes a list of Attack objects then change this to iterate over the list
            sb.appendln("Primary Attack = ${data.firstAttackName} dealing: ${data.firstAttackDamage}\n")
            if (data.secondAttackName != null) {
                sb.appendln("Secondary Attack = ${data.secondAttackName} dealing: ${data.secondAttackDamage}\n")
            }
            sb.appendln("----------------------------------------------------------------")
        }
        return sb.toString()
    }
}