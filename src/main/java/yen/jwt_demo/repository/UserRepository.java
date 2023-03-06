package yen.jwt_demo.repository;

import yen.jwt_demo.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    /**
     * 根據使用者名稱查找資料
     * @param username
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByUsername(String username);

}
