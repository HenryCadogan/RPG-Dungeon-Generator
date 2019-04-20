package display.menu

import theme.Theme
import tornadofx.*
import Dungeon
import grammar.Constraints
import grammar.Factories
import grammar.grammarItems.enemies.EnemyFactory
import grammar.grammarItems.factories.DungeonRoomFactory
import grammar.grammarItems.factories.ItemsFactory
import grammar.grammarItems.factories.TrappedRoomFactory
import grammar.grammarItems.treasure.money.MoneyValue
import grammar.operators.oneOf
import javafx.application.Platform
import javafx.beans.property.*
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ButtonBar
import javafx.scene.control.Slider

import kotlin.random.Random
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.stage.Stage
import javafx.stage.WindowEvent
import tornadofx.FX.Companion.primaryStage
import java.awt.Desktop
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.min


class GeneratorApp : App() {
    override val primaryView = MainMenu::class
    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
        primaryStage.onCloseRequest = EventHandler<WindowEvent> {
            Platform.exit()
            System.exit(0)
        }
    }

}

class MainMenu : View("Dungeon Generator Settings") {
    private val model = ViewModel()

    private val maxRoomCount = model.bind { SimpleIntegerProperty() }
    private val minRoomCount = model.bind { SimpleIntegerProperty() }
    private val roomSparsity = model.bind { SimpleDoubleProperty() }

    private val trappedPercentage = model.bind { SimpleFloatProperty() }
    private val roomSize = model.bind { SimpleIntegerProperty() }
    private val roomConnectivity = model.bind { SimpleIntegerProperty() }

    private val dungeonTheme = model.bind { SimpleObjectProperty<Theme>() }

    private val saveDirectory = model.bind { SimpleStringProperty() }
    private val saveDirectoryLabel = Label()

    private val maxEnemiesPerRoom = model.bind { SimpleIntegerProperty() }
    private val minEnemiesPerRoom = model.bind { SimpleIntegerProperty() }
    private val enemySparsity = model.bind { SimpleFloatProperty() }

    private val sparsitySlider = Slider(0.0, 1.0, 0.5)
    private val sparsityLabel = Label((sparsitySlider.value * 100).toInt().toString())
    private val trappedSlider = Slider(0.0, 1.0, 0.2)
    private val trappedLabel = Label((trappedSlider.value * 100).toInt().toString())
    private val connectivitySlider = Slider(0.0, 0.5, 0.15)
    private val connectivityLabel = Label((connectivitySlider.value * 100).toInt().toString())

    private val enemiesSlider = Slider(0.0, 1.0, 0.5)
    private val enemiesLabel = Label((enemiesSlider.value * 100).toInt().toString())

    private val minContainersPerRoom = model.bind { SimpleIntegerProperty() }
    private val maxContainersPerRoom = model.bind { SimpleIntegerProperty() }

    private val minItemsOfLootPerRoom = model.bind { SimpleIntegerProperty() }
    private val maxItemsOfLootPerRoom = model.bind { SimpleIntegerProperty() }

    private val moneyValuePerPile = model.bind { SimpleObjectProperty<MoneyValue>() }

    private val dungeonName = model.bind { SimpleStringProperty() }

    private val myController: MyController by inject()


