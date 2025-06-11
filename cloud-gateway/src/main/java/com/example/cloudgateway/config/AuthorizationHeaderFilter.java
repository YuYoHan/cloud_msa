package com.example.cloudgateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private final Environment env;

    // Spring은 @Component로 등록된 클래스에서 생성자가 하나만 있으면,
    // @Autowired를 붙이지 않아도 자동으로 주입해줍니다.
    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);       // 부모 생성자 호출
        this.env = env;            // 의존성 주입 (생성자 주입)
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // ✅ public 경로는 인증 필터 우회
            if (path.startsWith("/user-service/api/v1/login") || path.startsWith("/user-service/api/v1/users")) {
                return chain.filter(exchange);
            }

            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String jwt = authorizationHeader.replace("Bearer","").trim();

            if(!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            // ✅ JWT 검증 및 사용자 정보 추출
            Claims claims;
            try {
                String secret = env.getProperty("token.secret");
                SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

                claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

            } catch (Exception e) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            String userId = claims.getSubject(); // 일반적으로 userId
            String email = claims.get("email", String.class);
            String role = claims.get("role", String.class);

            // ✅ 새로운 요청에 유저 정보 헤더 추가
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Email", email)
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String secret = env.getProperty("token.secret");
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String subject = null;

        try {
            subject = Jwts.parser()
                    .verifyWith(key)         // 서명 키 설정
                    .build()
                    .parseSignedClaims(jwt)  // 서명된 JWT 파싱
                    .getPayload()
                    .getSubject();           // 서브젝트 추출
        } catch (Exception e) {
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()) {
            returnValue = false;
        }


        return returnValue;
    }

    // Mono, Flux -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange,
                               String err,
                               HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }
}
