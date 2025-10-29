package org.merra.config;

import java.util.HashMap;
import java.util.Map;

import org.merra.dto.UserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final WebClient webClient;

    public GoogleOpaqueTokenIntrospector(WebClient wb) {
        this.webClient = wb;
    }

    /*
     * Token sent from the frontend (in the Authorization header)
     * then receives it as an input parameter in this method.
     */
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // Use google client to call Google API
        String oauth2UserInfo = "/oauth2/v3/userinfo";
        var user = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(oauth2UserInfo).queryParam("access_token", token).build())
                .retrieve()
                .bodyToMono(UserInfo.class)
                .block();

        if (user == null) {
            throw new OAuth2IntrospectionException("User response was null");
        }
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", user.sub());
        attributes.put("name", user.name());
        attributes.put("email", user.email());
        System.out.println(">>>>> GoogleOpaqueTokenIntrospector <<<<<");
        System.out.println(user);
        return new OAuth2IntrospectionAuthenticatedPrincipal(user.name(), attributes, null);
    }

}
