package br.com.bcfinances.infrastructure.event.listener;

import br.com.bcfinances.infrastructure.event.ResourceCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent>{

	@Override
	public void onApplicationEvent(ResourceCreatedEvent event) {
		HttpServletResponse response = event.getResponse();
		Long id = event.getId();
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(id).toUri();
        response.setHeader("Location", uri.toASCIIString());
	}

}
