/*;==========================================
; Title:  Service class for email
; Author: Rupak Kumar
; Date:   17 Sep 2021
;==========================================*/
package demo.wsc.beta.service.mail


import kotlin.Throws
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import javax.mail.*
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress

@Service
 class ServiceMailProvider : ServiceMail {
    val WebSmartCredit = "WebSmartCredit"

    companion object {
        private const val sender = "noreply.wscredit@gmail.com"
        private const val password = "rupakkum"
    }

    private lateinit var properties: Properties
    val property: Properties
        get() {
            properties = Properties()
            properties["mail.smtp.auth"] = "true"
            properties["mail.smtp.starttls.enable"] = "true"
            properties["mail.smtp.host"] = "smtp.gmail.com"
            properties["mail.smtp.port"] = "587"
            return properties
        }


    @Throws(MessagingException::class)
    override fun sendCredit(reciver: String?, amount: Long?, cardNo: String?) {
        properties = property
        val properties = properties
        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(sender, password)
            }
        })
        val message = MessageCredit(session, sender, reciver, amount, cardNo)
        try {
            Transport.send(message)
        } catch (e: Exception) {
            e.message
        }
    }


    private fun MessageCredit(
        session: Session,
        sender: String,
        reciver: String?,
        amount: Long?,
        cardNo: String?
    ): Message? {
        val message: Message = MimeMessage(session)
        return try {
            message.setFrom(InternetAddress(sender))
            message.setRecipient(Message.RecipientType.TO, InternetAddress(reciver))
            message.subject = "Notification ${WebSmartCredit}"
            message.setText("Dear Customer \n\nAmount of INR $amount\n\nhas been credited to your credit card $cardNo\n\nThanks team WSC")
            message
        } catch (e: Exception) {
            e.message
            null
        }
    }


}