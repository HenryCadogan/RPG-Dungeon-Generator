package grammar.operators

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import grammar.Constraints
import grammar.Factories
import grammar.grammarItems.GrammarItem
import grammar.grammarItems.factories.DungeonRoomFactory
import grammar.grammarItems.rooms.DungeonRoom
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import theme.Theme
import kotlin.random.Random


class TestItem(terminal:Boolean = true): DungeonRoom(terminal)

class OneOfKtTest {
    private val rnd : Random = mock()
    private val ops = GrammarOperators(rnd)

    @BeforeTest
    fun setup(){
        reset(rnd)
    }

    @Test
    fun `when not specifying a weight it should pick each element half the time`() {
        val itemA = TestItem()
        val itemB = TestItem()
        whenever(rnd.nextInt(any(),any())).thenReturn(1)
        val result1 = ops.oneOf.oneOf(listOf(itemA,itemB))
        Assert.assertEquals(result1.size,1)
        Assert.assertEquals(result1.first(), itemA)

        whenever(rnd.nextInt(any(),any())).thenReturn(2)
        val result = ops.oneOf.oneOf(listOf(itemA,itemB))
        Assert.assertEquals(result.size,1)
        Assert.assertEquals(result.first(), itemB)
    }

    @Test
    fun `it should select based on the weightings provided`(){
        val itemA = TestItem(false)
        val itemB = TestItem(false)
        val itemC = TestItem(false)
        val items = mapOf<GrammarItem,Int>(itemA to 2,itemB to 1,itemC to 3)
        whenever(rnd.nextInt(any(),any())).thenReturn(6)
        val result1 = ops.oneOf.oneOf(items)
        Assert.assertEquals(result1.size,1)
        Assert.assertEquals(result1.first(),itemC)
        whenever(rnd.nextInt(any(),any())).thenReturn(3)
        val result2 = ops.oneOf.oneOf(items)
        Assert.assertEquals(result2.size,1)
        Assert.assertEquals(result2.first(),itemA)
        whenever(rnd.nextInt(any(),any())).thenReturn(1)
        val result3 = ops.oneOf.oneOf(items)
        Assert.assertEquals(result3.size,1)
        Assert.assertEquals(result3.first(),itemB)
    }


    @Test
    fun `it should return the single element if only one is provided`(){
        val itemA = TestItem(false)
        whenever(rnd.nextInt(any(),any())).thenReturn(1)
        val result = ops.oneOf.oneOf(listOf(itemA))
        Assert.assertEquals(result.size,1)
        Assert.assertEquals(result.first(),itemA)
    }
}