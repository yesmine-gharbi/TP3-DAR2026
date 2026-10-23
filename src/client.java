import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class client {

    public static void main(String[] args) {

        String adresseServeur = "127.0.0.1";
        if (args.length > 0) {
            adresseServeur = args[0];
        }

        try (Socket socket = new Socket()) {

            InetAddress adresse = InetAddress.getByName(adresseServeur);

            InetSocketAddress destination =new InetSocketAddress(adresse, 1234);

            socket.connect(destination);

            BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream())
            );

            String message = entree.readLine();

            System.out.println("Message du serveur : " + message);

        } catch (IOException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
