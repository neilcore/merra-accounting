package org.merra.config;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${spring.app.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.app.jwt.access-token}")
    private int forAccessToken;
    
    /**
     * This method will extract the user-name from the token.
     * @param token - accepts token in {@linkplain java.util.String} format.
     * @return - {@linkplain java.util.String}
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * This method will generate and return the token.
     * @param userDetails - accepts {@linkplain UserDetails} object type.
     * @return - {@linkplain java.util.Map}
     */
    public Map<String, String> generateToken(UserDetails userDetails) {
    	Map<String, String> tokens = new HashMap<>();
    	for (String dur: Set.of("AT", "RT")) {
    		tokens.putAll(generateToken(new HashMap<>(), userDetails, dur));
    	}
        return tokens;
    }
    
    /**
     * This method build generate and build the token.
     * @param extractClaims - accepts {@linkplain java.util.Map} object.
     * @param userDetails - accepts {@linkplain UserDetails} object type.
     * @return - {@linkplain java.util.Map}
     */
    public Map<String, String> generateToken(
    		Map<String, Objects> extractClaims,
    		UserDetails userDetails,
    		String duration
    ) {
    	Map<String, String> tokens = new HashMap<>();
    	String tokenType = null;
    	Date expirationDate = null;
    	if (duration.equalsIgnoreCase("AT")) {
    		tokenType = "accessToken";
    		expirationDate = new Date(System.currentTimeMillis() + forAccessToken);
    	} else if (duration.equalsIgnoreCase("RT")) {
    		tokenType = "refreshToken";
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 5); // add 5 days
            Date refreshTokenExpirationDate = cal.getTime();
            expirationDate = refreshTokenExpirationDate;
    	}
        String tokenBuild = Jwts
                .builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
        
        tokens.put(tokenType, tokenBuild);
        
        return tokens;
    }
    
    /**
     * This method will check if token is valid.
     * @param token - {@linkplain java.util.String}
     * @param userDetails - {@linkplain UserDetails} object type.
     * @return - boolean type
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token); // or email. but with the context of this Jwtservice, we'll stick to username
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}