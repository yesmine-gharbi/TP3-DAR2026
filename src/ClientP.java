import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientP implements Runnable {

    private Socket socket;
    private int numeroClient;

    public ClientP(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream sortie =
                    new ObjectOutputStream(socket.getOutputStream());
            sortie.flush();

            ObjectInputStream entree =
                    new ObjectInputStream(socket.getInputStream());

            while (true) {

                Operation op = (Operation) entree.readObject();

                double a = op.getOperande1();
                double b = op.getOperande2();
                char operateur = op.getOperateur();

                boolean calculReussi = true;

                try {
                    switch (operateur) {

                        case '+':
                            op.setResultat(a + b);
                            break;

                        case '-':
                            op.setResultat(a - b);
                            break;

                        case '*':
                            op.setResultat(a * b);
                            break;

                        case '/':
                            if (b == 0) {
                                throw new ArithmeticException(
                                        "Division par zero impossible"
                                );
                            }
                            op.setResultat(a / b);
                            break;

                        default:
                            throw new IllegalArgumentException(
                                    "Operateur invalide"
                            );
                    }

                } catch (ArithmeticException | IllegalArgumentException e) {
                    op.setErreur(e.getMessage());
                    calculReussi = false;
                }

                if (calculReussi) {
                    int total = Serveur3_2.incrementerCompteur();

                    System.out.println("Client n°" + numeroClient
                            + " : " + a + " " + operateur + " " + b
                            + " = " + op.getResultat());

                    System.out.println("Nombre total d'operations : "
                            + total);
                } else {
                    System.out.println("Erreur pour le client n°"
                            + numeroClient + " : " + op.getErreur());
                }

                sortie.writeObject(op);
                sortie.flush();
            }

        } catch (java.io.EOFException e) {
            System.out.println("Client n°" + numeroClient
                    + " deconnecte.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connexion du client n°" + numeroClient
                    + " terminee : " + e.getMessage());

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du socket.");
            }
        }
    }
}
