
package br.com.cbobio.Guardian.web.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
	    // Ignora endpoints do Actuator
	    String path = Optional.ofNullable(returnType.getContainingClass())
			    .map(Class::getPackageName)
			    .orElse("");
	    return !path.startsWith("org.springframework.boot.webmvc.actuate");
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // Se já for uma DefaultApiResponse, não faz nada
        if (body instanceof DefaultApiResponse) {
            return body;
        }

        // Obter a mensagem da anotação, se presente
        String customMessage = "Operation succeeded";
        CustomResponseMessage annotation = returnType.getMethodAnnotation(CustomResponseMessage.class);
        if (annotation != null) {
            customMessage = annotation.value();
        }

        ApiResponseData<Object> data = new ApiResponseData<>(body);

        // Caso contrário, encapsula em uma DefaultApiResponse com SUCCESS
        return new DefaultApiResponse<>(DefaultApiResponse.ResponseStatus.SUCCESS, data, customMessage);
    }
}