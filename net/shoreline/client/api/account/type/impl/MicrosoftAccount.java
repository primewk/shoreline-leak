package net.shoreline.client.api.account.type.impl;

import com.google.gson.JsonObject;
import net.minecraft.class_320;
import net.shoreline.client.api.account.msa.exception.MSAAuthException;
import net.shoreline.client.api.account.type.MinecraftAccount;
import net.shoreline.client.impl.manager.client.AccountManager;

public final class MicrosoftAccount implements MinecraftAccount {
   private final String email;
   private final String password;
   private String accessToken;
   private String username;

   public MicrosoftAccount(String accessToken) {
      this((String)null, (String)null);
      if (accessToken != null && !accessToken.isEmpty()) {
         this.accessToken = accessToken;
      } else {
         throw new RuntimeException("Access token should not be null");
      }
   }

   public MicrosoftAccount(String email, String password) {
      this.email = email;
      this.password = password;
   }

   public class_320 login() {
      class_320 session = null;

      try {
         if (this.email != null && this.password != null) {
            try {
               session = AccountManager.MSA_AUTHENTICATOR.loginWithCredentials(this.email, this.password);
            } catch (MSAAuthException var3) {
               AccountManager.MSA_AUTHENTICATOR.setLoginStage(var3.getMessage());
               return null;
            }
         } else if (this.accessToken != null) {
            if (this.accessToken.startsWith("M.")) {
               this.accessToken = AccountManager.MSA_AUTHENTICATOR.getLoginToken(this.accessToken);
            }

            session = AccountManager.MSA_AUTHENTICATOR.loginWithToken(this.accessToken, true);
         }
      } catch (MSAAuthException var4) {
         var4.printStackTrace();
         AccountManager.MSA_AUTHENTICATOR.setLoginStage(var4.getMessage());
         return null;
      }

      if (session != null) {
         AccountManager.MSA_AUTHENTICATOR.setLoginStage("");
         this.username = session.method_1676();
         return session;
      } else {
         return null;
      }
   }

   public JsonObject toJSON() {
      JsonObject object = MinecraftAccount.super.toJSON();
      if (this.accessToken != null) {
         object.addProperty("token", this.accessToken);
      } else {
         if (this.email == null || this.password == null) {
            throw new RuntimeException("Email/Password & Access token is null for a MSA?");
         }

         object.addProperty("email", this.email);
         object.addProperty("password", this.password);
      }

      return object;
   }

   public String getEmail() {
      return this.email;
   }

   public String getPassword() {
      return this.password;
   }

   public String username() {
      return this.username != null ? this.username : this.email;
   }

   public String getUsernameOrNull() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getAccessToken() {
      return this.accessToken;
   }
}
