package Anel;

import java.io.Serializable;

public class Objeto implements Serializable {
  private static final long serialVersionUID = 2L;
  String tipo;
  int destino;
  int origem;
  String mensagem;

  public Objeto(String tipo, String mensagem, int destino, int origem) {
    this.tipo = tipo;
    this.destino = destino;
    this.mensagem = mensagem;
    this.origem = origem;
  }
}