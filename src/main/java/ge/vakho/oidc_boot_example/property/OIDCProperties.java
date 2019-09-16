package ge.vakho.oidc_boot_example.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:oidc.properties")
@ConfigurationProperties("oidc")
public class OIDCProperties {

	private String issuerUrl;
	private String authorizationEndpointUri;
	private String tokenEndpointUri;
	private String userInfoUri;
	private String jwksUri;
	private String endSessionEndpoint;
	private String endSessionRedirectUri;
	private String clientId;
	private String clientSecret;
	private String redirectUri;

	public String getIssuerUrl() {
		return issuerUrl;
	}

	public void setIssuerUrl(String issuerUrl) {
		this.issuerUrl = issuerUrl;
	}

	public String getAuthorizationEndpointUri() {
		return authorizationEndpointUri;
	}

	public void setAuthorizationEndpointUri(String authorizationEndpointUri) {
		this.authorizationEndpointUri = authorizationEndpointUri;
	}

	public String getTokenEndpointUri() {
		return tokenEndpointUri;
	}

	public void setTokenEndpointUri(String tokenEndpointUri) {
		this.tokenEndpointUri = tokenEndpointUri;
	}

	public String getUserInfoUri() {
		return userInfoUri;
	}

	public void setUserInfoUri(String userInfoUri) {
		this.userInfoUri = userInfoUri;
	}

	public String getJwksUri() {
		return jwksUri;
	}

	public void setJwksUri(String jwksUri) {
		this.jwksUri = jwksUri;
	}

	public String getEndSessionEndpoint() {
		return endSessionEndpoint;
	}

	public void setEndSessionEndpoint(String endSessionEndpoint) {
		this.endSessionEndpoint = endSessionEndpoint;
	}

	public String getEndSessionRedirectUri() {
		return endSessionRedirectUri;
	}

	public void setEndSessionRedirectUri(String endSessionRedirectUri) {
		this.endSessionRedirectUri = endSessionRedirectUri;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

}
