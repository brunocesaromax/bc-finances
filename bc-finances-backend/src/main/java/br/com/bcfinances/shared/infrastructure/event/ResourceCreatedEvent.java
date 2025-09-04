package br.com.bcfinances.shared.infrastructure.event;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

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
