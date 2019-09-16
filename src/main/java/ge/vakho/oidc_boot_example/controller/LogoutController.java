package ge.vakho.oidc_boot_example.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ge.vakho.oidc_boot_example.property.OIDCProperties;

@Controller
@RequestMapping("/logout")
public class LogoutController {

	@Autowired
	private OIDCProperties oidcProperties;

	@PostMapping
	public void doPost(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		if (!(authentication instanceof OIDCAuthenticationToken)) {
			throw new IllegalArgumentException(
					"Authentication object must be an instance of OIDCAuthenticationToken object!");
		}

		final OIDCAuthenticationToken oidcAuthentication = (OIDCAuthenticationToken) authentication;

		try {
			URIBuilder uriBuilder = new URIBuilder(oidcProperties.getEndSessionEndpoint());
			uriBuilder.addParameter("id_token_hint", oidcAuthentication.getIdToken().serialize());
			uriBuilder.addParameter("post_logout_redirect_uri", oidcProperties.getEndSessionRedirectUri());

			// Store state in session
			String state = UUID.randomUUID().toString();
			request.getSession().setAttribute("oidc_logout_state", state);
			uriBuilder.addParameter("state", state);
			response.sendRedirect(uriBuilder.build().toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
