/*;==========================================
; Title:  End Points for admin
; Author: Rupak Kumar
; Date:   23 Sep 2021
;==========================================*/

package demo.wsc.beta.controller.admin

import demo.wsc.beta.algorithms.PasswordEncode.Encoder
import demo.wsc.beta.algorithms.utility.Validator
import demo.wsc.beta.appconfig.AppProperties
import demo.wsc.beta.enums.AuthRole
import demo.wsc.beta.exceptions.WSCExceptionInsufficientFund
import demo.wsc.beta.exceptions.WSCExceptionInvalidDetails
import demo.wsc.beta.exceptions.WSCExceptionInvalidModeldata
import demo.wsc.beta.exceptions.WSCExceptionInvalidUser
import demo.wsc.beta.model.WSCCards
import demo.wsc.beta.model.WSCOwner
import demo.wsc.beta.model.transport.ActivateToken
import demo.wsc.beta.model.transport.AllowCredit
import demo.wsc.beta.service.admin.ServiceAdminProvider
import demo.wsc.beta.service.authentication.ServiceAuthenticationProvider
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.mail.MessagingException
import kotlin.jvm.Throws

@RestController
@RequestMapping("api/admin")
class AdminEndPoints {

    @Autowired
    lateinit var serviceAdmin: ServiceAdminProvider

    @Autowired
    private lateinit var serviceAuth: ServiceAuthenticationProvider

    @Autowired
    lateinit var appProperties: AppProperties

    /**
     * End poin for publish  new card by admin
     *
     * @param 'Card Details-JSON'
     * @return - Response enitity
     */

