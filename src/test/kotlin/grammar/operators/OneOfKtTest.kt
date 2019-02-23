package grammar.operators

import grammar.GrammarItem
import org.testng.Assert
import org.testng.annotations.Test


class TestItem(terminal:Boolean = true): GrammarItem(terminal)

class OneOfKtTest {

    @Test
    fun `when not specifying a weight it should pick each element half the time`() {
        val itemA = TestItem()
        val itemB = TestItem()
        var numAs = 0
        var numBs = 0
        for (x in 0 until 10000 ){
            val result = oneOf(listOf(itemA,itemB))
            if (result == itemA){
                numAs ++
            } else {
                numBs ++
            }
        }

        Assert.assertTrue(approximatelyEqual(5000f,numAs.toFloat()))
        Assert.assertTrue(approximatelyEqual(5000f,numBs.toFloat()))
    }

    @Test
    fun `it should select based on the weightings provided`(){
        val itemA = TestItem()
        val itemB = TestItem()
        val itemC = TestItem()
        var numAs = 0
        var numBs = 0
        var numCs = 0
        val weightedInputs = mapOf<GrammarItem,Int>(
                itemA to 1,
                itemB to 2,
                itemC to 3
        )

        for (x in 0 until 12000 ){
            val result = oneOf(weightedInputs)
            when(result){
                itemA -> numAs++
                itemB -> numBs++
                itemC -> numCs++
            }
        }
        Assert.assertTrue(approximatelyEqual(2000f,numAs.toFloat()))
        Assert.assertTrue(approximatelyEqual(4000f,numBs.toFloat()))
        Assert.assertTrue(approximatelyEqual(6000f,numCs.toFloat()))

    }



    private fun approximatelyEqual(desiredValue: Float, actualValue: Float, tolerancePercentage: Float = 10f): Boolean {
        val diff = Math.abs(desiredValue - actualValue)
        val tolerance = tolerancePercentage / 100 * desiredValue
        return diff < tolerance
    }


}