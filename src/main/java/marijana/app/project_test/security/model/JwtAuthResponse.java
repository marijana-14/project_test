package marijana.app.project_test.security.model;

public class JwtAuthResponse {

    private final String jwtToken;

    public JwtAuthResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}