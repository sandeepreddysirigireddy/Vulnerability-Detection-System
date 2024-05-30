package credStorage.cred.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import credStorage.cred.client.ClientModel;
import credStorage.cred.model.CredDto;
import credStorage.cred.model.CredStore;
import credStorage.cred.model.CredVul;
import credStorage.cred.model.Sanitize;
import credStorage.cred.service.CredStoreService;
@RestController
public class credController {
    // public int a=0;

    // private static RestTemplate restTemplate;


    @Autowired
    private CredStoreService credStoreService;

    @Autowired
    private ClientModel clientModel;
    // public credController() {
    //     credController.restTemplate = new RestTemplate();
    // }

//    public static User  getId(int id)
//    {

//     String url="http://localhost:8080/users/findUser/"+id;
//     User  u=restTemplate.getForObject(url, User.class);
//     return u;
//    }

    @PostMapping("/store")
    public ResponseEntity<?> storeCred(@RequestBody CredStore credStore)
    {
        System.out.println(credStore.getUserId());
        User r=clientModel.getUserById(credStore.getUserId());
//        User r = service1Client.getUserById(credStore.getUserId());
        
//
       if(r==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User record not found! Register");
        }
//        
    if (CredVul.containsHtmlOrJsTag(credStore.getCredName()) || CredVul.containsHtmlOrJsTag(credStore.getUserName()) || CredVul.containsHtmlOrJsTag(credStore.getPassword())) {
            // HttpSession session=request.getSession(true);
            // session.setAttribute("credStore",credStore);
            
            // System.out.println("session datra stored");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Potential XSS attack detected");
        }
    return credStoreService.storeCredentials(credStore);

    }


    @PostMapping("/store/sanitize")
    public ResponseEntity<?> storeSanitize(@RequestBody CredStore credStore)

    {
// System.out.println("entered");
        
//     HttpSession session=request.getSession(false);
//     if(session==null)
// {
//     return ResponseEntity.badRequest().body("no credentials data found from previous registration");
// }
//     CredStore credStore = (CredStore) session.getAttribute("credStore");
//     if (credStore == null) 
// {
//         return ResponseEntity.badRequest().body("No credentials data found from previous registration.");
// }

        credStore.setCredName(Sanitize.sanitizeInput(credStore.getCredName()));
        credStore.setUserName(Sanitize.sanitizeInput(credStore.getUserName()));
        credStore.setPassword(Sanitize.sanitizeInput(credStore.getPassword()));

        if(credStore.getCredName().length()==0)
        {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("name of credentail is empty after sanitization");
        }
        if(credStore.getPassword().length()==0)
        {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("password of credentail is empty after sanitization");
        }
        if(credStore.getUserName().length()==0)
        {
        	return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("User name is empty after sanitization");
        }
    return credStoreService.storeCredentials(credStore);
    }
    @GetMapping("/getCred/{credId}")
    public ResponseEntity<?> fetchCred(@PathVariable int credId)
    {
 return credStoreService.getUserCred(credId);
    }


    @PutMapping("/delete/{userId}/{credId}")
    public ResponseEntity<?> deleteCred(@PathVariable int userId, @PathVariable int credId)
    {
        System.out.println(userId+"  "+credId);
        return credStoreService.deleteCredRecord(userId,credId);
    }

    @PostMapping("/changeCred/{userId}/{credId}")
    public ResponseEntity<?> changeCred(@PathVariable int userId,@PathVariable int credId , @RequestBody CredDto credDto)

    {
        
    if (CredVul.containsHtmlOrJsTag(credDto.getCredName()) || CredVul.containsHtmlOrJsTag(credDto.getUserName()) || CredVul.containsHtmlOrJsTag(credDto.getPassword())) {
    //   /  HttpSession session=request.getSession(true);
    //     session.setAttribute("credChange",credDto);
    //     session.setAttribute("credId", credId);
    //     System.out.println("session datra stored");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Potential  attack detected");
    }
        return credStoreService.updateCred(userId,credId,credDto);
    }

    @PostMapping("/changeCred/{userId}/{credId}/sanitize")
    public ResponseEntity<?> changeCredSanitize(@PathVariable int userId,@PathVariable int credId , @RequestBody CredDto credDto )
    {

        System.out.println("OOOOOO");

//     HttpSession session=request.getSession(false);
//     if(session==null)
// {
//     return ResponseEntity.badRequest().body("no credentials data found from previous registration");
// }
//     CredDto credDto1 = (CredDto) session.getAttribute("credChange");
    // int cid=(int)session.getAttribute("credId");

        credDto.setCredName(Sanitize.sanitizeInput(credDto.getCredName()));
        credDto.setUserName(Sanitize.sanitizeInput(credDto.getUserName()));
        credDto.setPassword(Sanitize.sanitizeInput(credDto.getPassword()));

        credDto.setCredName(credDto.getCredName().replace("&#64;", "@"));
        System.out.println(credDto.getCredName());
        credDto.setUserName(credDto.getUserName().replace("&#64;", "@"));
        credDto.setPassword(credDto.getPassword().replace("&#64;", "@"));
if(credDto.getCredName().length()==0)
return ResponseEntity.badRequest().body("Credential Name is empty after sanitization! enter new credential name");


if(credDto.getPassword().length()==0)
return ResponseEntity.badRequest().body("Credential password is empty after sanitization! enter new password");

if(credDto.getUserName().length()==0)
return ResponseEntity.badRequest().body("Credential user name is empty after sanitization! enter new user name");

        return credStoreService.updateCred(userId,credId, credDto);
    }

    
    @GetMapping("getAllCred/{userId}")
    public ResponseEntity<?> getAllCredUser(@PathVariable int userId)
    {
    	System.out.println("hello ---------------------------");
        return credStoreService.getCredForUser(userId);
    }

}
