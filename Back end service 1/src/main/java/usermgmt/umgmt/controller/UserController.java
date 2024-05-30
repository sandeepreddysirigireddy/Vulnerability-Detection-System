package usermgmt.umgmt.controller;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import usermgmt.umgmt.model.LoginRequest;
import usermgmt.umgmt.model.Sanitize;
import usermgmt.umgmt.model.User;
import usermgmt.umgmt.model.VulnerabiltiyTest;
import usermgmt.umgmt.repository.UserRepository;
import usermgmt.umgmt.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

       
        if (VulnerabiltiyTest.containsHtmlOrJsTag(user.getName()) || VulnerabiltiyTest.containsHtmlOrJsTag(user.getEmail()) || VulnerabiltiyTest.containsHtmlOrJsTag(user.getPassword())) {
            // HttpSession session=request.getSession(true);
            // session.setAttribute("user",user);
            // System.out.println("session datra stored");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return userService.registerUser(user);
    }


    @PostMapping("/register/sanitize")
    public ResponseEntity<?> registerSanitize(@RequestBody User user){
//     {
// HttpSession session=request.getSession(false);
// if(session==null)
// {
//     return ResponseEntity.badRequest().body("no user data found from previous registration");
// }
// User user = (User) session.getAttribute("user");
// if (user == null) {
//     return ResponseEntity.badRequest().body("No user data found from previous registration.");
// }
        if (VulnerabiltiyTest.containsHtmlOrJsTag(user.getEmail())){
      user.setEmail(Sanitize.sanitizeInput(user.getEmail()));}
      System.out.println(user.getEmail());
      if (VulnerabiltiyTest.containsHtmlOrJsTag(user.getName())){

      user.setName(Sanitize.sanitizeInput(user.getName()));}
      if (VulnerabiltiyTest.containsHtmlOrJsTag(user.getPassword())){

      user.setPassword(Sanitize.sanitizeInput(user.getPassword()));}
      user.setEmail(user.getEmail().replace("&#64;", "@"));
     user.setName(user.getName().replace("&#64;", "@"));
      user.setPassword(user.getPassword().replace("&#64;", "@"));
      if(user.getEmail().length()==0)
      {
        return ResponseEntity.badRequest().body("user email is empty after sanitization");

      }

      if(user.getName().length()==0)
      {
        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body("user name is empty after sanitization");

      }

      if(user.getPassword().length()==0)
      {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("user password is empty after sanitization");

      }
      return userService.registerUser(user);

 }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

       
        if (VulnerabiltiyTest.containsHtmlOrJsTag(loginRequest.getEmail()) || VulnerabiltiyTest.containsHtmlOrJsTag(loginRequest.getPassword())) {
            // HttpSession session=request.getSession(true);
            // session.setAttribute("loginreq", loginRequest);
            // System.out.println("Session data stored");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Potential XSS attack detected");

        }
        return userService.doLogin(loginRequest.getEmail(),loginRequest.getPassword());
        
    }

    


    @PostMapping("/login/sanitize")
    public ResponseEntity<?> loginSanitize(@RequestBody LoginRequest loginRequest)
    {

//         System.out.println("entered");
//         HttpSession session=request.getSession(false);
//         if(session==null)
//         {
//             System.out.println("session enterred");
//             System.out.println("okk"+loginRequest.getPassword()+"ok or nor");
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no user data found from login request");
//         }
        

// LoginRequest lgnrequest=(LoginRequest)session.getAttribute("loginreq");

// if(lgnrequest==null)
// {
//     return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("no login data found");
// }
System.out.println("1"+loginRequest.getEmail());
loginRequest.setEmail(Sanitize.sanitizeInput(loginRequest.getEmail()));
System.out.println(2+loginRequest.getEmail());
loginRequest.setPassword(Sanitize.sanitizeInput(loginRequest.getPassword()));
if(loginRequest.getEmail().length()==0)
{
    return ResponseEntity.badRequest().body("user email is empty after sanitization");
}

if(loginRequest.getPassword().length()==0)
{
    return ResponseEntity.badRequest().body("user password is empty after saitization");
}


return userService.doLogin(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @GetMapping("/findUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id)
    {
    	        User u=userRepo.findById(id);
    	        System.out.println(u.getId());
       
        return ResponseEntity.ok(u);
        
    }



    @GetMapping("/getDetails/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable int id)
    {

        return userService.fetchUserDetails(id);
    }

}
