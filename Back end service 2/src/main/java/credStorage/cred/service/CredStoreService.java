package credStorage.cred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.text.StringEscapeUtils;
import credStorage.cred.model.CredDto;
import credStorage.cred.model.CredStore;
import credStorage.cred.repository.CredStoreRepo;
@Service
public class CredStoreService {


    @Autowired
    private CredStoreRepo storeRepo;

    // @Autowired
    // private Service1Client service1Client;

    public ResponseEntity<?> storeCredentials(CredStore credStore) {
  
Map<String, Object> response = new HashMap<>();
//  // Check if the user exists
//  Optional<CredStore> existingUser = storeRepo.findFirstByUserId(credStore.getUserId());
//  if (existingUser.isEmpty()) {
//      response.put("message", "User account with ID " + credStore.getUserId() + " not found");
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//  }
// User r = service1Client.getUserById(credStore.getUserId());

// if(r==null){
//     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User record not found! Register");
// }
 Optional<CredStore> existingCred = storeRepo.findFirstByUserIdAndCredName(credStore.getUserId(), credStore.getCredName());
 if (existingCred.isPresent()) {
     response.put("message", "Credential name " + credStore.getCredName() + " already exists for user account with user ID " + credStore.getUserId());
     return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
 }

 // Save the new credential
 storeRepo.saveAndFlush(credStore);
 response.put("message", credStore.getUserName() + " credentials stored successfully with cred ID " + credStore.getCredId());
 return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);



}

    
    public ResponseEntity<?> getUserCred(int credId) {
        // TODO Auto-generated method stub

        CredStore credentialStore=storeRepo.findByCredId(credId);

        if(credentialStore==null)
        {
            return ResponseEntity.badRequest().body("Credentials record not found");
        }
        // u1.setName(StringEscapeUtils.unescapeHtml4(u1.getName()));

        CredStore c=credentialStore;
        c.setCredName(StringEscapeUtils.unescapeHtml4(c.getCredName()));
        c.setPassword(StringEscapeUtils.unescapeHtml4(c.getPassword()));
        c.setUserName(StringEscapeUtils.unescapeHtml4(c.getUserName()));

        return ResponseEntity.ok(credentialStore);
    }
    public ResponseEntity<?> deleteCredRecord(int userId,int credId) {
        // TODO Auto-generated method stub

        // CredStore credStore=storeRepo.findByCredId(credId);
        // if(credStore==null)
        // {
        //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user record was not found with cred id "+credId
        //     );
        //    // return ResponseEntity.badRequest().body("no record was found with credId  "+credId);
        // }
        // else
        // {
        //     storeRepo.deleteById(credId);
        //     //storeRepo.saveAndFlush(credStore);
        //     return ResponseEntity.status(HttpStatus.FOUND).body("record with credId  "+credId+"  deleted successfully");
        // }
        Iterable<CredStore> allRecords = storeRepo.findAll();
System.out.println(userId+" "+credId);
        // Iterate over each record
        for (CredStore record : allRecords) {
            // Check if the record matches the given userId and credId
            System.out.println(userId+" "+credId);
            if (record.getUserId() == userId && record.getCredId() == credId) {
                // If found, delete the record and return success response
                
                storeRepo.deleteById(credId);
                //storeRepo.saveAndFlush(record);
                return ResponseEntity.status(HttpStatus.FOUND).body("Record with credId " + credId + " deleted successfully");
            }
        }

        // If no matching record is found, return not found response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User record with credId " + credId + " not found");
    }
    
    



    public ResponseEntity<?> updateCred(int userId,int credId, CredDto credDto)
    {
        Iterable<CredStore> allRecords = storeRepo.findAll();
        for(CredStore record:allRecords)
        {
            if(record.getUserId()==userId && record.getCredId()==credId)
            {
                String res="";
                if(!record.getCredName().equals(credDto.getCredName())){
                record.setCredName(credDto.getCredName());
                 res=res+" Credential name ";
                }
         
                if(!record.getUserName().equals(credDto.getUserName())){
                record.setUserName(credDto.getUserName());
                res=res+" User name ";
                }
         
                if(!record.getPassword().equals(credDto.getPassword())){
                record.setPassword(credDto.getPassword());
                 res=res+" Password ";
                }
         
                res=res+" updated successfully";
         
                storeRepo.saveAndFlush(record);
         
         
                 return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
            }
        }

    //    CredStore credStore=storeRepo.findByCredId(credId);
    //    if(credStore==null)
    //    {
    //     return ResponseEntity.badRequest().body("No records were foud with credId  "+credId);

    //    }

    //    String res="";
    //    if(!credStore.getCredName().equals(credDto.getCredName())){
    //    credStore.setCredName(credDto.getCredName());
    //     res=res+" credName ";
    //    }

    //    if(!credStore.getUserName().equals(credDto.getUserName())){
    //    credStore.setUserName(credDto.getUserName());
    //    res=res+" userName ";
    //    }

    //    if(!credStore.getPassword().equals(credDto.getPassword())){
    //    credStore.setPassword(credDto.getPassword());
    //     res=res+" password ";
    //    }

    //    res=res+" updated successfully";

    //    storeRepo.saveAndFlush(credStore);

System.out.println("Not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no records found with credId "+credId);
    }


    public ResponseEntity<?> getCredForUser(int userId) {
        
       Iterable<CredStore> allRecords = storeRepo.findAll();

        // Convert Iterable to Stream
        Stream<CredStore> recordStream = StreamSupport.stream(allRecords.spliterator(), false);

        // Filter records by userId, map them to a map object and collect them into a list
        java.util.List<Map<String, Object>> result = recordStream
                .filter(record -> record.getUserId() == userId)
                .map(record -> {
                    Map<String, Object> credMap = new HashMap<>();
                    credMap.put("credId", record.getCredId());
                    credMap.put("credName", record.getCredName());
                    credMap.put("userName", record.getUserName());
                    credMap.put("password", record.getPassword());
                    return credMap;
                })
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No records found for user ID " + userId);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

}
