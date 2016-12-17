/*
 * Copyright (c) 2011 Google Inc.
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
//Using Sample Google Books API code for CMPE275 project implementation.
package edu.sjsu.cmpe275.model;


public class ClientCredentials {

    /** Value of the "API key" shown under "Simple API Access". */
    public static final String API_KEY = "AIzaSyDQdTP_y6l0Dhjqdb1iw7BGUo4VDAP8Tig";

    public static void errorIfNotSpecified() {
        if (API_KEY.startsWith("Enter ")) {
            System.err.println(API_KEY);
            System.exit(1);
        }
    }
}