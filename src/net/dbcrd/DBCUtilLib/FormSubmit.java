/*
 * Copyright (c) 2015, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.dbcrd.DBCUtilLib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <strong>a Singleton that connects to {@code "http://docanalysisreporter.dbcrd.net/mailForm.php"} to submit
 * e-mail content to be sent by dbcrd.net</strong>
 *
 */
@ClassPreamble(
        author = "Daniel B. Curtis",
        date = "Aug 2013 ",
        currentRevision = 1,
        lastModified = "9/04/2013",
        copyright = "(C) 2013 by Daniel B. Curtis, all rights reserved",
        lastModifiedBy = "Daniel B. Curtis"
)
public enum FormSubmit {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    private static final Logger THE_LOGGER = Logger.getLogger(FormSubmit.class.getName());

    /**
     *
     */
    private static final Object SYNC = new Object();

    /**
     * <strong>The fields for the e-mail to be submitted</strong>
     */
    public enum EmailFields {

        /**
         * Code for the subject
         */
        SUBJECT("GeuLLGBWdS"),
        /**
         * code for the message
         */
        MESSAGE("Z7ddf5c6209"),
        /**
         * code for the attachment
         */
        ATTACH("affef14d61"),
        /**
         * code for filename
         */
        FILENAME("d454c54b745"),
        /**
         * code for html
         */
        HTML("i4u4n9lph7");

        /**
         *
         */
        final private String value;

        /**
         *
         * @param value
         */
        EmailFields(String value) {
            this.value = value;
        }

        /**
         *
         * @return
         */
        private String getValue() {
            return value;
        }
    }

    /**
     *
     */
    private static final String URL = "http://docanalysisreporter.dbcrd.net/mailForm.php";

    /**
     * submits data to the URL
     * @param data a {@code Map<EmailFields, String>} 
     *
     * @throws MalformedURLException if...
     * @throws IOException if ...
     * @throws java.net.UnknownHostException if...
     * @throws java.net.ConnectException if ...
     */
    public void doSubmit(Map<EmailFields, String> data) throws MalformedURLException,
            IOException, UnknownHostException, java.net.ConnectException {
        doSubmit(URL, data);
    }

    /**
     *
     * @param url a String of a url
     * @param data a {@code Map<EmailFields,String>} of value pairs. 
     *
     * @throws MalformedURLException if ...
     * @throws IOException if ...
     * @throws java.net.UnknownHostException if ...
     * @throws java.net.ConnectException if ...
     */
    private void doSubmit(String url, Map<EmailFields, String> data)
            throws MalformedURLException, IOException, UnknownHostException, java.net.ConnectException {

        synchronized (SYNC) {
            final URL siteUrl = new URL(url);
            final HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            try (final DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {

                final StringBuilder sb = new StringBuilder(1000);
                for (Entry<EmailFields, String> e : data.entrySet()) {  //construct the url arguments.
                    sb.append('&').append(e.getKey().getValue()).append('=').
                            append(URLEncoder.encode(e.getValue(), "UTF-8"));
                }
                sb.deleteCharAt(0);
                // System.out.println(content);
                out.writeBytes(sb.toString());
                out.flush();
            }
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                final StringBuilder sb1 = new StringBuilder(10000);
                String line;
                while ((line = in.readLine()) != null) {
                    sb1.append(line).append('\n');
                }
                //("(?<=<label>).*?(?=</label>)
                final Matcher bdyMx = Pattern.
                        compile("(?<=<body>).*(?=</body>)", Pattern.CASE_INSENSITIVE + Pattern.DOTALL).
                        matcher(sb1);
                if (bdyMx.find()) {
                    final String dd = bdyMx.group().trim();
                    if (!dd.isEmpty()) {
                        if (!"Message sent!".equals(dd.trim())) {
                            THE_LOGGER.severe(dd);
                        }
                    }
                }
            }
        }
    }
}
