package tech.buildrun.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.buildrun.lambda.request.LoginRequest;
import tech.buildrun.lambda.response.LoginResponse;

public class Handler implements
        RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                                      Context context) {
        var logger = context.getLogger();

        logger.log("Request received - " + request.getBody());

        try {
            var loginRequest = objectMapper.readValue(request.getBody(), LoginRequest.class);

            boolean isAuthorized = false;

            if (loginRequest.username().equalsIgnoreCase("admin")
                    && loginRequest.password().equalsIgnoreCase("123")) {
                isAuthorized = true;
            }

            var loginResponse = new LoginResponse(isAuthorized);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(loginResponse))
                    .withIsBase64Encoded(false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
