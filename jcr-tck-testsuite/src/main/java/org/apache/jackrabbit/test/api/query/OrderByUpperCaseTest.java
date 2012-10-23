/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.test.api.query;

import javax.jcr.RepositoryException;
import javax.jcr.query.qom.DynamicOperand;

/**
 * <code>OrderByUpperCaseTest</code> contains test cases for order by queries
 * on upper cased property values.
 */
public class OrderByUpperCaseTest extends AbstractOrderByTest {

    public void testLowerCase() throws RepositoryException {
        populate(new String[]{"a", "AB", "abc", "aBCd"});
        checkOrder(new String[]{nodeName1, nodeName2, nodeName3, nodeName4});
    }

    protected DynamicOperand createOrderingOperand()
            throws RepositoryException {
        return qf.upperCase(super.createOrderingOperand());
    }

    protected String createSQL() {
        // no SQL equivalent
        return null;
    }

    protected String createXPath() throws RepositoryException {
        // no SQL equivalent
        return null;
    }
}