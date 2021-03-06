package ge.vakho.oidc_boot_example.config;

import org.mitre.oauth2.model.ClientDetailsEntity.AuthMethod;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.openid.connect.client.OIDCAuthenticationFilter;
import org.mitre.openid.connect.client.OIDCAuthenticationProvider;
import org.mitre.openid.connect.client.service.impl.PlainAuthRequestUrlBuilder;
import org.mitre.openid.connect.client.service.impl.StaticAuthRequestOptionsService;
import org.mitre.openid.connect.client.service.impl.StaticClientConfigurationService;
import org.mitre.openid.connect.client.service.impl.StaticServerConfigurationService;
import org.mitre.openid.connect.client.service.impl.StaticSingleIssuerService;
import org.mitre.openid.connect.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import ge.vakho.oidc_boot_example.handler.OIDCLogoutHandler;
import ge.vakho.oidc_boot_example.property.OIDCProperties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OIDCProperties oidcProperties;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http //
				.authorizeRequests() //
					.antMatchers("/", "/home") //
						.permitAll() //
					.anyRequest() //
						.authenticated() //
					.and()
				// This adds the authentication filter itself, see
				// the configureOIDCFilter method for more details
				.addFilterBefore(configureOIDCfilter(), AbstractPreAuthenticatedProcessingFilter.class)
				// This sets up the application to automatically request an OIDC login when
				// needed
				.exceptionHandling()
					.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/openid_connect_login")) //
					.and()
				// This sets up the logout system
				.logout() //
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) //
					.addLogoutHandler(new OIDCLogoutHandler())
					.logoutSuccessUrl("/")
					.permitAll();
	}

	/**
	 * Create and configure the MITREid Connect client filter
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public OIDCAuthenticationFilter configureOIDCfilter() throws Exception {

		final OIDCAuthenticationFilter filter = new OIDCAuthenticationFilter();

		// This sets the RP to talk only to one IdP, configured above
		StaticSingleIssuerService issuerService = new StaticSingleIssuerService();
		issuerService.setIssuer(oidcProperties.getIssuerUrl());
		filter.setIssuerService(issuerService);

		// This tells the RP to dynamically load the IdP's configuration over the web
		StaticServerConfigurationService serverService = new StaticServerConfigurationService();
		ServerConfiguration server = new ServerConfiguration();
		server.setIssuer(oidcProperties.getIssuerUrl());
		server.setAuthorizationEndpointUri(oidcProperties.getAuthorizationEndpointUri());
		server.setTokenEndpointUri(oidcProperties.getTokenEndpointUri());
		server.setUserInfoUri(oidcProperties.getUserInfoUri());
		server.setJwksUri(oidcProperties.getJwksUri());
		server.setEndSessionEndpoint(oidcProperties.getEndSessionEndpoint());
		serverService.setServers(ImmutableMap.of(oidcProperties.getIssuerUrl(), server));
		filter.setServerConfigurationService(serverService);

		// This tells the RP how to talk to the IdP
		StaticClientConfigurationService clientService = new StaticClientConfigurationService();
		RegisteredClient client = new RegisteredClient();
		client.setClientId(oidcProperties.getClientId());
		client.setClientSecret(oidcProperties.getClientSecret());
		client.setTokenEndpointAuthMethod(AuthMethod.SECRET_BASIC);
		client.setScope(ImmutableSet.of("openid", "profile"));
		client.setRedirectUris(ImmutableSet.of(oidcProperties.getRedirectUri()));
		clientService.setClients(ImmutableMap.of(oidcProperties.getIssuerUrl(), client));
		filter.setClientConfigurationService(clientService);

		// This tells the client to send no additional options
		filter.setAuthRequestOptionsService(new StaticAuthRequestOptionsService());

		// This tells the client to use a plain HTTP redirect to send its requests
		filter.setAuthRequestUrlBuilder(new PlainAuthRequestUrlBuilder());

		// This wires in the authentication manager to the filter
		filter.setAuthenticationManager(authenticationManager());

		return filter;

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				// This wires the OIDC authentication provider into the system
				.authenticationProvider(configureOIDCAuthenticationProvider());
	}

	/**
	 * This creates the authentication provider that handles the OIDC login process
	 * to create a spring Authentication object in the security context.
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationProvider configureOIDCAuthenticationProvider() {
		final OIDCAuthenticationProvider authenticationProvider = new OIDCAuthenticationProvider();
		// This default provider will set everyone to have the role "USER". To change
		// this
		// behavior, wire in a custom OIDCAuthoritiesMapper here
		//
		// authenticationProvider.setAuthoritiesMapper(OIDCAuthoritiesMapper);
		//
		return authenticationProvider;
	}

}
