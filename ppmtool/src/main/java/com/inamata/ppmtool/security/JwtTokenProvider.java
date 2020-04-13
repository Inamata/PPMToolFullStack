package com.inamata.ppmtool.security;

import com.inamata.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.inamata.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.inamata.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    //Generate Token
    public String generateToken(Authentication authentication){
        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime()+ EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id",(Long.toString(user.getId())));
        claims.put("username",user.getUsername());
        claims.put("fullName",user.getFullname());
        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    //Validate Token
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);

            return true;
        }catch (SignatureException e) {
            System.out.println("Invalid Token Signature");
        }catch (MalformedJwtException e) {
            System.out.println("Invalid or Malformed Token");
        }catch (ExpiredJwtException e) {
            System.out.println("Expired Token");
        }catch (UnsupportedJwtException e) {
            System.out.println("Unsupported Token");
        }catch (IllegalArgumentException e){
            System.out.println("Claims is Empty");
        }catch (Exception e){
            System.out.println("Exception Occured:"+e);
        }
        return false;
    }
    //Get UserId from token
    public long getUserIdFromJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");
        return Long.parseLong(id);
    }
}
