package yen.jwt_demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    // 過期時間五分鐘
    private final static long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 產生签名,5分鐘後過期
     * @param username 使用者名稱
     * @param secret   使用者密碼
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 檢驗token是否正确
     * @param token  密鑰
     * @param secret 使用者密碼
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 獲得token中的信息不需要secret解密也能取得
     * @return token中包含的使用者名稱
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
