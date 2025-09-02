package guzman.SalesDashboard.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // Permitir solicitudes OPTIONS sin filtro
        if ("OPTIONS".equals(method)) {
            return true;
        }
        
        // Rutas de autenticación sin filtro
        if (path.startsWith("/api/v1/auth/")) {
            return true;
        }
        
        // Rutas públicas GET sin filtro
        if ((path.startsWith("/api/v1/categories/") || path.startsWith("/api/v1/products/")) && 
            "GET".equals(method)) {
            return true;
        }
        
        // Todas las demás rutas (incluyendo POST, PUT, DELETE) pasan por el filtro JWT
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JWT Filter processing: " + request.getMethod() + " " + request.getRequestURI());
        
        final String token = getTokenFromRequest(request);
        
        if (token == null) {
            System.out.println("No JWT token found in request");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JWT token found: " + token.substring(0, Math.min(20, token.length())) + "...");

        try {
            final String userEmail = jwtService.getEmailFromToken(token);
            System.out.println("User email from token: " + userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set for user: " + userEmail + " with authorities: " + userDetails.getAuthorities());
                } else {
                    System.out.println("Token is not valid for user: " + userEmail);
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing JWT token: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
