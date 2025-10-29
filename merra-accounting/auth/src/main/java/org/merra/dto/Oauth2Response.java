package org.merra.dto;

public record Oauth2Response(
        String accessToken,
        String idToken,
        String email,
        String name,
        String pictureUrl) {

}
