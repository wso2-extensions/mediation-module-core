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

package org.wso2.carbon.module.core;

import com.google.gson.JsonElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wso2.carbon.module.core.models.IndexedElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonUtil.class})
public class SimpleMessageContextTest {

    @Mock
    private Axis2MessageContext messageContextMock;
    private SimpleMessageContext simpleMessageContext;

    @Before
    public void setUp() {

        simpleMessageContext = new SimpleMessageContext(messageContextMock);
        mockStatic(JsonUtil.class);
    }

    @Test
    public void getJsonStringTest_validInput_correctJsonShouldReturn() {

        final String inputJson = "{\"a\":\"a\"}";

        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        final String result = simpleMessageContext.getJsonString();

        assertEquals(inputJson, result);

    }

    @Test
    public void getJsonElementTest_validInput_correctJsonElementShouldReturn() {

        final String inputJson = "{\"a\":\"a\"}";
        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        JsonElement jsonElement = simpleMessageContext.getJsonElement();
        assertEquals(inputJson, jsonElement.toString());

    }

    @Test
    public void getJsonElementTest_invalidInput_emptyJsonShouldReturn() {

        final String inputJson = "\"a\":\"a\"}";
        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        JsonElement jsonElement = simpleMessageContext.getJsonElement();
        final String expected = "{}";
        final String result = jsonElement.toString();

        assertEquals(expected, result);

    }

    @Test
    public void getJsonArrayTest_inputIsAValidJsonArray_correctArrayShouldReturn() {

        final String inputJson = "[{\n" +
                "  \"id\": 1,\n" +
                "  \"first_name\": \"Hermon\",\n" +
                "  \"last_name\": \"Iacovolo\"\n" +
                "}, {\n" +
                "  \"id\": 2,\n" +
                "  \"first_name\": \"Susanne\",\n" +
                "  \"last_name\": \"Brame\"\n" +
                "}, {\n" +
                "  \"id\": 3,\n" +
                "  \"first_name\": \"Clemens\",\n" +
                "  \"last_name\": \"Quayle\"\n" +
                "}]";
        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        JsonElement resultElement = simpleMessageContext.getJsonArray();
        assertTrue(resultElement.isJsonArray());

        final String expected =
                "[{\"id\":1,\"first_name\":\"Hermon\",\"last_name\":\"Iacovolo\"},{\"id\":2,\"first_name\":\"Susanne\",\"last_name\":\"Brame\"},{\"id\":3,\"first_name\":\"Clemens\",\"last_name\":\"Quayle\"}]";
        final String result = resultElement.toString();

        assertEquals(expected, result);
    }

    @Test
    public void getJsonArrayTest_inputIsNotAValidJsonArray_emptyJsonArrayShouldReturn() {

        final String inputJson = "{\"a\":\"b\"}";
        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        JsonElement resultElement = simpleMessageContext.getJsonArray();
        assertTrue(resultElement.isJsonArray());

        final String expected = "[]";
        final String result = resultElement.toString();

        assertEquals(expected, result);
    }

    @Test
    public void getJsonObjectTest_validJsonObjectInput_correctJsonObjectShouldReturn() {

        final String inputJson = "{\n" +
                "  \"id\": 2,\n" +
                "  \"first_name\": \"Susanne\",\n" +
                "  \"last_name\": \"Brame\"\n" +
                "}";
        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        JsonElement resultElement = simpleMessageContext.getJsonObject();
        assertTrue(resultElement.isJsonObject());

        final String expected = "{\"id\":2,\"first_name\":\"Susanne\",\"last_name\":\"Brame\"}";
        final String result = resultElement.toString();

        assertEquals(expected, result);
    }

    @Test
    public void getJsonObjectTest_invalidJsonObjectInput_emptyJsonObjectShouldReturn() {

        final String inputJson = "\"a\":\"b\"}";
        when(JsonUtil.jsonPayloadToString(any())).thenReturn(inputJson);

        JsonElement resultElement = simpleMessageContext.getJsonObject();
        assertTrue(resultElement.isJsonObject());

        final String expected = "{}";
        final String result = resultElement.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testGetTextPayload_correctTextPayload_returnsActualPayload() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD);

