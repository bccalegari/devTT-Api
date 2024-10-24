package br.com.devtt.enterprise.application.exceptions;

import java.io.Serial;

public class InsufficientCredentialsException extends CoreException {
  @Serial
  private static final long serialVersionUID = 1233975465917948895L;

  public InsufficientCredentialsException() {
      super("Você não tem permissão para realizar essa ação.", 403);
  }
}