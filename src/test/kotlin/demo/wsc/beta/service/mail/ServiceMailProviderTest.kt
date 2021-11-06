/*;==========================================
; Title:  Test Class For Mail Services
; Author: Rupak Kumar
; Date:   2 Oct 2021
;==========================================*/

package demo.wsc.beta.service.mail

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.Throws
import javax.mail.MessagingException

@SpringBootTest
internal class ServiceMailProviderTest {

    @Autowired
    private lateinit var service: ServiceMailProvider



    @Test
    @Throws(MessagingException::class)
    fun sendSetTransactionLimit() {
        var status:Boolean
        try {
        service.sendCredit("patrorupak99@gmail.com",10000,"1234567890123456")
            status=true
        }
        catch (e:MessagingException){
            status=false
        }
        Assertions.assertTrue(status)
    }


}