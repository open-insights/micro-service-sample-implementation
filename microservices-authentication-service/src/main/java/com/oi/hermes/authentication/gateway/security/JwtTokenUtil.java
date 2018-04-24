package com.oi.hermes.authentication.gateway.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {
	public static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "iat";
	private static final long serialVersionUID = -3301605591108950415L;
	private Clock clock = DefaultClock.INSTANCE;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public Claims getTokenClaims(String token) {
		return getAllClaimsFromToken(token);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		LOGGER.info("get claims");
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		LOGGER.info("checking isExpired token");
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(clock.now());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {

		return doGenerateToken(userDetails.getAuthorities(), userDetails.getUsername());
	}

	private String doGenerateToken(Collection<? extends GrantedAuthority> authorities, String subject) {
		LOGGER.info("creating new token");
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);
		HashMap<String, Object> claims = new HashMap<String, Object>();
		if (authorities != null) {
			for (GrantedAuthority grantedAuthority : authorities) {
				claims.put("roles/" + grantedAuthority.getAuthority(), grantedAuthority.getAuthority());
			}
		}
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		LOGGER.info("check if new refresh token");
		final Date created = getIssuedAtDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public String refreshToken(String token) {
		LOGGER.info("refresh the token");
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);
		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private Date calculateExpirationDate(Date createdDate) {
		LOGGER.info("calculating the token expiration time");
		return new Date(createdDate.getTime() + expiration * 1000);
	}
}
