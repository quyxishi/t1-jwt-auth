package com.raii.jwtauth.security;

import com.raii.jwtauth.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // public static final String HTTP_ONLY_COOKIE_NAME = "access_token";
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return PathPatternRequestMatcher.withDefaults()
                .matcher("/auth/*")
                .matches(request);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        /* via cookie */
        // final var token = Optional.ofNullable(request.getCookies())
        //        .flatMap(x -> Arrays.stream(x)
        //                .filter(y -> y.getName().equals(HTTP_ONLY_COOKIE_NAME))
        //                .findFirst()
        //                .map(Cookie::getValue)
        //        );

        /* via header */
        final var token = Optional.ofNullable(request.getHeader(AUTH_HEADER_NAME))
                .filter(x -> x.startsWith(AUTH_HEADER_PREFIX))
                .map(x -> x.substring(AUTH_HEADER_PREFIX.length()));

        if (token.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            final var claims = jwtService.validateToken(token.get());
            final var subject = claims.getSubject();

            if (subject != null) {
                final var userDetails = userService.userDetailsService().loadUserByUsername(subject);
                final var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                log.info("Issuing authentication for subject: {}", subject);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
