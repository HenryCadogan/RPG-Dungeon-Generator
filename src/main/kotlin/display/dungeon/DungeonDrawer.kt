package display.dungeon

import Dungeon
import grammar.grammarItems.rooms.DungeonRoom
import grammar.operators.oneOf
import java.awt.image.BufferedImage
import theme.Theme
import java.awt.Color
import java.awt.Rectangle
import java.awt.TexturePaint
import javax.imageio.ImageIO
import kotlin.random.Random
import kotlin.random.nextInt


class DungeonDrawer(val theme: Theme) {


    fun drawDungeon(dungeon: Dungeon): BufferedImage {
        val image = BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB)
        val g2D = image.createGraphics()
        g2D.paint = getBackGroundTexture()
        g2D.fillRect(0, 0, 1000, 1000)


        for (room in dungeon.getRooms()) {
            //todo not draw over the top of each-other
            //todo future change to be tilebased
            room as DungeonRoom
            val x = Random.nextInt((1..1000 step (20)).toList().oneOf())
            val y = Random.nextInt((1..1000 step (20)).toList().oneOf())
            val size = room.size

            g2D.paint = getRoomTexture(size.width, size.height)
            g2D.fillRect(x, y, size.width, size.height)
            g2D.color = Color.BLACK
            g2D.drawRect(x, y, size.width, size.height)
            for (item in room.roomObjects) {
                val itemSize = 15
                val itemX = x + Random.nextInt(1..size.width - itemSize)
                val itemY = y + Random.nextInt(1..size.height - itemSize)
                g2D.color = Color.RED
                g2D.fillOval(itemX,
                        itemY,
                        itemSize,
                        itemSize)
                g2D.color = Color.BLACK
                g2D.drawOval(itemX,
                        itemY,
                        itemSize,
                        itemSize)
            }
        }

        return image
    }

    private fun getBackGroundTexture(): TexturePaint {
        //todo get the themed texture here
        val url = this::class.java.getResource("/textures/backgrounds/Stone.jpg")
        val texture = ImageIO.read(url)
        return TexturePaint(texture, Rectangle(1000, 1000))
    }

    private fun getRoomTexture(width: Int, height: Int): TexturePaint {
        val url = this::class.java.getResource("/textures/rooms/woodenFloor.jpg")
        val texture = ImageIO.read(url)
        return TexturePaint(texture, Rectangle(width, height))
    }
}

