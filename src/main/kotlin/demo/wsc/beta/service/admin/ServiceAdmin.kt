/*;==========================================
; Title:  Admin Services
; Author: Rupak Kumar
; Date:   16 Sep 2021
;==========================================*/
package demo.wsc.beta.service.admin

import kotlin.Throws
import demo.wsc.beta.exceptions.WSCExceptionInvalidModeldata
import demo.wsc.beta.model.WSCOwner
import demo.wsc.beta.exceptions.WSCExceptionInsufficientFund
import demo.wsc.beta.exceptions.WSCExceptionInvalidUser
import demo.wsc.beta.model.CustomerDetails
import javax.mail.MessagingException
import demo.wsc.beta.model.transport.AllowCredit
import demo.wsc.beta.model.transport.AllowCreditStatus
import demo.wsc.beta.model.WSCCards
import demo.wsc.beta.model.WSCServices
import demo.wsc.beta.model.transport.AdminProfile
import demo.wsc.beta.model.transport.UpdateBalance

 interface ServiceAdmin {
    @Throws(WSCExceptionInvalidModeldata::class)
    fun publishCard(card: WSCCards): Boolean

    @Throws(WSCExceptionInvalidModeldata::class)
    fun publishOwner(owner: WSCOwner): Boolean

    @Throws(WSCExceptionInsufficientFund::class, MessagingException::class)
    fun allowCredit(details: AllowCredit): AllowCreditStatus

    fun getEligibleCustomers(): List<CustomerDetails>

    fun getOrganizations(): List<WSCServices>

    @Throws(WSCExceptionInvalidUser::class)
    fun updateBalance(balance:UpdateBalance):UpdateBalance

    @Throws(WSCExceptionInvalidUser::class)
    fun getProfile(brandchId:Int):AdminProfile

    @Throws(WSCExceptionInvalidUser::class)
    fun activateToken(email:String):Boolean


}