package org.merra.controller;

import java.io.IOException;
import java.util.List;

import org.merra.api.ApiResponse;
import org.merra.dto.Oauth2Response;
import org.merra.dto.Oauth2UrlResponse;
import org.merra.entities.UserAccount;
import org.merra.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("auth/")
public class Oauth2Controller {
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;
    @Value("${app.frontend.url}")
    private String appUrl;
    @Value("${app.frontend-redirect.url}")
    private String appRedirectUrl;

    private final UserAccountRepository userAccountRepository;

    public Oauth2Controller(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("url")
    public ResponseEntity<ApiResponse> auth() {
        /*
         * This will generate the link where the user see the login form of Google.
         */
        String url = new GoogleAuthorizationCodeRequestUrl(clientId, appRedirectUrl,
                List.of("email", "profile", "openid")).build();

        ApiResponse apiRes = new ApiResponse(
                "Oauth2 link generated.",
                true,
                HttpStatus.OK,
                new Oauth2UrlResponse(url));
        return ResponseEntity.ok(apiRes);
    }

    @GetMapping("callback")
    public ResponseEntity<ApiResponse> callback(@RequestParam("code") String code) throws IOException {
        /*
         * This token will be sent back to the frontend and will be used in
         * Authorization header for every authenticated requests.
         */
        String accessToken = null;
        String idToken = null;
        String email = null;
        String name = null;
        String pictureUrl = null;

        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
                    new GsonFactory(), clientId,
                    clientSecret, code, appRedirectUrl)
                    .execute();

            GoogleIdToken parsedIdToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = parsedIdToken.getPayload();

            email = payload.getEmail();
            name = (String) payload.get("name");
            pictureUrl = (String) payload.get("picture");
            accessToken = tokenResponse.getAccessToken();
            idToken = tokenResponse.getIdToken();

            if (userAccountRepository.existsByEmail(email) == 0) {
                var saveGoogleUserAccount = new UserAccount();
                saveGoogleUserAccount.setEmail(email);
                saveGoogleUserAccount.setProfileUrl(pictureUrl);
                userAccountRepository.save(saveGoogleUserAccount);
            }

        } catch (TokenResponseException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ApiResponse res = new ApiResponse(
                "Oauth2 login successful.",
                true,
                HttpStatus.OK,
                new Oauth2Response(accessToken, idToken, email, name, pictureUrl));

        return ResponseEntity.ok(res);
    }

}
