package com.polarj.common.utility;

import java.security.MessageDigest;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CryptoUtil
{
    private static Logger logger = LoggerFactory.getLogger(CryptoUtil.class);

    private static final String[] SaltBase = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "0", ".", "-", "*", "/", "'", ":", ";", ">", "<", "~", "!", "@", "#", "$", "%",
            "^", "&", "(", ")", "{", "}", "[", "]", "|" };

    private static final int SaltLength = 32; // must be greater than 8

    private static final String HashType = "SHA-256";

    private static final String EncodingSet = "UTF-8";

    private static final int EncodeCount = 1000;

    public static String generateSalt()
    {

        StringBuffer result = new StringBuffer();
        Random r = new Random();
        int temp = 0;
        for (int i = 0; i < SaltLength; i++)
        {
            temp = r.nextInt(SaltBase.length);
            result.append(SaltBase[temp]);
        }
        return result.toString();
    }

    public static String generateHash(String plaintest)
    {
        return hashPassword(plaintest, "");
    }

    public static String hashPassword(String plaintext, String salt)
    {
        String ret = "";
        ret = hashPwdWithSalt(plaintext, salt);
        return ret;
    }

    public static boolean validatePassword(String encPwd, String pwd, String salt)
    {
        if (null == encPwd)
        {
            return false;
        }
        boolean ret = false;
        String newEncPwd = hashPwdWithSalt(pwd, salt);
        ret = encPwd.equals(newEncPwd);
        return ret;
    }

    private static String hashPwdWithSalt(String pwd, String salt)
    {
        String ret = "";
        try
        {
            String joinStr = joinSaltAndPwd(salt, pwd);
            ret = encodeJoinStr(joinStr);
        }
        catch (Exception e)
        {
            ret = "";
            logger.error(e.getMessage(), e);
        }
        return ret;
    }

    private static String joinSaltAndPwd(String salt, String pwd)
    {
        if (salt.length() != SaltLength)
        {
            return pwd + salt;
        }
        String temp1 = salt.substring(0, 8);
        String temp2 = salt.substring(8, salt.length());
        return temp2 + pwd + temp1;
    }

    private static String encodeJoinStr(String joinStr)
    {

        if (joinStr == null)
            return null;

        String temp = joinStr;

        for (int i = 0; i < EncodeCount; i++)
        {
            temp = encrypt(temp);
        }
        return temp;
    }

    private static String bytes2Hex(byte[] bts)
    {
        StringBuffer des = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1)
            {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    private static String encrypt(String src)
    {
        try
        {
            byte[] btSource = src.getBytes(EncodingSet);
            MessageDigest md = MessageDigest.getInstance(HashType);
            md.reset();
            md.update(btSource);
            String result = bytes2Hex(md.digest()); // to HexString
            return result;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "";
        }
    }
}
