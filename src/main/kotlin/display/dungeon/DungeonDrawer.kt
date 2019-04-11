package display.dungeon

import Dungeon
import grammar.grammarItems.rooms.DungeonRoom
import grammar.grammarItems.rooms.MapPosition
import grammar.grammarItems.rooms.angleTo
import grammar.grammarItems.rooms.distanceTo
import java.awt.image.BufferedImage
import theme.Theme
import java.awt.*
import java.lang.RuntimeException
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt


class DungeonDrawer(val theme: Theme) {

    fun drawDungeon(dungeon: Dungeon,imageSize:Int): BufferedImage {
        val image = BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB)
        val g2D = image.createGraphics()
        g2D.paint = getBackGroundTexture(imageSize)
        g2D.fillRect(0, 0, imageSize, imageSize)
        val bs1 = BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL)
        g2D.stroke = bs1
        drawCorridors(g2D,dungeon.getRooms())
        for (room in  dungeon.getRooms()) {
            //todo future change to be tile based
            val x = room.position.x
            val y = room.position.y
            val size = room.size


            g2D.paint = getRoomTexture(size.width, size.height)
            g2D.fillRect(x, y, size.width, size.height)
            g2D.color = Color.BLACK
            g2D.drawRect(x, y, size.width, size.height)


            for (item in room.roomObjects) {
                val itemSize = 10
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
            //todo account for larger room sizes
            g2D.color = Color.WHITE
            val boxWidth = 10 + (5 * room.id.length)
            g2D.fillRoundRect(x + 1, y + 1, boxWidth, 15, 4, 4)
            g2D.color = Color.BLACK
            g2D.drawRoundRect(x + 1, y + 1, boxWidth, 15, 4, 4)
            g2D.drawString(room.id, x + 5, y + 12)
        }

        return image
    }

    private fun drawCorridors(g2D: Graphics2D,rooms:List<DungeonRoom>){
        for (room in rooms) {
            val lc = room.leftChiLd
            val rc = room.rightChiLd
            if (lc != null) {
                println("Connecting ${room.id} to ${lc.id}")
                drawCorridorBetweenPoints(room.center, lc.center, g2D)
            }
            if (rc != null) {
                println("Connecting ${room.id} to ${rc.id}")
                drawCorridorBetweenPoints(room.center, rc.center, g2D)
            }
        }
    }

    private fun drawCorridorBetweenPoints(a:MapPosition,b:MapPosition,g2D: Graphics2D){
        val angle =  a.angleTo(b)
        val length = a.distanceTo(b)
        val oldg2D = g2D.transform
        g2D.paint = getCorridorTexture(6,length.roundToInt())
        g2D.rotate(angle+Math.PI*0.5, a.x.toDouble(),a.y.toDouble())
        g2D.fillRect(a.x,a.y,6,length.roundToInt())
        g2D.color = Color.BLACK
        g2D.drawRect(a.x,a.y,6,length.roundToInt())
        g2D.transform = oldg2D
    }

    private fun getBackGroundTexture(size: Int): TexturePaint {
        //todo get the themed texture here
        val url = this::class.java.getResource("/textures/backgrounds/Stone.jpg")
        val texture = ImageIO.read(url)
        return TexturePaint(texture, Rectangle(size, size))
    }

    private fun getRoomTexture(width: Int, height: Int): TexturePaint {
        val url = this::class.java.getResource("/textures/rooms/woodenFloor.jpg")
        val texture = ImageIO.read(url)
        return TexturePaint(texture, Rectangle(width, height))
    }

    private fun getCorridorTexture(width: Int, height: Int):TexturePaint{
        val url = this::class.java.getResource("/textures/corridors/dirtFloor.jpg")
        val texture = ImageIO.read(url)
        return TexturePaint(texture, Rectangle(width, height))
    }





}



class ClosestRoomNotFoundException(room:DungeonRoom) : RuntimeException("Could not find any rooms near point ${room.position.x},${room.position.y}")


