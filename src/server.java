import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

    public static void main(String[] args) {

        int numeroClient = 0;

        try (ServerSocket serveur = new ServerSocket(1234)) {

            System.out.println("Serveur demarre sur le port " + 1234);
            System.out.println("En attente des clients...");

            while (true) {

                Socket socketClient = serveur.accept();

                numeroClient++;

                System.out.println("Client n°" + numeroClient
                        + " connecte depuis "
                        + socketClient.getRemoteSocketAddress());

                Thread thread = new Thread(
                        new GestionClient(socketClient, numeroClient)
                );

                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}

class GestionClient implements Runnable {

    private Socket socket;
    private int numero;

    public GestionClient(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {

        try {
            java.io.PrintWriter sortie = new java.io.PrintWriter(
                    socket.getOutputStream(), true
            );

            sortie.println("Vous etes le client n°" + numero);

            sortie.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Erreur avec le client n°"
                    + numero + " : " + e.getMessage());
        }
    }
}
