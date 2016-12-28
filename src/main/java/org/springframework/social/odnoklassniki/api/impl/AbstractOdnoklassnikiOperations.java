package org.springframework.social.odnoklassniki.api.impl;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.DigestUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.springframework.social.odnoklassniki.api.Odnoklassniki.PROVIDER_ID;

public abstract class AbstractOdnoklassnikiOperations {

    private static final String ODNOKLASSNIKI_REST_URL = "http://api.odnoklassniki.ru/fb.do?";

    private final SortedMap<String, String> params = new TreeMap<String, String>();

    private final boolean isAuthorized;

    private final String accessToken;

    private final String clientSecret;

    public AbstractOdnoklassnikiOperations(String applicationKey, String clientSecret, String accessToken,
                                           boolean isAuthorized) {

        this.isAuthorized = isAuthorized;
        this.accessToken = accessToken;
        this.clientSecret = clientSecret;

        params.put("application_key", applicationKey);
        params.put("access_token", this.accessToken);
        params.put("format", "json");
    }

    protected void requireAuthorization() {
        if (!isAuthorized) {
            throw new MissingAuthorizationException(PROVIDER_ID);
        }
    }

    protected String makeOperationURL(Map<String, String> params) {
        this.params.putAll(params);

        StringBuilder url = new StringBuilder(ODNOKLASSNIKI_REST_URL);
        StringBuilder signature = new StringBuilder();

        for (String param : this.params.keySet()) {
            String value = this.params.get(param);
            if (!param.equals("access_token")) {
                signature.append(param).append("=").append(value);
            }
            url.append(param).append("=").append(URLEncoder.encode(value)).append("&");
        }
        signature.append(encodeSignature(accessToken + clientSecret));
        url.append("sig=").append(encodeSignature(signature.toString()));

        return url.toString();
    }

    private String encodeSignature(String sign) {
        return DigestUtils.md5DigestAsHex(sign.getBytes()).toLowerCase();
    }
}
