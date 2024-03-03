package Anel;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Processo {
  public static void main(String[] args) {
    // new Processo(1, 5000, 5001);
    // new Processo(2, 5001, 5002);
    // new Processo(3, 5002, 5003);
    new Processo(4, 5003, 5000);
  }

  ServerSocket socketServidor;
  Socket cliente;
  Socket clienteSocket;
  static int id;
  int porta;
  int proxPorta;

  public Processo(int id, int porta, int proxPorta) {
    this.id = id;
    this.porta = porta;
    this.proxPorta = proxPorta;
    run();
  }

  private void run() {
    try {
      Log log = new Log();
      Thread dt = new Thread(log);

      dt.setDaemon(true);
      dt.start();
      socketServidor = new ServerSocket(porta);

      System.out.println("Servidor rodando na porta " +
          socketServidor.getLocalPort());
      System.out.println("HostAddress = " +

          InetAddress.getLocalHost().getHostAddress());

      System.out.println("HostName = " +

          InetAddress.getLocalHost().getHostName());
      System.out.println("Aguardando conexão do cliente...");

      System.out.println("Enter para conectar");
      Scanner scanner = new Scanner(System.in);
      scanner.nextLine();

      cliente = new Socket("localhost", proxPorta);

      ImplCliente c = new ImplCliente(cliente);
      Thread t0 = new Thread(c);
      t0.start();

      clienteSocket = socketServidor.accept();

      ImplServidor servidor = new ImplServidor(clienteSocket, c, log);
      Thread t1 = new Thread(servidor);

      t1.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
