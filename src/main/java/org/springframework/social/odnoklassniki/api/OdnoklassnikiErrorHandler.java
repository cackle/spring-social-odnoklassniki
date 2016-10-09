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
package org.springframework.social.odnoklassniki.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.*;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

import static org.springframework.social.odnoklassniki.api.Odnoklassniki.PROVIDER_ID;

/**
 * Subclass of {@link DefaultResponseErrorHandler} that handles errors from Odnoklassnikiru's
 * API, interpreting them into appropriate exceptions.
 * @author Cackle
 */
public class OdnoklassnikiErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
			handleServerErrors(statusCode);
		} else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
			handleClientErrors(response);
		}

		// if not otherwise handled, do default handling and wrap with UncategorizedApiException
		try {
			super.handleError(response);
		} catch(Exception e) {
			throw new UncategorizedApiException(PROVIDER_ID, "Error consuming Odnoklassnikiru REST API", e);
		}
	}

	private void handleClientErrors(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();

		if (statusCode == HttpStatus.UNAUTHORIZED) {
			throw new NotAuthorizedException(PROVIDER_ID, "User was not authorised.");
		} else if (statusCode == HttpStatus.FORBIDDEN) {
			throw new OperationNotPermittedException(PROVIDER_ID, "User is forbidden to access this resource.");
		} else if (statusCode == HttpStatus.NOT_FOUND) {
			throw new ResourceNotFoundException(PROVIDER_ID, "Resource was not found.");
        }
	}

	private void handleServerErrors(HttpStatus statusCode) throws IOException {
		if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new InternalServerErrorException(PROVIDER_ID, "Something is broken at Odnoklassnikiru.");
		} else if (statusCode == HttpStatus.BAD_GATEWAY) {
			throw new ServerDownException(PROVIDER_ID, "Odnoklassnikiru is down or is being upgraded.");
		} else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE) {
			throw new ServerOverloadedException(PROVIDER_ID,
					"Odnoklassnikiru is overloaded with requests. Try again later.");
		}
	}
}
