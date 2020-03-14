package exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class IncorrectArgumentsExceptionMapper implements ExceptionMapper<IncorrectArgumentsException> {

	@Override
	public Response toResponse(IncorrectArgumentsException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500);
		return Response.status(Status.NOT_FOUND)
				.type(MediaType.APPLICATION_XML)
				.entity(errorMessage)
				.build();

	}

}
