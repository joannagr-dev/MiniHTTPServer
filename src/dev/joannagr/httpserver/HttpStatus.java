package dev.joannagr.httpserver;

/**
 * Contiene las líneas de estado utilizadas por el protocolo HTTP.
 *
 * @author Joanna García Ruiz
 */
public final class HttpStatus {

 /**
  * Evita la instanciación de la clase.
  */
 private HttpStatus() {
 }

 /**
  * Respuesta HTTP 200 (OK).
  */
 public static final String OK =
         "HTTP/1.1 200 OK";

 /**
  * Respuesta HTTP 404 (Not Found).
  */
 public static final String NOT_FOUND =
         "HTTP/1.1 404 Not Found";

 /**
  * Respuesta HTTP 400 (Bad Request).
  * Preparada para futuras ampliaciones.
  */
 public static final String BAD_REQUEST =
         "HTTP/1.1 400 Bad Request";

}