package br.com.bcfinances.infrastructure.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import jakarta.servlet.http.HttpServletResponse;

import java.io.Serial;

public class ResourceCreatedEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 6854441885344283277L;

	@Getter
	private HttpServletResponse response;

	@Getter
	private Long id;
	
	public ResourceCreatedEvent(Object source, HttpServletResponse response, Long id) {
		super(source);
		this.response = response;
		this.id = id;
	}

}
