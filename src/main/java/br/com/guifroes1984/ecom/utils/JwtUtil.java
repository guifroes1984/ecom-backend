package br.com.guifroes1984.ecom.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	public static final String SECRET = "S8YXDKNI2PE2V13RQHWZHXAYL0GWFGGDWNSMPGKDC2U8CBGFNAQKKLSEWUZZI7W08DB4VMQQLZNT96ZNYJA5J8CFA7YN1SEL5LO2WODEVSA5FJS1F3WG75PS28FY6CAMXT4YPIFTQTH6ATPHSMJYMHRDPCBS8JVIJXB18IIN85IZBXZX990GKQQ6EDZ6SFHRTAJNCHRH5PBH125MDTMZT5VUJ8SWRWIJYOUMCZ1Y4X6A4C6WIB0NA6A43D3KNC12";

	
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}


	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 30))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}


	private Key getSignKey() {
		byte[] keybytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keybytes);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}


	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}


	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
}
