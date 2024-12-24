
package com.iocl.Dispatch_Portal_Application.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.TimeUnit;
@Data
public class IncomingRequests {
    private static final String SECRET = "ioclJWTIncomings";
    private static final long EXPIRATION_TIME = 3600L; // 1 hour

    private String jwt;
    private String userName;

    public IncomingRequests(String jwt, String userName) {
        this.jwt = jwt;
        this.userName = userName;
    }

    public static String generateJwt(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(EXPIRATION_TIME));
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static boolean isJwtValid(String jwt) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public static String getUserIdFromJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

  
}

