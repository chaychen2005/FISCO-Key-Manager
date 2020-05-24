
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
package org.fisco.bcos.key.mgr.account;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.fisco.bcos.key.mgr.account.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import org.fisco.bcos.key.mgr.base.code.ConstantCode;
import org.fisco.bcos.key.mgr.base.controller.BaseController;
import org.fisco.bcos.key.mgr.base.entity.BasePageResponse;
import org.fisco.bcos.key.mgr.base.entity.BaseResponse;
import org.fisco.bcos.key.mgr.base.enums.SqlSortType;
import org.fisco.bcos.key.mgr.base.exception.KeyMgrException;
import org.fisco.bcos.key.mgr.base.properties.ConstantProperties;
import org.fisco.bcos.key.mgr.base.tools.KeyMgrTools;
import org.fisco.bcos.key.mgr.token.TokenService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "account")
public class AccountController extends BaseController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenService tokenService;
    
    /**
     * add account info.
     */
    @PostMapping(value = "/addAccount")
    @PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BaseResponse addAccountInfo(@RequestBody @Valid AccountInfo info, BindingResult result)
        throws KeyMgrException {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start addAccountInfo. startTime:{} accountInfo:{}", startTime.toEpochMilli(),
            JSON.toJSONString(info));

        // current
        String currentAccount = getCurrentAccount(request);

        // add account row
        accountService.addAccountRow(info, currentAccount);

        // query row
        TbAccountInfo tbAccount = accountService.queryByAccount(info.getAccount());
        tbAccount.setAccountPwd(null);
        baseResponse.setData(tbAccount);

        log.info("end addAccountInfo useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * query account list.
     */
    @GetMapping(value = "/accountList/{pageNumber}/{pageSize}")
    @PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BasePageResponse queryAccountList(@PathVariable("pageNumber") Integer pageNumber,
        @PathVariable("pageSize") Integer pageSize) throws KeyMgrException {
        BasePageResponse pagesponse = new BasePageResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();

        String account = getCurrentAccount(request);
        log.info("start queryAccountList.  startTime:{} pageNumber:{} pageSize:{} account:{} ",
            startTime.toEpochMilli(), pageNumber, pageSize, account);

        int count = accountService.countOfAccount(account);
        if (count > 0) {
            Integer start = Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize)
                .orElse(0);
            AccountListParam param = new AccountListParam(start, pageSize, account,
                SqlSortType.DESC.getValue());
            List<TbAccountInfo> listOfAccount = accountService.listOfAccount(param);
            listOfAccount.stream().forEach(accountData -> accountData.setAccountPwd(null));
            pagesponse.setData(listOfAccount);
            pagesponse.setTotalCount(count);
        }

        log.info("end queryAccountList useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(pagesponse));
        return pagesponse;
    }

    /**
     * get public key.
     */
    @GetMapping(value = "/getPublicKey")
    @PreAuthorize(ConstantProperties.HAS_ROLE_VISITOR)
    public BaseResponse getPublicKey() throws KeyMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start getPublicKey. startTime:{}", startTime.toEpochMilli());

        TbAccountInfo tbCurAccount = accountService.queryByAccount(getCurrentAccount(request));
        TbAccountInfo tbCreatorAccount = accountService.queryByAccount(tbCurAccount.getCreator());
        PublicKeyInfo publicKeyInfo = new PublicKeyInfo(tbCreatorAccount.getAccount(), tbCreatorAccount.getPublicKey());
        baseResponse.setData(publicKeyInfo);

        log.info("end getPublicKey. useTime:{} result:{}", Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * delete account by id.
     */
    @DeleteMapping(value = "/deleteAccount/{account}")
    @PreAuthorize(ConstantProperties.HAS_ROLE_ADMIN)
    public BaseResponse deleteAccount(@PathVariable("account") String account)
        throws KeyMgrException {
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start deleteAccount. startTime:{} account:{}", startTime.toEpochMilli(), account);

        String currentAccount = getCurrentAccount(request);
        TbAccountInfo tbCurAccount = accountService.queryByAccount(account);
        if (currentAccount == account || tbCurAccount == null || !currentAccount.equals(tbCurAccount.getCreator())) {
            log.info("lack of access to delete account");
            throw new KeyMgrException(ConstantCode.LACK_ACCESS_ACCOUNT);
        }

        accountService.deleteAccountRow(account);

        log.info("end deleteAccount. useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }

    /**
     * update password.
     */
    @PutMapping(value = "/updatePassword")
    public BaseResponse updatePassword(@RequestBody @Valid PasswordInfo info, HttpServletRequest request, 
            BindingResult result) throws KeyMgrException {
        checkBindResult(result);
        BaseResponse baseResponse = new BaseResponse(ConstantCode.SUCCESS);
        Instant startTime = Instant.now();
        log.info("start updatePassword startTime:{} passwordInfo:{}", startTime.toEpochMilli(),
            JSON.toJSONString(info));

        String targetAccount = getCurrentAccount(request);

        // update account row
        accountService
            .updatePassword(targetAccount, info.getOldAccountPwd(), info.getNewAccountPwd());

        log.info("end updatePassword useTime:{} result:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(baseResponse));
        return baseResponse;
    }
    
    /**
     * get current account.
     */
    private String getCurrentAccount(HttpServletRequest request) {
        String token = KeyMgrTools.getToken(request);
		log.debug("getCurrentAccount account:{}", token);
        return tokenService.getValueFromToken(token);
    }
}
