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
