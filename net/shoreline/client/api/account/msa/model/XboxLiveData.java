package net.shoreline.client.api.account.msa.model;

public final class XboxLiveData {
   private String token;
   private String userHash;

   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public String getUserHash() {
      return this.userHash;
   }

   public void setUserHash(String userHash) {
      this.userHash = userHash;
   }
}
