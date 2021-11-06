package demo.wsc.beta

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class WebSmartCreditApplicationTests {
    @Test
    fun contextLoads() {
        var x:Long=(Random().nextDouble()*100000000000000L).toLong()
        System.out.println(72.toString() + String.format("%014d", x))
    }
    @Test
    fun test(){
        Assertions.assertTrue("CUSTOMeR".uppercase().equals("CUSTOMER"))
    }
}
