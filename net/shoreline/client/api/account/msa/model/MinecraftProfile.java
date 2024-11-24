package net.shoreline.client.api.account.msa.model;

public record MinecraftProfile(String username, String id) {
   public MinecraftProfile(String username, String id) {
      this.username = username;
      this.id = id;
   }

   public String username() {
      return this.username;
   }

   public String id() {
      return this.id;
   }
}
