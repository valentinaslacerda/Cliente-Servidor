package Estrela;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

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
          if (mensagemDoServidor.startsWith("unicast: ")) {

            String[] partes = mensagemDoServidor.split(" ");
            String msg = partes[1];
            int origem = Integer.parseInt(partes[5]);

            System.out.println("Mensagem unicast de " + origem + ": " + msg);
          } else {

            String[] partes = mensagemDoServidor.split(" ");
            String msg = partes[1];
            int origem = Integer.parseInt(partes[3]);

            System.out.println("Mensagem broadcast de " + origem + ": " + msg);
          }

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }
}