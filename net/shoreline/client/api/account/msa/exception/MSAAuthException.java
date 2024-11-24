package net.shoreline.client.api.account.msa.exception;

public final class MSAAuthException extends Exception {
   private final String message;

   public MSAAuthException(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }
}
