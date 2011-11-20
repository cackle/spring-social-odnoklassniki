package org.springframework.social.odnoklassniki.api.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.social.odnoklassniki.api.OdnoklassnikiProfile;
import org.springframework.social.odnoklassniki.api.UsersOperations;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * User operations.
 * @author Cackle
 */
public class UsersTemplate extends AbstractOdnoklassnikiOperations implements UsersOperations {

    private static final String METHOD = "users.getCurrentUser";

    private final RestTemplate restTemplate;

    public UsersTemplate(String applicationKey, String clientSecret, RestTemplate restTemplate,
        String accessToken, boolean isAuthorizedForUser) {

        super(applicationKey, clientSecret, accessToken, isAuthorizedForUser);
        this.restTemplate = restTemplate;
    }

    @Override
    public OdnoklassnikiProfile getProfile() {
        requireAuthorization();

        Map<String, String> params = new HashMap<String, String>();
        params.put("method", METHOD);
        URI uri = URIBuilder.fromUri(makeOperationURL(params)).build();

        Map<String, String> profiles = restTemplate.getForObject(uri, Map.class);
        //checkForError(profiles);

        OdnoklassnikiProfile profile = new OdnoklassnikiProfile(profiles.get("uid"),
            profiles.get("first_name"), profiles.get("last_name"), profiles.get("email"),
            "http://odnoklassniki.ru?id=" + profiles.get("uid"));

        profile.setPhoto(profiles.get("pic_1"));

        return profile;
    }
}
