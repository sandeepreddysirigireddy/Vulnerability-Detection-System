package credStorage.cred.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import credStorage.cred.controller.User;

@FeignClient(name="micro1")
public interface ClientModel {
	  @GetMapping("/users/findUser/{id}")
	    User getUserById(@PathVariable int id);
 
}