    @PostMapping("offer/{branchId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(ExpiredJwtException::class, SignatureException::class, WSCExceptionInvalidModeldata::class)
    fun publishCard(
        token:String?,
        @PathVariable("branchId") branchId: Int,
        @RequestBody card: WSCCards,
    ): ResponseEntity<Any> {

        if (!token.equals(null)) {
            val body = Jwts.parser().setSigningKey(branchId.toString()).parseClaimsJws(token).body

            if (body.values.toList()[0].equals(branchId.toString()) && serviceAuth.getUserLevel(branchId)
                    == AuthRole.ADMIN.toString()
            ) {
                //-----
                if (!card.cardType.equals(null) && card.cardOffers.isNotEmpty() && card.instalmentPeriod.isNotEmpty() && card.creditAmount > 0 && card.interestRate > 0) {
                    val status = serviceAdmin.publishCard(card)
                    if (status == true) {
                        return ResponseEntity(status, HttpStatus.OK)
                    } else {
                        return ResponseEntity(status, HttpStatus.FORBIDDEN)
                    }
                } else {
                    throw WSCExceptionInvalidDetails()
                }
                //------
            } else {
                throw WSCExceptionInvalidUser()
            }
        }
        throw WSCExceptionInvalidUser()
    }


    /**
     * End point for register the admin
     *
     * @param 'admin details-JSON'
     * @return - Response enitity
     */

    @PostMapping("branch/register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(WSCExceptionInvalidModeldata::class)
    fun publishOwner(
        @RequestBody owner: WSCOwner
    ): ResponseEntity<Any> {

        //-----
        if (owner.adminPin.toString() == Encoder.decode(appProperties.urlPin) && Validator.getLength(owner.branchId) == 5 && owner.accountNo.toString().length == 12) {
            val status = serviceAdmin.publishOwner(owner)
            return ResponseEntity(status, HttpStatus.OK)
        } else {
            throw WSCExceptionInvalidUser()
        }
        //-----
    }

    /**
     * End point allow the credit for customer by admin
     *
     * @param 'details-JSON'
     * @return - Response enitity
     */

    @PutMapping("allowcredit", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(
        ExpiredJwtException::class, SignatureException::class,
        WSCExceptionInsufficientFund::class, MessagingException::class
    )
    fun allowCredit(
        @RequestBody details: AllowCredit,
        token:String?
    ): ResponseEntity<Any> {
        if (!token.equals(null) && details.branchId.toString().length == 5) {
            val body = Jwts.parser().setSigningKey(details.branchId.toString()).parseClaimsJws(token).body

            if (body.values.toList()[0].equals(details.branchId.toString()) && serviceAuth.getUserLevel(details.branchId)
                == AuthRole.ADMIN.toString()
            ) {
                //-----
                if (Validator.getLength(details.customerId) == 6 && Validator.getLength(details.branchId) == 5) {
                    return ResponseEntity(serviceAdmin.allowCredit(details), HttpStatus.OK)
                } else {
                    throw WSCExceptionInvalidDetails()
                }
                //----
            } else {
                throw WSCExceptionInvalidUser()
            }
        }
        throw WSCExceptionInvalidUser()
    }

    /**
     * End point view all the eligible customers for credit
     *
     * @param 'branchId of admin'
     * @return - Response enitity
     */

    @GetMapping("eligiblecustomer/{branchId}")
    @Throws(ExpiredJwtException::class, SignatureException::class)
    fun getEligibleCustomers(
        token:String?,
        @PathVariable("branchId") branchId: Int
    ): ResponseEntity<Any> {

        if (!token.equals(null) && branchId.toString().length == 5) {
            val body = Jwts.parser().setSigningKey(branchId.toString()).parseClaimsJws(token).body

            if (body.values.toList()[0].equals(branchId.toString()) && serviceAuth.getUserLevel(branchId)
                == AuthRole.ADMIN.toString()
            ) {

                //----
                val status = serviceAdmin.getEligibleCustomers()
                if (status.isNotEmpty()) {
                    return ResponseEntity(status, HttpStatus.OK)
                } else {
                    return ResponseEntity(status, HttpStatus.NO_CONTENT)
                }
                //----
            } else {
                throw WSCExceptionInvalidUser()
            }
        }
        throw WSCExceptionInvalidUser()
    }


    /**
     * End point view all the eligible customers for credit
     *
     * @param 'branchId of admin'
     * @return - Response enitity
     */

    @GetMapping("organization/{branchId}")
    @Throws(ExpiredJwtException::class, SignatureException::class)
    fun getOrganizations(
        token:String?,
        @PathVariable("branchId") branchId: Int
    ): ResponseEntity<Any> {
        if (!token.equals(null) && branchId.toString().length == 5) {
            val body = Jwts.parser().setSigningKey(branchId.toString()).parseClaimsJws(token).body

            if (body.values.toList()[0].equals(branchId.toString()) && serviceAuth.getUserLevel(branchId)
                == AuthRole.ADMIN.toString()
            ) {

                //----
                val status = serviceAdmin.getOrganizations()
                if (status.isNotEmpty()) {
                    return ResponseEntity(status, HttpStatus.OK)
                } else {
                    return ResponseEntity(status, HttpStatus.NO_CONTENT)
                }
                //----
            } else {
                throw WSCExceptionInvalidUser()
            }
        }
        throw WSCExceptionInvalidUser()
    }



    /**
     * End point view the admin profile
     *
     * @param 'branchId of admin'
     * @return - Response enitity
     */

    @GetMapping("branch/{branchId}")
    @Throws(ExpiredJwtException::class, SignatureException::class, WSCExceptionInvalidUser::class)
    fun getProfile(
        token:String?,
        @PathVariable("branchId") branchId: Int
    ): ResponseEntity<Any> {

        val body = Jwts.parser().setSigningKey(branchId.toString()).parseClaimsJws(token).body

        if (body.values.toList()[0].equals(branchId.toString()) && serviceAuth.getUserLevel(branchId)
            == AuthRole.ADMIN.toString()
        ) {

            //--------

            return ResponseEntity(serviceAdmin.getProfile(branchId), HttpStatus.OK)
            //--------
        }
        throw WSCExceptionInvalidUser()
    }

    /**
     * End point activate the access the token for other organizations
     *
     * @param 'Details-JSON'
     * @return - Response enitity
     */

    @PatchMapping("activatetoken")
    @Throws(ExpiredJwtException::class, SignatureException::class, WSCExceptionInvalidUser::class)
    fun activateKey(
        token:String?,
        @RequestBody details: ActivateToken
    ): ResponseEntity<Any> {

        val body = Jwts.parser().setSigningKey(details.branchId.toString()).parseClaimsJws(token).body

        if (body.values.toList()[0].equals(details.branchId.toString()) && serviceAuth.getUserLevel(details.branchId)
            == AuthRole.ADMIN.toString()
        ) {
            //--------
            return ResponseEntity(serviceAdmin.activateToken(details.organisationEmail!!), HttpStatus.OK)
            //-------
        }
        throw WSCExceptionInvalidUser()
    }


}
