 package com.backend.task.backend_task_app.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class JwtAuthHelper {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest request;

    private String secret = "thisisacodingninjasdemonstrationforsecretkeyinspringsecurityjsonwebtokenauthentication";
    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000; // 1 hour validity

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName())).build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract username from token
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    // Extract roles from token
    public List<String> getRolesFromToken(String token) {
        // Parse the JWT token and retrieve claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName())).build()
                .parseClaimsJws(token).getBody(); // Get the body of claims

        // Retrieve roles as List<String>
        return claims.get("roles", List.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Extract claims from token
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName())).build()
                    .parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            System.out.println("Invalid token format: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("JWT Parsing Exception: " + e.getMessage());
        }
        return null;
    }

    // Check if token is expired
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            Date expDate = claims.getExpiration();
            return expDate.before(new Date());
        }
        return true;
    }

    // Generate token with username and roles
    public String generateToken(UserDetails userDetails) {
    	 Map<String, Object> claims = new HashMap<>();
	        claims.put("roles", userDetails.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toList()));
    	
	   	claims.put("isAdmin", (Boolean)session.getAttribute("isAdmin"));
        System.out.println("=================================> "+claims);
        return Jwts.builder().setClaims(claims).
        		setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName()),
                        SignatureAlgorithm.HS512)
                .compact();
    }
    
}