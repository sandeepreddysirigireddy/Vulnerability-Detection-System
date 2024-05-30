package usermgmt.umgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import usermgmt.umgmt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	 boolean existsByName(String name);

	    boolean existsByEmail(String email);
	    
	    User findByName(String name);
	    
	    User findByEmail(String email);

		User findById(int id);
}
