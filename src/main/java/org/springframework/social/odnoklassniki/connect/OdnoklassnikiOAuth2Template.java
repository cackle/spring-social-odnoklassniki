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

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Odnoklassnikiru-specific extension of OAuth2Template.
 * @author Cackle
 */
public class OdnoklassnikiOAuth2Template extends OAuth2Template {

    private String uid;

	public OdnoklassnikiOAuth2Template(String clientId, String clientSecret) {
		super(clientId, clientSecret, "http://www.odnoklassniki.ru/oauth/authorize", "http://api.odnoklassniki.ru/oauth/token.do");
	}

    @Override
    protected AccessGrant createAccessGrant(String accessToken, String scope, String refreshToken, Integer expiresIn, Map<String, Object> response) {
        uid = (String) response.get("x_mailru_vid");
        return super.createAccessGrant(accessToken, scope, refreshToken, expiresIn, response);
    }

    public String getUid() {
        return uid;
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();

        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJacksonHttpMessageConverter) {
                MappingJacksonHttpMessageConverter jsonConverter = (MappingJacksonHttpMessageConverter) converter;

                List<MediaType> mTypes = new LinkedList<MediaType>(jsonConverter.getSupportedMediaTypes());
                mTypes.add(new MediaType("text", "javascript", MappingJacksonHttpMessageConverter.DEFAULT_CHARSET));
                jsonConverter.setSupportedMediaTypes(mTypes);
            }
        }

        return restTemplate;
    }
}
