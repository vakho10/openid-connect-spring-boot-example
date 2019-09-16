package ge.vakho.oidc_boot_example.handler;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import ge.vakho.oidc_boot_example.property.OIDCProperties;

@Component
public class SessionEndLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private OIDCProperties oidcProperties;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (!(authentication instanceof OIDCAuthenticationToken)) {
			throw new IllegalArgumentException(
					"Authentication object must be an instance of OIDCAuthenticationToken object!");
		}

		final OIDCAuthenticationToken oidcAuthentication = (OIDCAuthenticationToken) authentication;

		try {
			URIBuilder uriBuilder = new URIBuilder(oidcProperties.getEndSessionEndpoint());

			uriBuilder.addParameter("id_token_hint", oidcAuthentication.getIdToken().serialize());
			uriBuilder.addParameter("post_logout_redirect_uri", oidcProperties.getEndSessionRedirectUri());
//			uriBuilder.addParameter("state", state);
			response.sendRedirect(uriBuilder.build().toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
}