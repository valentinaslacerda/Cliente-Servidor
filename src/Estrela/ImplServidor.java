package Estrela;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ImplServidor implements Runnable {
  public Socket socketCliente;
  public static int cont = 0;
  private boolean conexao = true;
  private Scanner s = null;
  private static PrintStream[] saidas = new PrintStream[4];
  public static int id = 0;

  public ImplServidor(Socket cliente) {
    socketCliente = cliente;

    try {
      ImplServidor.saidas[cont] = new PrintStream(cliente.getOutputStream());
    } catch (IOException e) {

      e.printStackTrace();
    }

  }

  public void run() {

    String mensagemRecebida;
    System.out.println("Conexão " +
        ImplServidor.cont +
        " com o cliente " +
        socketCliente.getInetAddress().getHostAddress() +
        "/" +
        socketCliente.getInetAddress().getHostName());

    try {
      s = new Scanner(socketCliente.getInputStream());
      // Exibe mensagem no console
      while (conexao) {
        mensagemRecebida = s.nextLine();
        if (mensagemRecebida.equalsIgnoreCase("fim"))
          conexao = false;
        else if (mensagemRecebida.startsWith("broadcast: ")) {
          encaminharBroadcast(mensagemRecebida);
        } else {
          encaminharUnicast(mensagemRecebida + " origem: " + cont);
        }

      }
      // Finaliza scanner e socket
      s.close();
      System.out.println("Fim do cliente " +

          socketCliente.getInetAddress().getHostAddress());

      socketCliente.close();
    } catch (IOException e) {
      e.getMessage();
    }
  }

  private void encaminharUnicast(String mensagem) {
    String[] partes = mensagem.split(" ");
    String msg = partes[1];
    int destino = Integer.parseInt(partes[3]);
    int origem = Integer.parseInt(partes[5]);

    if (destino == origem) {
      System.out.println("você não pode enviar uma mensagem para si mesmo");
    } else {
      try {
        saidas[destino - 1].println(mensagem);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

  }

  private void encaminharBroadcast(String mensagem) {
    String[] partes = mensagem.split(" ");
    String msg = partes[1];
    int origem = Integer.parseInt(partes[3]);
    for (int i = 0; i < 3; i++) {
      if (origem - 1 != i) {
        saidas[i].println(mensagem);
      }

    }
  }
}
