package com.oi.hermes.subscription.management.security;

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



	public Boolean validateToken(String token) {
		LOGGER.info("validating  the token");
		/*
		 * HermesUser user = (HermesUser) userDetails; final String username =
		 * getUsernameFromToken(token); final Date created =
		 * getIssuedAtDateFromToken(token); final Date expiration =
		 * getExpirationDateFromToken(token); return
		 * (username.equals(user.getUsername()) && !isTokenExpired(token) &&
		 * !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
		 */
		return isTokenExpired(token);
	}

}
