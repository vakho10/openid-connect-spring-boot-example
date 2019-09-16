package ge.vakho.oidc_boot_example.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Logout handler for post-identity server's logout.
 */
public class OIDCLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		do {
			// Use wasn't even authenticated!
			if (authentication == null) {
				break;
			}

			HttpSession session = request.getSession(false);
			// No session exists!
			if (session == null) {
				break;
			}

			String state = (String) session.getAttribute("oidc_logout_state");
			// No state present in session!
			if (state == null) {
				break;
			}

			// Wrong state returned!
			if (!request.getParameter("state").equals(state)) {
				break;
			}
			
			// Logout...
			new SecurityContextLogoutHandler().logout(request, response, authentication);			
			
		} while (false);
	}
}
