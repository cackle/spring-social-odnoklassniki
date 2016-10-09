/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.odnoklassniki.connect;

import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;

import java.util.Map;

/**
 * Odnoklassnikiru-specific extension of OAuth2Template.
 * @author Cackle
 */
public class OdnoklassnikiOAuth2Template extends OAuth2Template {

    private String uid;

	public OdnoklassnikiOAuth2Template(String clientId, String clientSecret) {
		super(clientId, clientSecret, "http://www.odnoklassniki.ru/oauth/authorize", "http://api.odnoklassniki.ru/oauth/token.do");
        setUseParametersForClientAuthentication(true);
	}

    @Override
    protected AccessGrant createAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn,
                                            Map<String, Object> response) {
        uid = (String) response.get("x_mailru_vid");
        return super.createAccessGrant(accessToken, scope, refreshToken, expiresIn, response);
    }

    public String getUid() {
        return uid;
    }
}
