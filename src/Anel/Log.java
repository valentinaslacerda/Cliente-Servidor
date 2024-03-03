package Anel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Log implements Runnable {
  private BufferedWriter logWriter;
  private BlockingQueue<String> mensagemQueue;
  private final String arquivo = "log_msg.txt";

  public Log() {
    try {

      FileWriter fw = new FileWriter(arquivo, true);
      logWriter = new BufferedWriter(fw);
      mensagemQueue = new LinkedBlockingQueue<>();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      while (true) {
        String mensagem = mensagemQueue.take();
        logWriter.write(mensagem);
        logWriter.newLine();
        logWriter.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        logWriter.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void close() {
    try {
      if (logWriter != null) {
        logWriter.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addMensagemToQueue(String mensagem) {
    try {
      mensagemQueue.put(mensagem);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public BufferedWriter getLogWriter() {
    return logWriter;
  }

}
