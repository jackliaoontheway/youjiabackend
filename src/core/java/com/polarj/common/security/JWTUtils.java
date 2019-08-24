package com.polarj.common.security;

import org.slf4j.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JWTUtils
{
    private static String HASHED_LOGIN_NAME = "hashName";

    // 验证token
    public static boolean verify(String token, String hashedLoginName, String passwordHash, Logger logger)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(passwordHash);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(HASHED_LOGIN_NAME, hashedLoginName).build();
            verifier.verify(token);
            return true;
        }
        catch (Exception e)
        {
            if (logger != null)
            {
                logger.error(e.getMessage(), e);
            }
            return false;
        }
    }

    public static String getHashedLoginName(String token, Logger logger)
    {
        try
        {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(HASHED_LOGIN_NAME).asString();
        }
        catch (Exception e)
        {
            if (logger != null)
            {
                logger.error(e.getMessage(), e);
            }
            return null;
        }
    }

    // 生成token
    public static String sign(String hashedLoginName, String passwordHash, Logger logger)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(passwordHash);
            return JWT.create().withClaim(HASHED_LOGIN_NAME, hashedLoginName).sign(algorithm);
        }
        catch (Exception e)
        {
            if (logger != null)
            {
                logger.error(e.getMessage(), e);
            }
            return null;
        }
    }
}
