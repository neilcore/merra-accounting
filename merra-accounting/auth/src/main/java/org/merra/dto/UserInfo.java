package org.merra.dto;

public record UserInfo(
                String exp,
                String iat,
                String iss,
                String sub,
                String name,
                String given_name,
                String family_name,
                String picture,
                String profile,
                String email,
                boolean email_verified,
                String locale) {

}
