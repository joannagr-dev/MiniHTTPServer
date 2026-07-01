package dev.joannagr.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Servidor HTTP multihilo que atiende peticiones GET a través del puerto 8066.
 * Cada cliente es gestionado mediante un hilo independiente.
 *
 * @author Joanna García Ruiz
 */
public class HttpServer {

  /**
   * Puerto de escucha del servidor.
   */
  private static final int PORT = 8066;

  /**
   * Punto de entrada de la aplicación.
   *
   * @param args argumentos de línea de comandos
   * @throws Exception si ocurre un error durante la ejecución
   */
  public static void main(String[] args) throws Exception {

    ServerSocket serverSocket = new ServerSocket(PORT);

    mostrarInformacionServidor();

    while (true) {

      Socket clientSocket = serverSocket.accept();

      System.out.println("[INFO] Cliente conectado: "
              + clientSocket.getInetAddress().getHostAddress());

      ClientHandler client = new ClientHandler(clientSocket);

      client.start();

    }

  }

  /**
   * Procesa una petición HTTP recibida desde un cliente.
   *
   * @param clientSocket socket del cliente
   * @throws IOException si ocurre un error de entrada/salida
   */
  public static void procesarPeticion(Socket clientSocket)
          throws IOException {

    BufferedReader reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));

    PrintWriter writer = new PrintWriter(
            clientSocket.getOutputStream(), true);

    String peticion = reader.readLine();

    if (peticion == null) {
      return;
    }

    String ruta = obtenerRuta(peticion);

    System.out.println("[PETICIÓN] GET " + ruta);

    String html = resolverPagina(ruta);

    String estado = obtenerEstadoHTTP(ruta);

    System.out.println("[RESPUESTA] " + estado);
    System.out.println("--------------------------------------------------");

    enviarRespuesta(writer, estado, html);

  }

  /**
   * Obtiene la ruta solicitada por el cliente.
   *
   * @param peticion petición HTTP recibida
   * @return ruta solicitada
   */
  private static String obtenerRuta(String peticion) {

    peticion = peticion.replace(" ", "");

    if (!peticion.startsWith("GET")) {
      return "/";
    }

    return peticion.substring(
            3,
            peticion.lastIndexOf("HTTP"));

  }

  /**
   * Resuelve la página HTML correspondiente a una ruta.
   *
   * @param ruta ruta solicitada
   * @return contenido HTML
   * @throws IOException si ocurre un error de lectura
   */
  private static String resolverPagina(String ruta)
          throws IOException {

    switch (ruta) {

      case "":
      case "/":
        return leerRecurso("index.html");

      case "/quijote":
        return leerRecurso("quijote.html");

      default:
        return leerRecurso("404.html");

    }

  }

  /**
   * Devuelve el código HTTP correspondiente a la ruta solicitada.
   *
   * @param ruta ruta solicitada
   * @return línea de estado HTTP
   */
  private static String obtenerEstadoHTTP(String ruta) {

    switch (ruta) {

      case "":
      case "/":
      case "/quijote":
        return HttpStatus.OK;

      default:
        return HttpStatus.NOT_FOUND;

    }

  }

  /**
   * Envía una respuesta HTTP al cliente.
   *
   * @param writer flujo de salida
   * @param status línea de estado HTTP
   * @param html contenido HTML
   */
  private static void enviarRespuesta(
          PrintWriter writer,
          String status,
          String html) {

    writer.println(status);
    writer.println("Content-Type: text/html; charset=UTF-8");
    writer.println("Date: " + obtenerFechaHTTP());
    writer.println("Content-Length: " + html.length());
    writer.println();
    writer.println(html);

  }

  /**
   * Lee un recurso HTML almacenado en la carpeta resources.
   *
   * @param nombreArchivo nombre del archivo HTML
   * @return contenido del archivo
   * @throws IOException si ocurre un error durante la lectura
   */
  private static String leerRecurso(String nombreArchivo)
          throws IOException {

    Path ruta = Paths.get("resources", nombreArchivo);

    return Files.readString(ruta);

  }

  /**
   * Devuelve la fecha actual con formato HTTP.
   *
   * @return fecha con formato HTTP.
   */
  private static String obtenerFechaHTTP() {

    SimpleDateFormat formato = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z",
            Locale.US);

    formato.setTimeZone(
            TimeZone.getTimeZone("GMT"));

    return formato.format(new Date());

  }

  /**
   * Muestra la información de inicio del servidor.
   */
  private static void mostrarInformacionServidor() {

    System.out.println();
    System.out.println("==================================================");
    System.out.println("                MiniHTTPServer");
    System.out.println("==================================================");
    System.out.println("Servidor iniciado correctamente");
    System.out.println("Escuchando en el puerto: " + PORT);
    System.out.println();
    System.out.println("Rutas disponibles:");
    System.out.println("  GET  http://localhost:" + PORT + "/");
    System.out.println("  GET  http://localhost:" + PORT + "/quijote");
    System.out.println();
    System.out.println("Ruta para probar el error 404:");
    System.out.println("  GET  http://localhost:" + PORT + "/cualquiercosa");
    System.out.println("==================================================");
    System.out.println();

  }

}