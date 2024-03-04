package Anel;

import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.Scanner;

public class ImplCliente implements Runnable {

  private Socket cliente;
  private boolean conexao = true;
  ObjectOutputStream saida;
  private int id;

  public ImplCliente(Socket cliente) {
    this.cliente = cliente;
    try {
      saida = new ObjectOutputStream(cliente.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    Objeto o = null;
    String mensagem;
    int id = Processo.id;
    String tipo;
    String msg;
    int destino;
    try {

      System.out.println("O cliente conectou ao servidor com ID " + Processo.id);

      Scanner teclado = new Scanner(System.in);

      while (conexao) {

        System.out.println("Digite uma mensagem: ");

        mensagem = teclado.nextLine();

        String[] partes = mensagem.split(" ");

        tipo = partes[0];
        msg = partes.length > 1 ? partes[1] : "";
        destino = partes.length > 3 ? Integer.parseInt(partes[3]) : 0;

        if (tipo.equalsIgnoreCase("fim")) {
          saida.writeObject(o);
          conexao = false;
        } else {
          o = new Objeto(tipo, msg, destino, id);

          saida.writeObject(o);
          saida.flush();
        }

      }
      saida.close();
      teclado.close();
      cliente.close();
      System.out.println("Cliente finaliza conexão.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
