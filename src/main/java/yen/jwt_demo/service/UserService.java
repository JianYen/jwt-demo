package yen.jwt_demo.service;

import yen.jwt_demo.entity.UserEntity;

import java.util.Optional;


public interface UserService {

    /**
     * 根據username取得User實體
     * @param username
     * @return
     */
    Optional<UserEntity> getUserByUsername(String username);

    /**
     * 驗證token
     * @param token
     * @return
     */
    boolean verifyToken(String token);

    /**
     * 登出
     * @param username
     */
    void logout(String username);

    /**
     * 儲存
     * @param userEntity
     * @return
     */
    UserEntity save(UserEntity userEntity);
}
