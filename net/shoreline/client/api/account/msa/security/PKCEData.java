package net.shoreline.client.api.account.msa.security;

public record PKCEData(String challenge, String verifier) {
   public PKCEData(String challenge, String verifier) {
      this.challenge = challenge;
      this.verifier = verifier;
   }

   public String challenge() {
      return this.challenge;
   }

   public String verifier() {
      return this.verifier;
   }
}
