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

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.odnoklassniki.api.Odnoklassniki;
import org.springframework.social.odnoklassniki.api.OdnoklassnikiProfile;

/**
 * Odnoklassniki ApiAdapter implementation.
 * @author Cackle
 */
public class OdnoklassnikiAdapter implements ApiAdapter<Odnoklassniki> {

    @Override
	public boolean test(Odnoklassniki odnoklassnikiru) {
		try {
		    odnoklassnikiru.usersOperations().getProfile();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

    @Override
    public void setConnectionValues(Odnoklassniki odnoklassnikiru, ConnectionValues values) {
        OdnoklassnikiProfile profile = odnoklassnikiru.usersOperations().getProfile();
        values.setProviderUserId(profile.getUid());
        values.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
        values.setProfileUrl(profile.getLink());
        values.setImageUrl(profile.getPhoto());
    }

    @Override
    public UserProfile fetchUserProfile(Odnoklassniki odnoklassnikiru) {
        OdnoklassnikiProfile profile = odnoklassnikiru.usersOperations().getProfile();
        return new UserProfileBuilder()
            .setFirstName(profile.getFirstName())
            .setLastName(profile.getLastName())
            .setName(profile.getFirstName() + " " + profile.getLastName())
            .setEmail(profile.getEmail())
            .build();
    }

    @Override
    public void updateStatus(Odnoklassniki api, String message) {
    }
}
