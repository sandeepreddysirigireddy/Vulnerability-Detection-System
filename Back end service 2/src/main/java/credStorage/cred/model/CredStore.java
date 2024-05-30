package credStorage.cred.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CredStore {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int credId;

private int userId;

private String credName;

private String userName;

private String password;

public int getCredId() {
	return credId;
}

public void setCredId(int credId) {
	this.credId = credId;
}

public int getUserId() {
	return userId;
}

public void setUserId(int userId) {
	this.userId = userId;
}

public String getCredName() {
	return credName;
}

public void setCredName(String credName) {
	this.credName = credName;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}



}
