package Anel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

public class ImplServidor implements Runnable {
  public Socket socketCliente;
  public ImplCliente proxCliente;
  private boolean conexao = true;
  ObjectInputStream entrada;
  ObjectOutputStream saida;
  private Log log;

  public ImplServidor(Socket socketCliente, ImplCliente c, Log log) {
    this.socketCliente = socketCliente;
    this.proxCliente = c;
    this.log = log;
    try {
      entrada = new ObjectInputStream(socketCliente.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {

    try {
      Objeto o;

      while ((o = (Objeto) entrada.readObject()) != null) {
        if (o.tipo.equals("fim")) {
          conexao = false;
        } else if (o.tipo.equals("broadcast:")) {
          encaminharBroadcast(o);
        } else {

          encaminharUnicast(o);

        }

      }
      // Finaliza scanner e socket
      if (conexao == false) {
        entrada.close();
        System.out.println("Fim do cliente " +

            socketCliente.getInetAddress().getHostAddress());

        socketCliente.close();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized void writeLog(String mensagem) {
    try {
      log.addMensagemToQueue(mensagem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void encaminharUnicast(Objeto mensagem) {

    if (mensagem.destino == Processo.id) {

      try {
        System.out.println("mensagem unicast de " + mensagem.origem + ": " + mensagem.mensagem);
        writeLog("Processo " + mensagem.destino + " recebeu mensagem de " + mensagem.origem + ". Mensagem: "
            + mensagem.mensagem);
      } catch (Exception e) {
        System.out.println("Erro ao enviar mensagem para o destino.");

      }
    } else {
      try {
        System.out.println("Enviando unicast de " + Processo.id + " para " + mensagem.destino);
        writeLog("Processo " + Processo.id + " enviando unicast de " + mensagem.origem + " para " + mensagem.destino);
        proxCliente.saida.writeObject(mensagem);
      } catch (Exception e) {
        System.out.println("Erro ao passar msg");
      }
    }

  }

  private void encaminharBroadcast(Objeto mensagem) {

    if (Processo.id != mensagem.origem) {
      try {
        System.out.println("mensagem  broadcast de " + mensagem.origem + ": " + mensagem.mensagem);
        writeLog("Processo " + Processo.id + " recebeu mensagem  broadcast de " + mensagem.origem + ": "
            + mensagem.mensagem);
        proxCliente.saida.writeObject(mensagem);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

}
