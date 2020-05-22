/**
 * Copyright 2014-2020  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fisco.bcos.key.mgr.base.exception;

import com.alibaba.fastjson.JSON;
import org.fisco.bcos.key.mgr.base.code.RetCode;

/**
 * business exception.
 */
public class KeyMgrException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private RetCode retCode;

    /**
     * init by RetCode.
     */
    public KeyMgrException(RetCode retCode) {
        super(retCode.getMessage());
        this.retCode = retCode;
    }

    /**
     * init by RetCode and Throwable.
     */
    public KeyMgrException(RetCode retCode, Throwable cause) {
        super(retCode.getMessage(), cause);
        retCode.setMessage(JSON.toJSONString(cause.getMessage()));
        this.retCode = retCode;
    }

    /**
     * init by code and msg.
     */
    public KeyMgrException(int code, String msg) {
        super(msg);
        this.retCode = new RetCode(code, msg);
    }

    /**
     * init by code/msg and Throwable.
     */
    public KeyMgrException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.retCode = new RetCode(code, msg);
    }

    public RetCode getRetCode() {
        return retCode;
    }
}
