package Estrela;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ImplCliente implements Runnable {
  private Socket cliente;
  private boolean conexao = true;
  private PrintStream saida;
  private BufferedReader entradaDoServidor;

  public ImplCliente(Socket c) {
    this.cliente = c;
  }

  public void run() {
    try {
      System.out.println("O cliente conectou ao servidor com id: " + Cliente.id);

      BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

      saida = new PrintStream(cliente.getOutputStream());

      entradaDoServidor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

      Thread receberMensagens = new Thread(new ReceberMensagens());
      receberMensagens.start();

      String mensagem;

      while (conexao) {

        System.out.println("Digite uma mensagem: ");
        mensagem = teclado.readLine();

        if (mensagem.equalsIgnoreCase("fim"))
          conexao = false;
        saida.println(mensagem + " origem: " + Cliente.id);

      }
      saida.close();
      teclado.close();
      entradaDoServidor.close();
      cliente.close();
      System.out.println("Cliente finaliza conexão.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class ReceberMensagens implements Runnable {

    @Override
    public void run() {
      try {
        String mensagemDoServidor;
        while ((mensagemDoServidor = entradaDoServidor.readLine()) != null) {
          System.out.println("Mensagem do servidor: " + mensagemDoServidor);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }
}