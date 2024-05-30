package credStorage.cred.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import credStorage.cred.model.CredStore;

@Repository
public interface CredStoreRepo extends JpaRepository<CredStore, Integer>{

    CredStore findByCredId(int credId);
    Optional<CredStore> findByUserIdAndCredId(int userId, int credId);
    Optional<CredStore> findByUserIdAndCredName(int userId, String credName);
    Optional<CredStore> findByUserId(int userId);
    boolean existsByUserIdAndCredName(int userId, String credName);
    Optional<CredStore> findFirstByUserId(int userId);
    Optional<CredStore> findFirstByUserIdAndCredName(int userId, String credName);

    boolean existsByUserId(int userId);

}
