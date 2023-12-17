package net.javaguides.springboot.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.springboot.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;

@Component
public class JwtAthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader(AUTHORIZATION);
    final String userEmail;
    final String jwtToken;

    if (authHeader == null || authHeader.startsWith("Bearer")) {
        filterChain.doFilter(request, response);
        return;
    }

    jwtToken = authHeader.substring(7);
    userEmail = jwtUtils.extractUsername(jwtToken);
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        }
        filterChain.doFilter(request, response);
    }


    }
}
