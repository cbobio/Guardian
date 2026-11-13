
package br.com.cbobio.Guardian.web.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultApiResponse<T> {
    public enum ResponseStatus {
        SUCCESS,
        ERROR
    }

    private ResponseStatus status;
    private ApiResponseData<T> data;
    private String message;

    public static <T> DefaultApiResponse<T> success(ApiResponseData<T> data) {
        return new DefaultApiResponse<>(ResponseStatus.SUCCESS, data, "Operation succeeded");
    }

    public static <T> DefaultApiResponse<T> error(String message) {
        return new DefaultApiResponse<>(ResponseStatus.ERROR, null, message);
    }

    public static <T> DefaultApiResponse<T> error(String message, ApiResponseData<T> data) {
        return new DefaultApiResponse<>(ResponseStatus.ERROR, data, message);
    }
}
