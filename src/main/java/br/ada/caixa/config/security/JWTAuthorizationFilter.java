//package br.ada.caixa.config.security;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import java.util.Objects;
//
//public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
//
//    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain) throws IOException, ServletException {
//
//        String token = request.getHeader(SecurityConstants.HEADER_STRING);
//
//        if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken = this.getAuth(token);
//
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//        chain.doFilter(request, response);
//
//    }
//
//    private UsernamePasswordAuthenticationToken getAuth(String token) {
//
//        final String email = JwtUtils.getEmail(token);
//        final List<String> roles = JwtUtils.getRoles(token);
//
//        if (Objects.nonNull(email)) {
//            return new UsernamePasswordAuthenticationToken(
//                    email,
//                    null,
//                    List.of(new SimpleGrantedAuthority(roles.get(0))));
//        } else {
//            return null;
//        }
//
//    }
//}
