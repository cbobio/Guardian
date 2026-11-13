
package br.com.cbobio.Guardian.web.interceptor;


public record ValidationError(
		String campo,
		String erro
) {
}
