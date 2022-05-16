package com.example.diaryapp2.security.jwt;

import com.example.diaryapp2.services.UserService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.diaryapp2.security.jwt.SecurityConstants.AUTHORITIES_KEY;
import static com.example.diaryapp2.security.jwt.SecurityConstants.SIGNING_KEY;

@Slf4j
@Component
public class TokenProviderImpl implements TokenProvider{

//    @Value("${jwt.token.validity}")
//    public long TOKEN_VALIDITY;


//    private final int ACCESS_TOKEN_VALIDITY = 9 * 3_600_000;//9hrs
//
//    @Value("${jwt.signing.key}")
//    private String SIGNING_KEY;
//
//    @Value("${jwt.authorities.key}")
//    private String AUTHORITIES_KEY;

    private final static Long TOKEN_VALIDITY_PERIOD = (long) (24 * 10 * 3600);


//    @Autowired
//    private TokenRepository tokenRepository;
//
//    @Autowired
//    private UserService userService;

    @Override
    public String getUsernameFromJWTToken(String token) {
        return getClaimFromJWTToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromJWTToken(String token) {
        return getClaimFromJWTToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromJWTToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJWTToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Header<?> getHeaderFromJWTToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getHeader();
    }

    @Override
    public Claims getAllClaimsFromJWTToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Boolean isJWTTokenExpired(String token) {
         Date expiration = getExpirationDateFromJWTToken(token);
        return expiration.before(new Date());
    }

    @Override
    public String generateJWTToken(Authentication authentication) {
        log.info("Signing key -> {}", SIGNING_KEY);
        log.info("Authorities key -> {}", AUTHORITIES_KEY);
        log.info("Authentication name --> {}",authentication.getName());
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

       String jwt= Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD ))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
        log.info("Authentication name --> {}",authentication.getName());
        log.info("authorities-->{}", AUTHORITIES_KEY);
        log.info("Jwt -->{}", jwt);
        return jwt;
    }

    @Override
    public Boolean validateJWTToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWTToken(token);
        return (username.equals(userDetails.getUsername()) && !isJWTTokenExpired(token));
    }

    @Override
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, Authentication existingAuth, UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
