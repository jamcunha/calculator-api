package pt.jamcunha.rest.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestIdFilter implements Filter {

	private static final String REQUEST_ID = "X-Request-Id";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String requestId = UUID.randomUUID().toString();

		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader(REQUEST_ID, requestId);

		chain.doFilter(request, response);
	}

}