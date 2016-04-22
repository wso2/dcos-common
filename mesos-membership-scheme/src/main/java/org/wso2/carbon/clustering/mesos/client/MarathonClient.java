/*
* Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.clustering.mesos.client;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.wso2.carbon.clustering.mesos.client.utils.MarathonException;
import org.wso2.carbon.clustering.mesos.client.utils.ModelUtils;

import static java.util.Arrays.asList;
import static org.wso2.carbon.clustering.mesos.MesosConstants.APPLICATION_JSON;

public class MarathonClient {

    public static Marathon getInstance(String endpoint) {
        return getInstance(endpoint, null);
    }

    /**
     * The generalized version of the method that allows more in-depth customizations via
     * {@link RequestInterceptor}s.
     *
     * @param endpoint URL of Marathon
     */
    public static Marathon getInstance(String endpoint, RequestInterceptor... interceptors) {
        Feign.Builder b = Feign.builder()
                .encoder(new GsonEncoder(ModelUtils.GSON))
                .decoder(new GsonDecoder(ModelUtils.GSON))
                .errorDecoder(new MarathonErrorDecoder());
        if (interceptors != null) {
            b.requestInterceptors(asList(interceptors));
        }
        b.requestInterceptor(new MarathonHeadersInterceptor());
        return b.target(Marathon.class, endpoint);
    }

    /**
     * Creates a Marathon client proxy that performs HTTP basic authentication.
     */
    public static Marathon getInstanceWithBasicAuth(String endpoint, String username, String password) {
        return getInstance(endpoint, new BasicAuthRequestInterceptor(username, password));
    }

    private static class MarathonHeadersInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
            template.header("Accept", APPLICATION_JSON);
            template.header("Content-Type", APPLICATION_JSON);
        }
    }

    private static class MarathonErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            return new MarathonException(response.status(), response.reason());
        }
    }
}
