/*; Title:  Entity  for Service Consumer Details
; Author: Rupak Kumar
; Date:   4 Oct 2021
;==========================================*/

package demo.wsc.beta.repository

import org.springframework.data.mongodb.repository.MongoRepository
import demo.wsc.beta.model.WSCServices
import java.util.*

interface WSCServicesRepository : MongoRepository<WSCServices, String>
{
    fun findByOrganisationEmail(email: String): Optional<WSCServices>
}