package grammar.operators

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import kotlin.random.Random


class OneOrMoreKtTest{

    private val rnd: Random = mock()
    private val operators = GrammarOperators(rnd)

    @BeforeTest
    fun setup(){
        reset(rnd)
    }
    @Test
    fun `it should return a list containing Items of the class specified`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(0f)
        val output = operators.oneOrMore.oneOrMore(item,1,1f)
        Assert.assertTrue(output.first() is TestItem)
    }

    @Test
    fun `it should return a list containing multiple Items of the class specified`(){
        val item = TestItem(false)
        val output =operators.oneOrMore.oneOrMore(item,2,1f)
        whenever(rnd.nextFloat()).thenReturn(1f)
        Assert.assertEquals(output.size,2)
        Assert.assertTrue(output.first() is TestItem)
        Assert.assertTrue(output[1] is TestItem)
        Assert.assertNotEquals(output.first(),output[1])
    }

    @Test
    fun `it should not return a list bigger than the limit`(){
        val item = TestItem(false)
        whenever(rnd.nextFloat()).thenReturn(1f)
        val output = operators.oneOrMore.oneOrMore(item,1,1f)
        Assert.assertTrue(output.size<2)
    }

}