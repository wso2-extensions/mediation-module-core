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

public final class TestConstants {

    public static final String SOAP_CSV_PAYLOAD =
            "<?xml version='1.0' encoding='utf-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><text xmlns=\"http://ws.apache.org/commons/ns/payload\">" +
                    "id,name,email,phone_number\n" +
                    "1,De witt Hambidge,dwitt0@newsvine.com,723-376-0325\n" +
                    "2,Brody Dowthwaite,bdowthwaite1@delicious.com,557-258-6813\n" +
                    "3,Catlin Drought,cdrought2@etsy.com,608-510-7991\n" +
                    "4,Kissiah Douglass,kdouglass3@squarespace.com,903-543-9223\n" +
                    "5,Robinette Udey,rudey4@nytimes.com,140-672-9856</text></soapenv:Body></soapenv:Envelope>";

    public static final String SOAP_CSV_PAYLOAD_TO_TEST_CSV_READER =
            "<?xml version='1.0' encoding='utf-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><text xmlns=\"http://ws.apache.org/commons/ns/payload\">" +
                    "id,name,email,phone_number\n" +
                    "1,De witt Hambidge,dwitt0@newsvine.com,723-376-0325\n" +
                    "2,\"Brody, Dowthwaite\",bdowthwaite1@delicious.com,557-258-6813\n" +
                    "3,\"Catlin Drought\",cdrought2@etsy.com,608-510-7991\n" +
                    "4,Kissiah Douglass,kdouglass3@squarespace.com,903-543-9223\n" +
                    "5,Robinette Udey,rudey4@nytimes.com,140-672-9856</text></soapenv:Body></soapenv:Envelope>";

    public static final String SOAP_CSV_INVALID_PAYLOAD =
            "<?xml version='1.0' encoding='utf-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><hello></hello></soapenv:Body></soapenv:Envelope>";

    public static final String[][] SOAP_CSV_VALUES =
            new String[][]{new String[]{"id", "name", "email", "phone_number"},
                    new String[]{"1", "De witt Hambidge", "dwitt0@newsvine.com", "723-376-0325"},
                    new String[]{"2", "Brody, Dowthwaite", "bdowthwaite1@delicious.com", "557-258-6813"},
                    new String[]{"3", "Catlin Drought", "cdrought2@etsy.com", "608-510-7991"},
                    new String[]{"4", "Kissiah Douglass", "kdouglass3@squarespace.com", "903-543-9223"},
                    new String[]{"5", "Robinette Udey", "rudey4@nytimes.com", "140-672-9856"}};

    public static final String SOAP_JSON_PAYLOAD =
            "<?xml version='1.0' encoding='utf-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><jsonArray><jsonElement><id>1</id><name>Alanah O'Hartnett</name><email>aohartnett0@nature.com</email><gender>306-708-7173</gender></jsonElement><jsonElement><id>2</id><name>Carita Warre</name><email>cwarre1@bbb.org</email><gender>982-289-8928</gender></jsonElement><jsonElement><id>3</id><name>Juliane Wolsey</name><email>jwolsey2@pen.io</email><gender>962-530-4403</gender></jsonElement><jsonElement><id>4</id><name>Isabel Grollmann</name><email>igrollmann3@unblog.fr</email><gender>332-851-8493</gender></jsonElement><jsonElement><id>5</id><name>Myer Swafford</name><email>mswafford4@infoseek.co.jp</email><gender>615-650-9030</gender></jsonElement></jsonArray></soapenv:Body></soapenv:Envelope>";

}