        final String actual = simpleMessageContext.getTextPayload();
        final String expected = "id,name,email,phone_number\n" +
                "1,De witt Hambidge,dwitt0@newsvine.com,723-376-0325\n" +
                "2,Brody Dowthwaite,bdowthwaite1@delicious.com,557-258-6813\n" +
                "3,Catlin Drought,cdrought2@etsy.com,608-510-7991\n" +
                "4,Kissiah Douglass,kdouglass3@squarespace.com,903-543-9223\n" +
                "5,Robinette Udey,rudey4@nytimes.com,140-672-9856";

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTextPayload_incorrectTextPayload_returnsEmptyString() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);

        final String actual = simpleMessageContext.getTextPayload();
        final String expected = "";

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCsvArrayStream_validCsvPayload_returnsCorrectStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER);

        Stream<String[]> actual = simpleMessageContext.getCsvArrayStream();

        assertThat(actual).containsExactly(TestConstants.SOAP_CSV_VALUES);

    }

    @Test
    public void testGetCsvArrayStream_invalidCsvPayload_returnEmptyStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);

        Stream<String[]> actual = simpleMessageContext.getCsvArrayStream();
        assertNotNull(actual);
        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetCsvArrayStreamWithLinesToSkip_validParams_returnsCorrectStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER);

        Stream<String[]> actual = simpleMessageContext.getCsvArrayStream(1);

        assertThat(actual).containsExactly(Arrays.copyOfRange(TestConstants.SOAP_CSV_VALUES, 1, 6));
    }

    @Test
    public void testGetCsvArrayStreamWithLinesToSkip_invalidCsv_returnsEmptyStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);

        Stream<String[]> actual = simpleMessageContext.getCsvArrayStream(1);

        assertNotNull(actual);
        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetCsvArrayStreamWithIndex_validCsv_returnsCorrectStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER);

        Stream<IndexedElement<String[]>> actual = simpleMessageContext.getCsvArrayStreamWithIndex();
        List<IndexedElement<String[]>> actualList = actual.collect(Collectors.toList());
        Stream<Integer> indexStream = actualList.stream().map(IndexedElement::getIndex);
        Stream<String[]> valueStream = actualList.stream().map(IndexedElement::getElement);

        assertThat(valueStream).containsExactly(TestConstants.SOAP_CSV_VALUES);
        assertThat(indexStream).containsExactly(0, 1, 2, 3, 4, 5);

    }

    @Test
    public void testGetCsvArrayStreamWithIndex_invalidCsv_returnsEmptyStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);

        Stream<IndexedElement<String[]>> actual = simpleMessageContext.getCsvArrayStreamWithIndex();

        assertNotNull(actual);
        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetCsvArrayStreamWithIndexWithLinesToSkip_validCsv_returnsCorrectStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER);
        final int numberOfLinesToSkip = 1;

        Stream<IndexedElement<String[]>> actual = simpleMessageContext.getCsvArrayStreamWithIndex(numberOfLinesToSkip);
        List<IndexedElement<String[]>> actualList = actual.collect(Collectors.toList());
        Stream<Integer> indexStream = actualList.stream().map(IndexedElement::getIndex);
        Stream<String[]> valueStream = actualList.stream().map(IndexedElement::getElement);

        assertThat(valueStream).containsExactly(Arrays.copyOfRange(
                TestConstants.SOAP_CSV_VALUES, numberOfLinesToSkip, 6));
        assertThat(indexStream).containsExactly(0, 1, 2, 3, 4);

    }

    @Test
    public void testGetCsvArrayStreamWithIndexWithLinesToSKip_invalidCsv_returnsEmptyStream() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);
        final int numberOfLinesToSkip = 1;

        Stream<IndexedElement<String[]>> actual = simpleMessageContext.getCsvArrayStreamWithIndex(numberOfLinesToSkip);

        assertNotNull(actual);
        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetCsvPayload_validCsv_returnCorrectPayload() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER);

        List<String[]> actual = simpleMessageContext.getCsvPayload();

        assertThat(actual).containsExactly(TestConstants.SOAP_CSV_VALUES);

    }

    @Test
    public void testGetCsvPayload_invalidCsv_emptyListShouldReturn() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);

        List<String[]> actual = simpleMessageContext.getCsvPayload();

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

    }

    @Test
    public void testGetCsvPayloadLinesToSkip_validCsv_returnCorrectPayload() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER);
        final int numberOfLinesToSkip = 1;

        List<String[]> actual = simpleMessageContext.getCsvPayload(numberOfLinesToSkip);

        assertThat(actual).containsExactly(Arrays.copyOfRange(TestConstants.SOAP_CSV_VALUES, numberOfLinesToSkip, 6));

    }

    @Test
    public void testGetCsvPayloadLinesToSkip_invalidCsv_emptyListShouldReturn() {

        mockSoapEnvelop(TestConstants.SOAP_CSV_INVALID_PAYLOAD);
        final int numberOfLinesToSkip = 1;

        List<String[]> actual = simpleMessageContext.getCsvPayload(numberOfLinesToSkip);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

    }

    private void mockSoapEnvelop(String xmlPayload) {

        StAXSOAPModelBuilder builder =
                (StAXSOAPModelBuilder) OMXMLBuilderFactory
                        .createSOAPModelBuilder(new StringInputStream(xmlPayload), "UTF-8");
        SOAPEnvelope envelope = builder.getSOAPEnvelope();
        when(messageContextMock.getEnvelope()).thenReturn(envelope);

        MessageContext messageContext = new MessageContext();
        try {
            messageContext.setEnvelope(envelope);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        when(messageContextMock.getAxis2MessageContext()).thenReturn(messageContext);
    }

}