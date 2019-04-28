package grammar.operators

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import grammar.Factories
import grammar.grammarItems.factories.Descriptions
import grammar.grammarItems.factories.DungeonRoomFactory
import grammar.grammarItems.rooms.DungeonRoom
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import theme.Theme
import kotlin.random.Random


class OneOrMoreKtTest{

    private val rnd: Random = mock()
    private val operators = GrammarOperators(rnd)
    private val roomFactory= DungeonRoomFactory(Theme.AQUATIC)

    @BeforeTest
    fun setup(){
        reset(rnd)
        Factories.roomFactory = roomFactory
    }

    @Test
    fun `it should return a list containing Items of the class specified`(){
        val item = DungeonRoom(false)
        whenever(rnd.nextFloat()).thenReturn(0f)
        val output = operators.oneOrMore.oneOrMore(item,1,1f)
        Assert.assertTrue(output.first() is DungeonRoom)
    }

    @Test
    fun `it should return a list containing multiple Items of the class specified`(){
        val item = DungeonRoom(false)
        val output =operators.oneOrMore.oneOrMore(item,2,1f)
        whenever(rnd.nextFloat()).thenReturn(1f)
        Assert.assertEquals(output.size,2)
        Assert.assertTrue(output.first() is DungeonRoom)
        Assert.assertTrue(output[1] is DungeonRoom)
        Assert.assertNotEquals(output.first(),output[1])
    }

    @Test
    fun `it should not return a list bigger than the limit`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        val output = operators.oneOrMore.oneOrMore(item,1,1f)
        Assert.assertTrue(output.size<2)
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun`it should not allow the limit to be negative`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        operators.oneOrMore.oneOrMore(item,-1,1f)
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun`it should not allow the limit to be less than min`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        operators.oneOrMore.oneOrMore(item,1,2,1f)
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun`it should not allow the min to be negative`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        operators.oneOrMore.oneOrMore(item,1, -1,1f)
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun`it should not allow the probability to less than 0`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        operators.oneOrMore.oneOrMore(item,1,-0.1f)
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun`it should not allow the probability to be equal to 0`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        operators.oneOrMore.oneOrMore(item,1,0f)
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun`it should not allow the probability to be greater than 1`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        operators.oneOrMore.oneOrMore(item,1,10f)
    }


}