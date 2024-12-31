
package com.iocl.Dispatch_Portal_Application.Security;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.iocl.Dispatch_Portal_Application.Entity.MstEmployee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtCookie)) {
                    String value = cookie.getValue();
                    if (value.contains(" ")) {
                        logger.error("Cookie value contains invalid characters (spaces): {}", value);
                        return null;
                    }
                    return value;
                }
            }
        }
        return null;
    }
    public ResponseCookie generateJwtCookie(MstEmployee employee) {
      //  String jwt = generateJwtToken(employee);
        String jwt = generateTokenFromUsername(String.valueOf(employee.getEmpCode()));

        return ResponseCookie.from("jwt", jwt)
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .httpOnly(true)
                .build();
    } 
    public ResponseCookie createJwtCookie(String jwt) {
        if (jwt == null || jwt.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT is null or empty");
        }

        jwt = jwt.trim(); // Ensure no extra spaces

        // Log the cleaned JWT
        logger.info("Cleaned JWT before creating cookie: '{}'", jwt);

        return ResponseCookie.from(jwtCookie, jwt)
                .httpOnly(true)
                .secure(true) // Set this to true in production for secure HTTPS cookies
                .maxAge(24 * 60 * 60) // 1 day expiry
                .path("/")
                .build();
    } 
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, "")
                .httpOnly(true)
                .secure(true) // Set this to true in production for secure HTTPS cookies
                .maxAge(0) // Expire the cookie immediately
                .path("/")
                .build();
    }



    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
    
    public String generateTokenFromUsernameAndClaims(String username, Map<String, Object> claims) {
        claims.put("locCode", claims.get("locCode")); // Use locCode for both senderLocCode and recipientLocCode
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
              //  .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    
    public Claims getAllClaimsFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public String getUserIdFromJwtToken(String token) {
        return (String) getAllClaimsFromJwtToken(token).get("userId");
    }

    
    public String getLocCodeFromJwtToken(String token) {
        Claims claims = getAllClaimsFromJwtToken(token);
        System.out.println("Claims: " + claims); // Debugging line to print all claims
        return (String) claims.get("locCode");
    }
    
    
    public String getNameFromJwtToken(String token) {
        Claims claims = getAllClaimsFromJwtToken(token);
        return claims.get("username", String.class);
    }
    
    public String getRoleFromJwtToken(String token) {
        Claims claims = getAllClaimsFromJwtToken(token);
        System.out.println("Extracted Claims: " + claims); // Debugging
        return claims.get("role", String.class);
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {	
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
