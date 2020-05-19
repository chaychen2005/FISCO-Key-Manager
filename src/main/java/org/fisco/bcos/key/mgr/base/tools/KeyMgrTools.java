/**
 * Copyright 2014-2020  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.fisco.bcos.key.mgr.base.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.fisco.bcos.key.mgr.base.code.ConstantCode;
import org.fisco.bcos.key.mgr.base.code.RetCode;
import org.fisco.bcos.key.mgr.base.entity.BaseResponse;
import org.fisco.bcos.key.mgr.base.exception.KeyMgrException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

/**
 * common method.
 */
@Log4j2
public class KeyMgrTools {

    public static final String TOKEN_HEADER_NAME = "AuthorizationToken";
    private static final String TOKEN_START = "Token";

    /**
     * convert timestamp to localDateTime.
     */
    public static LocalDateTime timestamp2LocalDateTime(Long inputTime) {
        if (inputTime == null) {
            log.warn("timestamp2LocalDateTime fail. inputTime is null");
            return null;
        }
        Instant instant = Instant.ofEpochMilli(inputTime);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * encode String by sha.
     */
    public static String shaEncode(String inStr) {

        byte[] byteArray = new byte[0];
        try {
            byteArray = inStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("shaEncode fail:", e);
            return null;
        }
        byte[] hashValue = getHashValue(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < hashValue.length; i++) {
            int val = ((int) hashValue[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * get hash value
     * type: sha256 or sm3
     */
    public static byte[] getHashValue(byte[] byteArray) {
        byte[] hashResult;

        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
            hashResult = sha.digest(byteArray);
            return hashResult;
        } catch (Exception e) {
            log.error("shaEncode getHashValue fail:", e);
            return null;
        }
    }

    /**
     * sort list and convert to String.
     */
    private static String list2SortString(List<String> values) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }

        values.removeAll(Collections.singleton(null));// remove null
        Collections.sort(values);

        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * is json.
     */
    public static boolean isJSON(String str) {
        boolean result;
        try {
            JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * response string.
     */
    public static void responseString(HttpServletResponse response, String str) {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SYSTEM_EXCEPTION);
        if (StringUtils.isNotBlank(str)) {
            baseResponse.setMessage(str);
        }

        RetCode retCode;
        if (isJSON(str) && (retCode = JSONObject.parseObject(str, RetCode.class)) != null) {
            baseResponse = new BaseResponse(retCode);
        }

        try {
            response.getWriter().write(JSON.toJSONString(baseResponse));
        } catch (IOException e) {
            log.error("fail responseRetCodeException", e);
        }
    }

    /**
     * check account format.
     */
    public static boolean checkUserName(String userName) {
        // Begins with a letter, between 5 and 20 in length, and contain only characters, Numbers, and underscores.
        String regExp = "^[a-zA-Z][a-zA-Z0-9_]{4,19}$";
        return userName.matches(regExp);
    }

    /**
     * check password format.
     */
    public static boolean checkPassword(String password) {
        // Between 6 and 20 in length, and contain only characters, Numbers, and underscores.
        String regExp = "^[0-9a-zA-Z_]{6,20}$";
        return password.matches(regExp);
    }

    /**
     * get token.
     */
    public static synchronized String getToken(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_HEADER_NAME);
        if (StringUtils.isBlank(header)) {
            log.error("not found token");
            throw new KeyMgrException(ConstantCode.INVALID_TOKEN);
        }

        String token = StringUtils.removeStart(header, TOKEN_START).trim();
        if (StringUtils.isBlank(token)) {
            log.error("token is empty");
            throw new KeyMgrException(ConstantCode.INVALID_TOKEN);
        }
        return token;
    }
}
