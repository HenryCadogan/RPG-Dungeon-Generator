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
import javafx.application.Platform
import javafx.beans.property.*
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Slider

import kotlin.random.Random
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.stage.Stage
import javafx.stage.WindowEvent
import sun.misc.Signal.handle
import tornadofx.FX.Companion.primaryStage
import java.awt.Desktop
import java.io.File
import javax.imageio.ImageIO


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

    private val roomCount = model.bind { SimpleIntegerProperty() }
    private val roomSparsity = model.bind { SimpleDoubleProperty() }

    private val trappedPercentage = model.bind { SimpleFloatProperty() }
    private val roomSize = model.bind { SimpleIntegerProperty() }
    private val roomConnectivity = model.bind { SimpleIntegerProperty() }

    private val dungeonTheme = model.bind { SimpleObjectProperty<Theme>() }

    private val saveDirectory = model.bind { SimpleStringProperty() }
    private val saveDirectoryLabel = Label()

    private val maxEnemiesPerRoom = model.bind { SimpleIntegerProperty() }
    private val enemySparsity = model.bind { SimpleFloatProperty() }


    private val sparsitySlider = Slider(0.0, 1.0, 0.5)
    private val sparsityLabel = Label((sparsitySlider.value * 100).toInt().toString())
    private val trappedSlider = Slider(0.0, 1.0, 0.2)
    private val trappedLabel = Label((trappedSlider.value * 100).toInt().toString())
    private val connectivitySlider = Slider(0.0, 1.0, 0.3)
    private val connectivityLabel = Label((connectivitySlider.value * 100).toInt().toString())

    private val enemiesSlider = Slider(0.0, 1.0, 0.5)
    private val enemiesLabel = Label((enemiesSlider.value * 100).toInt().toString())

    private val myController: MyController by inject()


    override val root = vbox {
        tabpane {

            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

            tab("Room Settings") {
                form {
                    fieldset {
                        field("Max Number of rooms") {
                            textfield(roomCount).required()
                        }

                        field("Theme") {
                            vgrow
                            combobox(property = dungeonTheme, values = myController.themes.observable()).required()
                        }
                        field("Room Sparsity") {
                            hbox(20) {
                                this.add(sparsitySlider)
                                this.add(sparsityLabel)
                            }
                        }
                        field("Trapped Room Percentage") {
                            hbox(20) {
                                this.add(trappedSlider)
                                this.add(trappedLabel)
                            }
                        }
                        field("Connectivity") {
                            hbox(20) {
                                this.add(connectivitySlider)
                                this.add(connectivityLabel)
                            }
                        }.tooltip { text = "This value determines how many corridors connect the rooms together" }

                    }
                }

                tab("Enemy Settings") {
                    form {
                        fieldset {
                            field("Max enemies per room") {
                                textfield(maxEnemiesPerRoom) {
                                    autosize()
                                }.required()
                            }

                            field("Enemy Sparsity") {
                                hbox(20) {
                                    this.add(enemiesSlider)
                                    this.add(enemiesLabel)
                                }
                            }
                        }
                    }
                }
            }
        }

        buttonbar {
                button("Save Directory") {
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
                                roomCount = roomCount.value.toInt(),
                                roomSparsity = roomSparsity.value.toFloat(),
                                trappedPercentage = trappedPercentage.value.toFloat(),
                                roomSize = roomSize.value.toInt(),
                                roomConnectivity = roomConnectivity.value.toInt(),
                                dungeonTheme = dungeonTheme.value,
                                maxEnemiesPerRoom = maxEnemiesPerRoom.value.toInt(),
                                enemySparsity = enemySparsity.value.toFloat()
                        )
                        println(constraints)
                        println(saveDirectory.value)
                        myController.generateMap(constraints,saveDirectory.value)
                    }
                }
                vboxConstraints {
                    marginLeft = 20.0
                    marginTopBottom(20.0)
                }
            }
        }

        this.add(saveDirectoryLabel)
    }

    init {
        //add initial values to model in case the user does not change them
        roomConnectivity.value = connectivitySlider.value
        roomCount.value = 10
        roomSparsity.value = sparsitySlider.value
        trappedPercentage.value = trappedSlider.value
        enemySparsity.value = enemiesSlider.value
        maxEnemiesPerRoom.value = 10
        roomSize.value = 100

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
            roomConnectivity.value = new_val
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
    private val dungeon = Dungeon()
    private val random = Random
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun generateMap(constraints: DungeonConstraints, saveLocation: String) {
        setConstraints(constraints)
        val text = dungeon.generate()
        val image = dungeon.draw()
        val name = (1..10)
                .map { i -> Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

        val imageFile = File("${saveLocation}Dungeon$name.png")
        val textFile = File("${saveLocation}Dungeon$name.txt")
        textFile.writeText(text)
        ImageIO.write(image, "png", imageFile)
        val desktop = Desktop.getDesktop()
        desktop.open(imageFile.absoluteFile)
        desktop.open(textFile)
    }


    private fun setConstraints(c: DungeonConstraints) {
        Constraints.theme = c.dungeonTheme
        createFactories(c.dungeonTheme)
        setRoomConstraints(c.roomCount, c.roomSparsity, c.trappedPercentage, c.roomSize, c.roomConnectivity)
        setEnemyConstraints(c.maxEnemiesPerRoom, c.enemySparsity)
    }


    private fun createFactories(theme: Theme) {
        Factories.enemyFactory = EnemyFactory(theme)
        Factories.itemFactory = ItemsFactory(random)
        Factories.trappedRoomFactory = TrappedRoomFactory()
        Factories.roomFactory = DungeonRoomFactory(theme)
    }

    private fun setRoomConstraints(maxRooms: Int,
                                   roomSparsity: Float,
                                   trappedPercentage: Float,
                                   maxRoomSize: Int,
                                   connectivity: Int
    ) {
        Constraints.rooms.maxRoomCount = maxRooms
        Constraints.rooms.roomSparsity = roomSparsity
        Constraints.rooms.trappedRoomPercentage = trappedPercentage
        Constraints.rooms.maxRoomSize = maxRoomSize
        Constraints.rooms.connectivityThreshold = connectivity
    }

    private fun setEnemyConstraints(maxEnemiesPerRoom: Int, enemySparsity: Float) {
        Constraints.enemies.maxEnemiesPerRoom = maxEnemiesPerRoom
        Constraints.enemies.enemySparsity = enemySparsity
    }
}


fun main() {
    launch<GeneratorApp>()
}

data class DungeonConstraints(
        val roomCount: Int,
        val roomSparsity: Float,
        val trappedPercentage: Float,
        val roomSize: Int,
        val roomConnectivity: Int,
        val dungeonTheme: Theme,
        val maxEnemiesPerRoom: Int,
        val enemySparsity: Float
)