/**
 * Copyright 2014-2020  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fisco.bcos.key.mgr.keyescrow;

import com.alibaba.fastjson.JSON;
import org.fisco.bcos.key.mgr.base.code.ConstantCode;
import org.fisco.bcos.key.mgr.base.exception.KeyMgrException;
import org.fisco.bcos.key.mgr.keyescrow.entity.KeyListParam;
import org.fisco.bcos.key.mgr.keyescrow.entity.PrivateKeyInfo;
import org.fisco.bcos.key.mgr.keyescrow.entity.TbPKeyInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * services for key data.
 */
@Log4j2
@Service
public class KeyEscrowService {

    @Autowired
    private KeyEscrowMapper keyEscrowMapper;

    /**
     * add key row.
     */
    public void addKeyRow(String account, PrivateKeyInfo keyInfo) throws KeyMgrException {
        log.debug("start addKeyRow. account:{}, key info:{}", account, JSON.toJSONString(keyInfo));

        String keyAlias = keyInfo.getKeyAlias();
        String cipherText = keyInfo.getCipherText();
        String privateKey = keyInfo.getPrivateKey();

        // check account with key
        accountWithKeyNotExist(account, keyAlias);

        // add account row
        TbPKeyInfo rowInfo = new TbPKeyInfo(account, keyAlias, cipherText, privateKey);
        Integer affectRow = keyEscrowMapper.addKeyRow(rowInfo);

        // check result
        checkDbAffectRow(affectRow);

        log.debug("end addKeyRow. affectRow:{}", affectRow);
    }

    /**
     * query the key of an account.
     */
    public TbPKeyInfo queryByAccountWithKey(String account, String keyAlias) {
        log.debug("start queryByAccountWithKey. account:{}, keyAlias:{} ", account, keyAlias);
        TbPKeyInfo keyRow = keyEscrowMapper.queryByAccountWithKey(account, keyAlias);
        log.debug("end queryByAccountWithKey. accountRow:{} ", JSON.toJSONString(keyRow));
        return keyRow;
    }

    /**
     * query count of key by account.
     */
    public int countOfKeyByAccount(String account) {
        log.debug("start countOfKeyByAccount. account:{} ", account);
        Integer keyCount = keyEscrowMapper.countOfKeyByAccount(account);
        int count = keyCount == null ? 0 : keyCount.intValue();
        log.debug("end countOfKeyByAccount. count:{} ", count);
        return count;
    }

    /**
     * query key list by account.
     */
    public List<TbPKeyInfo> listOfKeyByAccount(KeyListParam param) {
        log.debug("start listOfKeyByAccount. param:{} ", JSON.toJSONString(param));
        List<TbPKeyInfo> list = keyEscrowMapper.listOfKeyByAccount(param);
        log.debug("end listOfKeyByAccount. list:{} ", JSON.toJSONString(list));
        return list;
    }

    /**
     * delete key info.
     */
    public void deleteKeyRow(String account, String keyAlias) throws KeyMgrException {
        log.debug("start deleteKeyRow. account:{}, keyAlias:{} ", account, keyAlias);

        // check account
        accountWithKeyExist(account, keyAlias);

        // delete account row
        Integer affectRow = keyEscrowMapper.deleteKeyRow(account, keyAlias);

        // check result
        checkDbAffectRow(affectRow);

        log.debug("end deleteKeyRow. affectRow:{} ", affectRow);
    }

    /**
     * query count of account with key.
     */
    public int countOfAccountWithKey(String account, String keyAlias) {
        log.debug("start countOfAccountWithKey. account:{} keyAlias:{} ", account, keyAlias);
        Integer accountCount = keyEscrowMapper.countOfAccountWithKey(account, keyAlias);
        int count = accountCount == null ? 0 : accountCount.intValue();
        log.debug("end countOfAccountWithKey. count:{} ", count);
        return count;
    }

    /**
     * boolean the key of an account is exist.
     */
    public void accountWithKeyExist(String account, String keyAlias) throws KeyMgrException {
        if (StringUtils.isBlank(account)) {
            throw new KeyMgrException(ConstantCode.ACCOUNT_NAME_EMPTY);
        }
        if (StringUtils.isBlank(keyAlias)) {
            throw new KeyMgrException(ConstantCode.KEY_ALIASES_EMPTY);
        }
        int count = countOfAccountWithKey(account, keyAlias);
        if (count == 0) {
            throw new KeyMgrException(ConstantCode.KEY_NOT_EXISTS);
        }
    }

    /**
     * boolean the key of an account is not exist.
     */
    public void accountWithKeyNotExist(String account, String keyAlias) throws KeyMgrException {
        if (StringUtils.isBlank(account)) {
            throw new KeyMgrException(ConstantCode.ACCOUNT_NAME_EMPTY);
        }
        if (StringUtils.isBlank(keyAlias)) {
            throw new KeyMgrException(ConstantCode.KEY_ALIASES_EMPTY);
        }
        int count = countOfAccountWithKey(account, keyAlias);
        if (count > 0) {
            throw new KeyMgrException(ConstantCode.KEY_EXISTS);
        }
    }

    /**
     * check db affect row.
     */
    private void checkDbAffectRow(Integer affectRow) throws KeyMgrException {
        if (affectRow == 0) {
            log.warn("affect 0 rows of tb_key_info");
            throw new KeyMgrException(ConstantCode.DB_EXCEPTION);
        }
    }
}