    override val root = vbox {
        tabpane {

            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            tab("Rooms") {
                form {
                    fieldset {
                        field("Max Number of rooms") {
                            textfield(maxRoomCount) {
                                maxWidth = 30.0
                            }.required()
                        }
                        field("Min Number of rooms") {
                            textfield(minRoomCount) {
                                maxWidth = 30.0
                            }.required()

                        }

                        field("Theme") {
                            vgrow
                            combobox(property = dungeonTheme, values = myController.themes.observable()).required()
                        }
                        field("Room Sparsity") {
                            hbox(10) {
                                this.add(sparsitySlider)
                                this.add(sparsityLabel)
                            }
                        }.tooltip { text = "Higher means closer to max number of rooms." }
                        field("Trapped Room Percentage") {
                            hbox(10) {
                                this.add(trappedSlider)
                                this.add(trappedLabel)
                            }
                        }.tooltip { text = "Percentage of rooms that will be trapped." }
                        field("Connectivity") {
                            hbox(10) {
                                this.add(connectivitySlider)
                                this.add(connectivityLabel)
                            }
                        }.tooltip { text = "Higher means more corridors between the rooms, ideally leave this lower than 30." }

                    }
                }

                tab("Enemies") {
                    form() {
                        fieldset {
                            field("Max enemies per room") {
                                textfield(maxEnemiesPerRoom) {
                                    maxWidth = 30.0
                                }.required()
                            }
                            field("Min enemies per room") {
                                textfield(minEnemiesPerRoom) {
                                    maxWidth = 30.0
                                }.required()

                            }

                            field("Enemy sparsity") {
                                hbox {
                                    this.add(enemiesSlider)
                                    this.add(enemiesLabel)
                                }
                            }.tooltip { text = "Higher means closer to max enemies" }
                        }
                    }
                }

                tab("Treasure") {
                    form {
                        fieldset {
                            field("Max containers per room") {
                                textfield(property = maxContainersPerRoom) {
                                    maxWidth = 30.0
                                }.required()

                            }

                            field("Min containers per room") {
                                textfield(minContainersPerRoom) {
                                    maxWidth = 30.0
                                }.required()
                            }

                            field("Max items of loot per room") {
                                textfield(maxItemsOfLootPerRoom) {
                                    maxWidth = 30.0
                                }.required()

                            }

                            field("Min items of loot per room") {
                                textfield(minItemsOfLootPerRoom) {
                                    maxWidth = 30.0
                                }.required()

                            }

                            field("Value of money per pile") {
                                combobox(property = moneyValuePerPile, values = myController.moneyValues.observable()).required()
                            }
                        }
                    }
                }
            }
        }

        vbox {
            padding = Insets(0.0, 20.0, 0.0, 20.0)
            hbox {
                label { text = "Current Save Location:   " }
                this.add(saveDirectoryLabel)
            }

            hbox {
                padding = Insets(10.0, 0.0, 10.0, 0.0)
                label { text = "Dungeon Name   " }
                textfield(dungeonName).required(message = "Please enter a name for the dungeon")
            }
        }

        buttonbar {
            padding = Insets(10.0, 20.0, 10.0, 20.0)
            button("Select Save Directory", ButtonBar.ButtonData.LEFT) {
                action {
                    val dir = chooseDirectory("Select Save Directory").toString()
                    if (dir != "null") {
                        saveDirectory.value = dir
                        saveDirectoryLabel.text = dir
                    }
                }
            }
            button("Generate!") {

                enableWhen(model.valid)
                isDefaultButton = true
                useMaxHeight = true
                action {
                    runAsyncWithProgress {

                        val constraints = DungeonConstraints(
                                maxRoomCount = maxRoomCount.value.toInt(),
                                minRoomCount = minRoomCount.value.toInt(),
                                roomSparsity = roomSparsity.value.toFloat(),
                                trappedPercentage = trappedPercentage.value.toFloat(),
                                roomSize = roomSize.value.toInt(),
                                roomConnectivity = roomConnectivity.value.toInt(),
                                dungeonTheme = dungeonTheme.value,
                                maxEnemiesPerRoom = maxEnemiesPerRoom.value.toInt(),
                                minEnemiesPerRoom = minEnemiesPerRoom.value.toInt(),
                                enemySparsity = enemySparsity.value.toFloat(),
                                minContainersPerRoom = minContainersPerRoom.value.toInt(),
                                maxContainersPerRoom = maxContainersPerRoom.value.toInt(),
                                minItemsOfLootPerRoom = minContainersPerRoom.value.toInt(),
                                maxItemsOfLootPerRoom = maxItemsOfLootPerRoom.value.toInt(),
                                moneyValuePerPile = moneyValuePerPile.value
                        )
                        println(constraints)
                        println(saveDirectory.value)
                        myController.generateMap(constraints, saveDirectory.value, dungeonName.value)
                    }
                }
                vboxConstraints {
                    marginTopBottom(20.0)
                }

            }
        }

    }

    init {
        //add initial values to model in case the user does not change them
        roomConnectivity.value = connectivitySlider.value * 100
        maxRoomCount.value = 15
        minRoomCount.value = 8
        roomSparsity.value = sparsitySlider.value
        trappedPercentage.value = trappedSlider.value
        enemySparsity.value = enemiesSlider.value
        maxEnemiesPerRoom.value = 5
        roomSize.value = 100

        maxItemsOfLootPerRoom.value = 3
        minItemsOfLootPerRoom.value = 0
        maxContainersPerRoom.value = 3
        minContainersPerRoom.value = 0


        val defaultSaveLocation = System.getProperty("user.home") + "\\Documents"
        saveDirectory.value = defaultSaveLocation
        saveDirectoryLabel.text = defaultSaveLocation
        saveDirectoryLabel.alignment = Pos.BASELINE_RIGHT

        sparsitySlider.valueProperty().addListener { _, _, new_val ->
            roomSparsity.value = new_val
            sparsityLabel.text = (new_val.toDouble() * 100).toInt().toString()
        }
        trappedSlider.valueProperty().addListener { _, _, new_val ->
            trappedPercentage.value = new_val
            trappedLabel.text = (new_val.toDouble() * 100).toInt().toString()
        }
        connectivitySlider.valueProperty().addListener { _, _, new_val ->
            roomConnectivity.value = (new_val.toDouble() * 100).toInt()
            connectivityLabel.text = (new_val.toDouble() * 100).toInt().toString()
        }

        enemiesSlider.valueProperty().addListener { _, _, new_val ->
            enemySparsity.value = new_val
            enemiesLabel.text = (new_val.toDouble() * 100).toInt().toString()
        }
    }
}


