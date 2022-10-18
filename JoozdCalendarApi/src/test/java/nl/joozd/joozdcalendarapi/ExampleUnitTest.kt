package nl.joozd.joozdcalendarapi

import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testBuilder(){
        val e = EventsExtractor{
            endAfterEpochMilli(1000)
        }
        assert(e is EventsExtractor)
    }
}