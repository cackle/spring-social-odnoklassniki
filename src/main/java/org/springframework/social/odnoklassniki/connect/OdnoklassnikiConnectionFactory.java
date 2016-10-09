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

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.odnoklassniki.api.Odnoklassniki;

/**
 * Odnoklassniki ConnectionFactory implementation.
 * @author Cackle
 */
public class OdnoklassnikiConnectionFactory extends OAuth2ConnectionFactory<Odnoklassniki> {

	public OdnoklassnikiConnectionFactory(String clientId, String clientSecret, String applicationKey) {
		super(Odnoklassniki.PROVIDER_ID, new OdnoklassnikiServiceProvider(clientId, clientSecret, applicationKey),
				new OdnoklassnikiAdapter());
	}
}