class MyController : Controller() {
    val themes = Theme.values().toList()
    val moneyValues = MoneyValue.values().toList()
    private val random = Random

    fun generateMap(constraints: DungeonConstraints, saveLocation: String, saveName: String) {
        setConstraints(constraints)
        val dungeon = Dungeon()
        val text = dungeon.generate()
        val image = dungeon.draw()
        val imageFile = File("$saveLocation\\$saveName.png")
        val textFile = File("$saveLocation\\$saveName.txt")
        textFile.writeText(text)
        ImageIO.write(image, "png", imageFile)
        val desktop = Desktop.getDesktop()
        try {
            desktop.open(imageFile)
        } catch (e: IOException) {
            desktop.edit(imageFile)
        } catch (e: IOException) {
            //todo do something else here rather than hide the error
        }
        desktop.open(textFile)
    }


    private fun setConstraints(c: DungeonConstraints) {
        Constraints.theme = if (c.dungeonTheme == Theme.RANDOM) {
            Theme.values().toList().oneOf()
        } else {
            c.dungeonTheme
        }
        createFactories(Constraints.theme)
        setRoomConstraints(c.maxRoomCount, c.minRoomCount, c.roomSparsity, c.trappedPercentage, c.roomSize, c.roomConnectivity)
        setEnemyConstraints(c.maxEnemiesPerRoom, c.enemySparsity)
        setTreasureConstraints(c.maxContainersPerRoom,c.minContainersPerRoom,c.maxItemsOfLootPerRoom,c.minItemsOfLootPerRoom,c.moneyValuePerPile)
    }


    private fun createFactories(theme: Theme) {
        Factories.enemyFactory = EnemyFactory(theme)
        Factories.itemFactory = ItemsFactory(random)
        Factories.trappedRoomFactory = TrappedRoomFactory()
        Factories.roomFactory = DungeonRoomFactory(theme)
    }

    private fun setRoomConstraints(maxRooms: Int,
                                   minRooms: Int,
                                   roomSparsity: Float,
                                   trappedPercentage: Float,
                                   maxRoomSize: Int,
                                   connectivity: Int
    ) {
        //subtract one as the starting room is always added
        Constraints.rooms.maxRoomCount = maxRooms-1
        Constraints.rooms.minRoomCount = minRooms-1
        Constraints.rooms.roomSparsity = roomSparsity
        Constraints.rooms.trappedRoomPercentage = trappedPercentage
        Constraints.rooms.maxRoomSize = maxRoomSize
        Constraints.rooms.connectivityThreshold = connectivity
    }

    private fun setEnemyConstraints(maxEnemiesPerRoom: Int, enemySparsity: Float) {
        Constraints.enemies.maxEnemiesPerRoom = maxEnemiesPerRoom
        Constraints.enemies.enemySparsity = enemySparsity
    }

    private fun setTreasureConstraints(maxContainers: Int, minContainers: Int, maxLoot: Int, minLoot: Int, moneyValue:MoneyValue) {
        Constraints.loot.minLootPerRoom = minLoot
        Constraints.loot.maxLootPerRoom = maxLoot
        Constraints.containers.minContainersPerRoom = minContainers
        Constraints.containers.maxContainersPerRoom = maxContainers
        Constraints.loot.moneyValuePerPile = moneyValue
    }
}


fun main() {
    launch<GeneratorApp>()
}

data class DungeonConstraints(
        val maxRoomCount: Int,
        val minRoomCount: Int,
        val roomSparsity: Float,
        val trappedPercentage: Float,
        val roomSize: Int,
        val roomConnectivity: Int,
        val dungeonTheme: Theme,
        val maxEnemiesPerRoom: Int,
        val minEnemiesPerRoom: Int,
        val enemySparsity: Float,
        val maxContainersPerRoom: Int,
        val minContainersPerRoom: Int,
        val minItemsOfLootPerRoom: Int,
        val maxItemsOfLootPerRoom: Int,
        val moneyValuePerPile: MoneyValue
)