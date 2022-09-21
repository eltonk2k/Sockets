import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Runnable {

	private static final String SERVER_ANDRESS = "127.0.0.1";
	private ClientSocket clientSocket;
	private Scanner scanner;

	public ChatClient() {
		scanner = new Scanner(System.in);
	}

	public void start() throws UnknownHostException, IOException {
		try {
			clientSocket = new ClientSocket(new Socket(SERVER_ANDRESS, ChatServer.PORT));
			System.out.println("Cliente conectado ao servidor em " + SERVER_ANDRESS + ":" + ChatServer.PORT);
			new Thread(this).start();
			messageLoop();
		} finally {
			clientSocket.close();
		}
	}

	@Override
	public void run() {
		String msg;
		while ((msg = clientSocket.getMessage()) != null) {
			System.out.printf("Mensagem recebida do Servidor: %s\n", msg);
		}
	}

	private void messageLoop() throws IOException {
		String msg;
		do {
			System.out.print("Digite uma mensagem ou 'sair' para finalizar: ");
			msg = scanner.nextLine();
			clientSocket.sendMsg(msg);

		} while (!msg.equalsIgnoreCase("sair"));
	}

	public static void main(String[] args) {
		try {
			ChatClient client = new ChatClient();
			client.start();
		} catch (IOException e) {
			System.out.println("Erro ao iniciar o cliente: " + e.getMessage());
		}
		System.out.println("Cliente finalizado!");

	}

}
