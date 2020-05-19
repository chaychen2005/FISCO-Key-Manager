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
package org.fisco.bcos.key.mgr.base.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * constants.
 */
@Data
@Component
@ConfigurationProperties(prefix = ConstantProperties.CONSTANT_PREFIX)
public class ConstantProperties {

    @Autowired
    private ConstantProperties constants;

    // constant
    public static final String CONSTANT_PREFIX = "constant";
    public static final String HAS_ROLE_ADMIN = "hasRole('admin')";
    public static final String HAS_ROLE_VISITOR = "hasRole('visitor')";

    //receive http request
    private Integer authTokenMaxAge = 900; // seconds
    private Boolean isUseSecurity = true;

}