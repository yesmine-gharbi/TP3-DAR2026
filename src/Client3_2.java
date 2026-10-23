
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client3_2 {

    public static void main(String[] args) {

        String adresseServeur = "127.0.0.1";
        int port = 1234;

        if (args.length > 0) {
            adresseServeur = args[0];
        }

        try (
                Socket socket = new Socket();
                Scanner clavier = new Scanner(System.in)
        ) {

            InetAddress adresse =
                    InetAddress.getByName(adresseServeur);

            InetSocketAddress destination =
                    new InetSocketAddress(adresse, port);

            socket.connect(destination);

            ObjectOutputStream sortie =
                    new ObjectOutputStream(socket.getOutputStream());
            sortie.flush();

            ObjectInputStream entree =
                    new ObjectInputStream(socket.getInputStream());

            System.out.println("Connecte au serveur calculatrice.");
            System.out.println("Tape q comme premier nombre pour quitter.");

            while (true) {

                System.out.print("Premier nombre : ");
                String texteA = clavier.nextLine();

                if (texteA.equalsIgnoreCase("q")) {
                    break;
                }

                System.out.print("Operateur (+, -, *, /) : ");
                String texteOperateur = clavier.nextLine();

                System.out.print("Deuxieme nombre : ");
                String texteB = clavier.nextLine();

                try {
                    double a = Double.parseDouble(texteA);
                    double b = Double.parseDouble(texteB);

                    if (texteOperateur.length() != 1) {
                        System.out.println("Operateur invalide.");
                        continue;
                    }

                    char operateur = texteOperateur.charAt(0);

                    Operation op = new Operation(a, operateur, b);

                    sortie.writeObject(op);
                    sortie.flush();

                    Operation reponse = (Operation) entree.readObject();

                    if (reponse.getErreur() != null) {
                        System.out.println("Erreur : "
                                + reponse.getErreur());
                    } else {
                        System.out.println("Resultat : "
                                + reponse.getResultat());
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Veuillez saisir des nombres valides.");

                }
            }

            System.out.println("Deconnexion du client.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
