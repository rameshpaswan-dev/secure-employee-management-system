package in.ramesh.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.ramesh.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			String authHeader = request.getHeader("Authorization");

			// Check header validity
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = authHeader.substring(7);

			if (token.isBlank() || !jwtUtil.validateToken(token)) {
				filterChain.doFilter(request, response);
				return;
			}

			String username = jwtUtil.extractUsername(token);
			String role = jwtUtil.extractRole(token);

			//  Safety check
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				//  Ensure ROLE_ prefix
				if (role != null && !role.startsWith("ROLE_")) {
					role = "ROLE_" + role;
				}

				List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
						authorities);

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		} catch (Exception e) {
			//  Prevent breaking request
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}