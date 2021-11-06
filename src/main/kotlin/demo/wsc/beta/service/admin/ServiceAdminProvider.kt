/*;==========================================
; Title:  Admin Service Provider
; Author: Rupak Kumar
; Date:   16 Sep 2021
;==========================================*/
package demo.wsc.beta.service.admin


import demo.wsc.beta.algorithms.PasswordEncode.Encoder.Companion.encode
import org.springframework.beans.factory.annotation.Autowired
import demo.wsc.beta.service.mail.ServiceMailProvider
import kotlin.Throws
import demo.wsc.beta.exceptions.WSCExceptionInvalidModeldata
import org.springframework.transaction.annotation.Transactional
import demo.wsc.beta.exceptions.WSCExceptionInsufficientFund
import javax.mail.MessagingException
import demo.wsc.beta.model.transport.AllowCredit
import demo.wsc.beta.model.transport.AllowCreditStatus
import java.time.LocalDate
import java.time.LocalDateTime
import demo.wsc.beta.exceptions.WSCExceptionInvalidUser
import demo.wsc.beta.model.*
import demo.wsc.beta.model.transport.UpdateBalance
import demo.wsc.beta.model.transport.AdminProfile
import demo.wsc.beta.repository.*
import org.springframework.stereotype.Service
import java.util.*

