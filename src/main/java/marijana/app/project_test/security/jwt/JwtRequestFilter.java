package marijana.app.project_test.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {

        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Method called on every request
    // JWT is pulled and validated
    // if valid, configure spring security to set authentication
    // current user becomes authenticated after authentication is set in context
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String requestHeader = httpServletRequest.getHeader(JWT_TOKEN_HEADER);
        if (requestHeader != null && requestHeader.startsWith(JWT_TOKEN_PREFIX)) {
            String jwtToken = requestHeader.substring(7);
            if (jwtTokenUtil.validate(jwtToken) && jwtTokenUtil.findUserDetailsByUsernameFromToken(jwtToken) != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        jwtTokenUtil.getAuthentication(jwtToken);
                usernamePasswordAuthenticationToken.
                        setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}