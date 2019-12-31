/**
 * Copyright 2020 Xianhua Liu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.json4orm.web.config;

/**
 * The Class JdbcConfig holds configuration variables for JDBC connection.
 *
 * @author Xianhua Liu
 */
public class JdbcConfig {

    /** The db url. */
    private String dbUrl;

    /** The db user. */
    private String dbUser;

    /** The db password. */
    private String dbPassword;

    /**
     * Gets the db url.
     *
     * @return the db url
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Sets the db url.
     *
     * @param dbUrl the new db url
     */
    public void setDbUrl(final String dbUrl) {
        this.dbUrl = dbUrl;
    }

    /**
     * Gets the db user.
     *
     * @return the db user
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Sets the db user.
     *
     * @param dbUser the new db user
     */
    public void setDbUser(final String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * Gets the db password.
     *
     * @return the db password
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Sets the db password.
     *
     * @param dbPassword the new db password
     */
    public void setDbPassword(final String dbPassword) {
        this.dbPassword = dbPassword;
    }

}
