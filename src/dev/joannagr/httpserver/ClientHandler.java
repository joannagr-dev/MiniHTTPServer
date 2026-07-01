package dev.joannagr.httpserver;

import java.io.IOException;
import java.net.Socket;

/**
 * Hilo encargado de atender una petición HTTP de un cliente.
 *
 * Cada instancia procesa una única conexión y finaliza cuando la respuesta
 * ha sido enviada.
 *
 * @author Joanna García Ruiz
 */
public class ClientHandler extends Thread {

    /**
     * Socket asociado al cliente.
     */
    private final Socket clientSocket;

    /**
     * Crea un nuevo hilo para atender un cliente.
     *
     * @param clientSocket socket del cliente
     */
    public ClientHandler(Socket clientSocket) {

        this.clientSocket = clientSocket;

    }

    /**
     * Procesa la petición del cliente.
     */
    @Override
    public void run() {

        try (Socket socket = clientSocket) {

            HttpServer.procesarPeticion(socket);

        } catch (IOException e) {

            System.err.println(
                    "[ERROR] Error al procesar la petición del cliente: "
                            + e.getMessage());

        }

    }

}