@Service
    class ServiceAdminProvider : ServiceAdmin {

    @Autowired
    private lateinit var repoCard: WSCCardsRepository

    @Autowired
    private lateinit var repoOwner: WSCOwnerRepository

    @Autowired
    private lateinit var repoCustomer: CustomerDetailsRepository

    @Autowired
    private lateinit var repoCredit: CreditRepository

    @Autowired
    private lateinit var repoCusTrans: CustomerTransactionsRepository

    @Autowired
    private lateinit var serviceMail: ServiceMailProvider

    @Autowired
    private lateinit var repoWscService: WSCServicesRepository

    /**
     * Adding a new card to repository
     *
     * @param 'CardDetails-DTO'
     * @return - boolen
     */
    @Throws(WSCExceptionInvalidModeldata::class)
    override fun publishCard(card: WSCCards): Boolean {
        return if (card.interestRate > 0 && card.creditAmount > 0 && card.cardOffers.isNotEmpty() && card.instalmentPeriod.isNotEmpty()) {
            repoCard.save(card)
            true
        } else {
            throw WSCExceptionInvalidModeldata()
        }
    }

    /**
     * Adding a new owner to repository
     *
     * @param 'OwnerDetails-DTO'
     * @return - boolen
     */
    @Throws(WSCExceptionInvalidModeldata::class)
    override fun publishOwner(owner: WSCOwner): Boolean {
         if (owner.bankName != null && owner.amount!! > 0 && owner.accountNo!! > 0 && owner.branchId > 0 && owner.ifscCode != null && owner.password != null) {
            owner.password = (encode(owner.password!!))
            repoOwner.save(owner)
             return true
        }else {
            throw WSCExceptionInvalidModeldata()
        }
    }

    /**
     * Get all the elegibile customers from repository in a list
     *
     * @param 'NA
     * @return - List for customers
     */
    override fun getEligibleCustomers(): List<CustomerDetails> {
        return repoCustomer.findByCardEligibility()
    }

    /**
     * Get all the Organizations from repository in a list
     *
     * @param 'NA
     * @return - List for customers
     */
   override fun getOrganizations(): List<WSCServices>{
      return repoWscService.findAll()
    }

    /**
     * Providing the credit card to the customer
     *
     * @param 'CustomerDetails-DTO'
     * @return - TransactionStatus-DTO
     */
    @Transactional
    @Throws(WSCExceptionInsufficientFund::class, MessagingException::class)
    override fun allowCredit(details: AllowCredit): AllowCreditStatus {
        val customerId = details.customerId
        val branchId = details.branchId
        val customer = repoCustomer.findById(customerId)
        if (customer.isPresent && customer.get().cardEligibility == 1) {
            val card = repoCard.findById(customer.get().cardType!!)
            if (card.isPresent) {
                val branch = repoOwner.findById(branchId)
                return if (branch.isPresent && branch.get().amount!! >= card.get().creditAmount) {
                    branch.get().amount = (branch.get().amount!! - card.get().creditAmount)
                    val credit = Credit()
                    val cardId = (Random().nextDouble() * 100000000000000L).toLong()
                    credit.cardNumber = (72.toString() + String.format("%014d", cardId))
                    credit.branchId=details.branchId
                    credit.customerId = (customerId)
                    credit.cardLimit = (card.get().creditAmount)
                    credit.cardType = (card.get().cardType)
                    credit.interestRate = (card.get().interestRate)
                    credit.instalmentPeriod = (card.get().instalmentPeriod)
                    credit.creditRecivedDate = (LocalDate.now())
                    credit.creditReciveDateShowUser = (LocalDate.now().toString())
                    credit.cardSpend = (0L)
                    credit.cardPendingInstalment = (0)
                    credit.cardPaidInstalment = (0)
                    credit.cardFlag = (0)
                    repoOwner.save(branch.get())
                    repoCredit.save(credit)

                    //Transasction
                    val transactions = CustomerTransactions()
                    transactions.transactionId = (String.format("%05d", Random().nextInt(100000)))
                    transactions.panId = (customer.get().panId)
                    transactions.receiverAccount = (customer.get().accountNumber.toString())
                    transactions.senderAccount = (branch.get().accountNo.toString())
                    transactions.senderName = (branch.get().bankName)
                    transactions.transactionDate = (LocalDateTime.now())
                    transactions.transactionDateShowUser = (LocalDate.now().toString())
                    transactions.amount = (card.get().creditAmount)
                    transactions.interest="0"
                    transactions.transactionDetails = (
                            "Added to credit card " + credit.cardNumber + " form " + branch.get().bankName
                            )
                    repoCusTrans.save(transactions)
                    serviceMail.sendCredit(
                        repoCustomer.findById(customerId).get().email,
                        card.get().creditAmount,
                        credit.cardNumber
                    )
                    AllowCreditStatus("success", card.get().creditAmount)
                } else {
                    throw WSCExceptionInsufficientFund()
                }
            }
        }
        return AllowCreditStatus()
    }

    /**
     * Update the balance into bank account
     *
     * @param 'AdminDetails-DTO'
     * @return - TransactionStatus-DTO
     */
    @Transactional
    @Throws(WSCExceptionInvalidUser::class)
    override fun updateBalance(balance: UpdateBalance): UpdateBalance {
        return if (repoOwner.findById(balance.branchId).isPresent) {
            val owner = repoOwner.findById(balance.branchId).get()
            owner.amount = (owner.amount!! + balance.amount)
            repoOwner.save(owner)
            UpdateBalance(balance.branchId, owner.amount!!)
        } else {
            throw WSCExceptionInvalidUser()
        }
    }

    /**
     * Get the admin profile based on the branchId
     *
     * @param 'branch Id'
     * @return - BranchDetails-DTO
     */
    @Throws(WSCExceptionInvalidUser::class)
    override fun getProfile(brandchId: Int): AdminProfile {
        return if (repoOwner.findById(brandchId).isPresent) {
            val data = repoOwner.findById(brandchId).get()
            AdminProfile(
                data.branchId,
                data.ifscCode,
                data.accountNo,
                data.bankName,
                data.returnInterest
            )
        } else {
            throw WSCExceptionInvalidUser()
        }
    }

    /**
     * Update the account status for other organizations
     *
     * @param 'email Id'
     * @return - Boolean
     */
    @Throws(WSCExceptionInvalidUser::class)
    override fun activateToken(email: String): Boolean {
        if (repoWscService.findByOrganisationEmail(email).isPresent) {
            val user = repoWscService.findByOrganisationEmail(email).get()
            user.accountFlag = 1
            repoWscService.save(user)
            return true
        } else {
            throw WSCExceptionInvalidUser()
        }
    }
}