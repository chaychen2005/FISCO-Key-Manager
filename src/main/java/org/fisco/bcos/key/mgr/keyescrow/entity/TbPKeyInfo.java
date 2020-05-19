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
package org.fisco.bcos.key.mgr.keyescrow.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Entity class of table tb_pkey.
 */
@Data
public class TbPKeyInfo {

    private String account;
    private String keyAlias;
    private String cipherText;
    private String privateKey;
    private LocalDateTime createTime;

    public TbPKeyInfo() {
        super();
    }

    public TbPKeyInfo(String account, String keyAlias) {
        super();
        this.account = account;
        this.keyAlias = keyAlias;
    }

    public TbPKeyInfo(String account, String keyAlias, String cipherText, String privateKey) {
        super();
        this.account = account;
        this.keyAlias = keyAlias;
        this.cipherText = cipherText;
        this.privateKey = privateKey;
    }
}
