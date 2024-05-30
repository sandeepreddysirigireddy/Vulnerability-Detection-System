package usermgmt.umgmt.service;

import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import usermgmt.umgmt.model.Sanitize;
import usermgmt.umgmt.model.User;
import usermgmt.umgmt.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> registerUser(User user) {
        
    	User u=userRepository.findByEmail(user.getEmail());

        // if(userRepository.existsByEmail(user.getEmail()))
        // {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("account aldready exists with email id "+user.getEmail());
        // }
       
        if(u==null)
        {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("acc aldready exists");
        
       // userRepository.save(user);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok().body("user  registered sccessfully with id "+user.getId());}
        else
        {
          return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("acc aldready exists");

        }
    }

    

    public ResponseEntity<?> fetchUserDetails(int id) {

        User u=userRepository.findById(id);
        if(u==null)
        {
return ResponseEntity.badRequest().body("no user record found");
        }
        User u1=u;
        u1.setName(StringEscapeUtils.unescapeHtml4(u1.getName()));
        u1.setEmail(StringEscapeUtils.unescapeHtml4(u1.getEmail()));
        u1.setPassword(StringEscapeUtils.unescapeHtml4(u1.getPassword()));
System.out.println(u1);
        return ResponseEntity.ok(u1);
    }



    public ResponseEntity<?> doLogin(String email, String password) {

        User u=userRepository.findByEmail(email);

        if(u==null)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid emial");
        }
        String name= StringEscapeUtils.unescapeHtml4(u.getName());
       // String password1=StringEscapeUtils.unescapeHtml4(u.getPassword());
        // String email1=StringEscapeUtils.unescapeHtml4(u.getEmail());
        String cmp=StringEscapeUtils.unescapeHtml4(u.getPassword());
        String password1=StringEscapeUtils.unescapeHtml4(password);
       
        if(!cmp.equals(password1)){
            System.out.println(cmp+" "+password1);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("name", name);
        responseBody.put("id", u.getId());
        responseBody.put("email", email);
        responseBody.put("message", name + " with id "+u.getId()+"   logged in successfully");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }}
