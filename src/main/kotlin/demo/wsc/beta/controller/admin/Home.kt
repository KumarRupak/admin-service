package demo.wsc.beta.controller.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Home {

    @GetMapping("/")
    fun home():String{
        return "----------------------------Admin Service-----------------------------------"
    }
}