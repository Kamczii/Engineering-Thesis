package org.once_a_day.sso.repository;


import org.once_a_day.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findBySsoId(UUID ssoId);

    @Query(value = "SELECT * FROM USERS WHERE USER_ID IN (SELECT CASE WHEN M.USER_ONE_ID = U.USER_ID THEN M.USER_TWO_ID ELSE M.USER_ONE_ID END FROM USERS U LEFT JOIN MATCHES M ON U.USER_ID IN (M.USER_ONE_ID, M.USER_TWO_ID) WHERE U.USER_ID = ?1 ORDER BY m.created_date desc)", nativeQuery = true)
    List<User> findMatchedUser(Long userId);

    Optional<User> findByUsername(String username);
}
