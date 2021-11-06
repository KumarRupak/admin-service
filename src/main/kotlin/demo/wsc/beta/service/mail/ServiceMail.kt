/*;==========================================
; Title:  Service class for email
; Author: Rupak Kumar
; Date:   17 Sep 2021
;==========================================*/
package demo.wsc.beta.service.mail

import kotlin.Throws
import javax.mail.MessagingException

 interface ServiceMail {

    @Throws(MessagingException::class)
    fun sendCredit(reciver: String?, amount: Long?, cardNo: String?)

}