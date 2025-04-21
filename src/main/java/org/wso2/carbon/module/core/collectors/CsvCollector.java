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
package org.wso2.carbon.module.core.collectors;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.collect.Sets;
import org.wso2.carbon.module.core.SimpleMessageContext;
import org.wso2.carbon.module.core.exceptions.SimpleMessageContextException;
import org.wso2.carbon.module.core.writers.CSVWriterWithQuote;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.UNORDERED;

/**
 * Collector to collect a Stream of String array to a Text payload and set the payload content
 */
public class CsvCollector implements Collector<String[], List<String[]>, Boolean> {

    private final SimpleMessageContext simpleMessageContext;
    private final String[] header;
    private final char separator;
    private final char escapeCharacter;
    private final boolean applyQuotes;

    /**
     * Create new instance with Message context and header
     *
     * @param simpleMessageContext SimpleMessageContext to use
     * @param header               Headers to append to the top of the csv. If this is null, then no headers would be added
     */
    public CsvCollector(SimpleMessageContext simpleMessageContext, String[] header) {

        this.simpleMessageContext = simpleMessageContext;
        this.header = header;
        this.separator = CSVWriter.DEFAULT_SEPARATOR;
        this.escapeCharacter = CSVWriter.DEFAULT_ESCAPE_CHARACTER;
        this.applyQuotes = false;
    }

    /**
     * Create new instance with Message context and header
     *
     * @param simpleMessageContext SimpleMessageContext to use
     * @param header               Headers to append to the top of the csv. If this is null, then no headers would be added
     * @param separator            Separator to be used in the CSV content
     */
    public CsvCollector(SimpleMessageContext simpleMessageContext, String[] header, char separator) {

        this.simpleMessageContext = simpleMessageContext;
        this.header = header;
        this.separator = separator;
        this.escapeCharacter = CSVWriter.DEFAULT_ESCAPE_CHARACTER;
        this.applyQuotes = false;
    }

    /**
     * Create new instance with Message context and header
     *
     * @param simpleMessageContext SimpleMessageContext to use
     * @param header               Headers to append to the top of the csv. If this is null, then no headers would be added
     * @param separator            Separator to be used in the CSV content
     */
    public CsvCollector(SimpleMessageContext simpleMessageContext, String[] header, char separator,
                        boolean suppressEscapeCharacter) {

        this.simpleMessageContext = simpleMessageContext;
        this.header = header;
        this.separator = separator;
        if (suppressEscapeCharacter) {
            this.escapeCharacter = CSVWriter.NO_ESCAPE_CHARACTER;
        } else {
            this.escapeCharacter = CSVWriter.DEFAULT_ESCAPE_CHARACTER;
        }
        this.applyQuotes = false;
    }

    /**
     * Create new instance with Message context and header
     *
     * @param simpleMessageContext SimpleMessageContext to use
     * @param header               Headers to append to the top of the csv. If this is null, then no headers would be added
     * @param separator            Separator to be used in the CSV content
     * @param applyQuotes          Whether to apply quotes for the Fields with line breaks, commas, or double quotes
     */
    public CsvCollector(SimpleMessageContext simpleMessageContext, String[] header, char separator,
                        boolean suppressEscapeCharacter, boolean applyQuotes) {

        this.simpleMessageContext = simpleMessageContext;
        this.header = header;
        this.separator = separator;
        this.applyQuotes = applyQuotes;
        if (suppressEscapeCharacter) {
            this.escapeCharacter = CSVWriter.NO_ESCAPE_CHARACTER;
        } else {
            this.escapeCharacter = CSVWriter.DEFAULT_ESCAPE_CHARACTER;
        }
    }

    @Override
    public Supplier<List<String[]>> supplier() {

        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<String[]>, String[]> accumulator() {

        return List::add;
    }

    @Override
    public BinaryOperator<List<String[]>> combiner() {

        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<String[]>, Boolean> finisher() {

        return rowList -> {
            StringWriter stringWriter = new StringWriter();

            if (header != null && header.length != 0) {
                rowList.add(0, header);
            }

            CSVWriter csvWriter;
            if (applyQuotes) {
                csvWriter = new CSVWriterWithQuote(stringWriter, separator, CSVWriter.DEFAULT_QUOTE_CHARACTER, escapeCharacter, CSVWriter.DEFAULT_LINE_END);
            } else {
                csvWriter = new CSVWriter(stringWriter, separator, CSVWriter.NO_QUOTE_CHARACTER, escapeCharacter, CSVWriter.DEFAULT_LINE_END);
            }
            csvWriter.writeAll(rowList);
            try {
                csvWriter.close();
                stringWriter.flush();
                String resultPayload = stringWriter.toString();
                simpleMessageContext.setTextPayload(resultPayload);
            } catch (IOException e) {
                throw new SimpleMessageContextException(e);
            }

            return true;

        };
    }

    @Override
    public Set<Characteristics> characteristics() {

        return Sets.immutableEnumSet(UNORDERED);
    }
}
