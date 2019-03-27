package display.dungeon

import Dungeon
import grammar.Constraints
import grammar.grammarItems.rooms.DungeonRoom
import java.awt.image.BufferedImage
import theme.Theme
import java.awt.Color
import java.awt.Rectangle
import java.awt.TexturePaint
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt


class DungeonDrawer(val theme: Theme) {

    fun drawDungeon(dungeon: Dungeon): BufferedImage {
        val plotSize = (Constraints.rooms.maxRoomSize*1.5).roundToInt()
        val plots = calculatePlots(dungeon.getRooms().size,plotSize)
        val imageSize= plots.size*plotSize
        val image = BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB)
        val g2D = image.createGraphics()
        g2D.paint = getBackGroundTexture(imageSize)
        g2D.fillRect(0, 0, imageSize, imageSize)
        for (plot in plots){
            g2D.color=Color.WHITE
            g2D.drawRect(plot.x,plot.y,plot.width,plot.height)
        }
        for (roomNumber in 0 until dungeon.getRooms().size) {
            //todo not draw over the top of each-other
            //todo future change to be tilebased
            val plot = plots[roomNumber]
            val room = dungeon.getRooms()[roomNumber] as DungeonRoom
            val x = plot.x + Random.nextInt((1..plot.width - room.size.width))
            val y = plot.y + Random.nextInt((1..plot.height - room.size.height))

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
            val boxWidth = 10 + 5 * room.identifier.length
            g2D.fillRoundRect(x + 1, y + 1, boxWidth, 15, 4, 4)
            g2D.color = Color.BLACK
            g2D.drawRoundRect(x + 1, y + 1, boxWidth, 15, 4, 4)
            g2D.drawString(room.identifier, x + 5, y + 12)
        }
        return image
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

    private fun calculatePlots(roomCount: Int,plotSize:Int): List<Plot> {

        val numPlots = roomCount/2
        val plots = mutableListOf<Plot>()
        var index = 0
        for (y in 0 until numPlots) {
            for (x in 0 until numPlots) {
                index++
                plots.add(Plot(index, x*plotSize, y*plotSize, plotSize, plotSize))
                println("Made plot at ${x*plotSize} ${y*plotSize} with index $index")
            }
        }

        val plotsToUse = mutableListOf<Plot>()
        for (n in 1..roomCount) {
            plots.shuffle()
            plotsToUse.add(plots[0])
            plots.remove(plots[0])
        }
        return plotsToUse.sortedBy { it.index }
    }
}

data class Plot(
        val index : Int,
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
)


