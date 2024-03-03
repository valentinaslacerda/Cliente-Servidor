package Estrela;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Cliente {
  Socket socket;
  InetAddress inet;
  static int id;
  int porta;

  public Cliente(int id, int porta) {
    this.id = id;
    this.porta = porta;
    this.rodar();
  }

  private void rodar() {

    try {
      socket = new Socket("localhost", porta);
      inet = socket.getInetAddress();
      System.out.println("HostAddress = " + inet.getHostAddress());
      System.out.println("HostName = " + inet.getHostName());

      ImplCliente c = new ImplCliente(socket);
      Thread t = new Thread(c);
      t.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String args[]) {
    // new Cliente(1, 5000);
    // new Cliente(2, 5000);
    new Cliente(3, 5000);

  }
}
