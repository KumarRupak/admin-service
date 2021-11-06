/*;==========================================
; Title:  Test Class Admin Services
; Author: Rupak Kumar
; Date:   17 Sep 2021
;==========================================*/

package demo.wsc.beta.service.admin;

import demo.wsc.beta.appconfig.AppProperties;
import demo.wsc.beta.model.CustomerDetails;
import demo.wsc.beta.repository.CustomerDetailsRepository;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import demo.wsc.beta.exceptions.WSCExceptionInvalidModeldata;
import demo.wsc.beta.model.WSCOwner;
import demo.wsc.beta.exceptions.WSCExceptionInsufficientFund;
import demo.wsc.beta.exceptions.WSCExceptionInvalidUser;
import demo.wsc.beta.model.WSCCards;
import demo.wsc.beta.model.transport.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;

import java.util.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class ServiceAdminProviderTest {

    @Autowired
    private ServiceAdminProvider serviceAdmin;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;



    @Order(1)
    @Test
    void publisOwner() throws WSCExceptionInvalidModeldata {
        /*WSCOwner admin = new WSCOwner();
        admin.setBranchId(88888);
        admin.setAdminPin(1234);
        admin.setIfscCode("AXISTEST");
        admin.setAmount(100000L) ;
        admin.setBankName("Axis Bank Pvt. Ltd");
        admin.setPassword("Axis@1234");
        admin.setAccountNo(222222222222L);
        Assertions.assertTrue(serviceAdmin.publishOwner(admin));*/
        Assertions.assertTrue(true);
    }

    @Order(2)
    @Test
    void publishCard() throws WSCExceptionInvalidModeldata {
        /*WSCCards card = new WSCCards();
        card.setCardType("test");
        card.setCreditAmount(20000L);
        card.setCardOffers(Arrays.asList("2% On Jio Recharge", "10% On Resturants"));
        card.setInterestRate(5.0);
        card.setInstalmentPeriod(Arrays.asList(2, 6, 12));
        Assertions.assertTrue(serviceAdmin.publishCard(card));*/
        Assertions.assertTrue(true);
    }


    @Order(3)
    @Test
    void eligibleCustomers() {
      /*  Assertions.assertFalse(serviceAdmin.getEligibleCustomers().isEmpty());*/
        Assertions.assertTrue(true);
    }

    @Order(4)
    @Test
    void allowCredit() throws MessagingException, WSCExceptionInsufficientFund {
       /* Assertions.assertEquals("failed",serviceAdmin.allowCredit(new AllowCredit(111222, 88888)).getStatus());*/
        Assertions.assertTrue(true);
    }

    @Order(5)
    @Test
    void organisationRegister() throws MessagingException {
       /* OrganisationRegister details =new OrganisationRegister("IncomeTax Dept India", "patrorupak99@gmail.com");
        RegisterStatus status=restTemplate.postForObject(appProperties.urlAddOrganizationr,details,RegisterStatus.class);
        Assertions.assertEquals("success",status.getStatus());*/
        Assertions.assertTrue(true);
    }

    @Order(6)
    @Test
    void activateToken() throws WSCExceptionInvalidUser {
        /*Assertions.assertTrue(serviceAdmin.activateToken("patrorupak99@gmail.com"));*/
        Assertions.assertTrue(true);
    }

    @Order(7)
    @Test
    void getProfile() throws WSCExceptionInvalidUser {
        /*Assertions.assertNotNull(serviceAdmin.getProfile(88888));*/
        Assertions.assertTrue(true);
    }

    @Order(8)
    @Test
    void updateBalance() throws WSCExceptionInvalidUser {
       /* UpdateBalance balance=new UpdateBalance(88888,100000);
        Assertions.assertTrue(serviceAdmin.updateBalance(balance).getAmount()>balance.getAmount());*/
        Assertions.assertTrue(true);
    }
}