/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.module.core.models;

import java.util.Collections;
import java.util.List;

public class CsvPayloadInfo {

    private final String[] firstRow;
    private final int numberOfColumns;
    private final List<String[]> payload;

    public CsvPayloadInfo() {

        this(new String[]{}, 0, Collections.emptyList());
    }

    public CsvPayloadInfo(String[] firstRow, int numberOfColumns, List<String[]> payload) {

        this.firstRow = firstRow;
        this.numberOfColumns = numberOfColumns;
        this.payload = payload;
    }

    public String[] getFirstRow() {

        return firstRow.clone();
    }

    public int getNumberOfColumns() {

        return numberOfColumns;
    }

    public List<String[]> getPayload() {

        return payload;
    }
}
