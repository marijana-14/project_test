package marijana.app.project_test.security.controller;

import marijana.app.project_test.security.jwt.JwtTokenUtil;
import marijana.app.project_test.security.model.JwtAuthRequest;
import marijana.app.project_test.security.model.JwtAuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Method called for authentication with existing credentials
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody JwtAuthRequest authenticationRequest)
    {
        final String username = authenticationRequest.getUsername();
        final String password = authenticationRequest.getPassword();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final String token = jwtTokenUtil.createToken(authentication);
        return new ResponseEntity(new JwtAuthResponse(token), HttpStatus.OK);


    }
}
