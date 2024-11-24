package net.shoreline.client.impl.gui.account;

import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_320;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.account.msa.exception.MSAAuthException;
import net.shoreline.client.api.account.type.MinecraftAccount;
import net.shoreline.client.api.account.type.impl.CrackedAccount;
import net.shoreline.client.api.account.type.impl.MicrosoftAccount;
import net.shoreline.client.impl.manager.client.AccountManager;
import net.shoreline.client.init.Managers;

public final class AccountAddAccountScreen extends class_437 {
   private final class_437 parent;
   private class_342 email;
   private class_342 password;

   public AccountAddAccountScreen(class_437 parent) {
      super(class_2561.method_30163("Add or Create an Alt Account"));
      this.parent = parent;
   }

   protected void method_25426() {
      this.method_37067();
      this.method_37063(this.email = new class_342(this.field_22787.field_1772, this.field_22789 / 2 - 75, this.field_22790 / 2 - 30, 150, 20, class_2561.method_30163("")));
      this.email.method_47404(class_2561.method_30163("Email or Username..."));
      this.method_37063(this.password = new class_342(this.field_22787.field_1772, this.field_22789 / 2 - 75, this.field_22790 / 2 - 5, 150, 20, class_2561.method_30163("")));
      this.password.method_47404(class_2561.method_30163("Password (Optional)"));
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Add"), (action) -> {
         String accountEmail = this.email.method_1882();
         if (accountEmail.length() >= 3) {
            String accountPassword = this.password.method_1882();
            Object account;
            if (!accountPassword.isEmpty()) {
               account = new MicrosoftAccount(accountEmail, accountPassword);
            } else {
               account = new CrackedAccount(accountEmail);
            }

            Managers.ACCOUNT.register((MinecraftAccount)account);
            this.field_22787.method_1507(this.parent);
         }

      }).method_46434(this.field_22789 / 2 - 72, this.field_22790 / 2 + 20, 145, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Browser..."), (action) -> {
         try {
            AccountManager.MSA_AUTHENTICATOR.loginWithBrowser((token) -> {
               Shoreline.EXECUTOR.execute(() -> {
                  MicrosoftAccount account = new MicrosoftAccount(token);
                  class_320 session = account.login();
                  if (session != null) {
                     Managers.ACCOUNT.setSession(session);
                     Managers.ACCOUNT.register(account);
                     this.field_22787.method_1507(this.parent);
                  } else {
                     AccountManager.MSA_AUTHENTICATOR.setLoginStage("Could not login to account");
                  }

               });
            });
         } catch (URISyntaxException | MSAAuthException | IOException var3) {
            var3.printStackTrace();
         }

      }).method_46434(this.field_22789 / 2 - 72, this.field_22790 / 2 + 20 + 22, 145, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Go Back"), (action) -> {
         this.field_22787.method_1507(this.parent);
      }).method_46434(this.field_22789 / 2 - 72, this.field_22790 / 2 + 20 + 44, 145, 20).method_46431());
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      class_327 var10001 = this.field_22787.field_1772;
      int var10003 = this.email.method_46426() - 10;
      int var10004 = this.email.method_46427() + this.email.method_25364() / 2;
      Objects.requireNonNull(this.field_22787.field_1772);
      context.method_25303(var10001, "*", var10003, var10004 - 9 / 2, (this.email.method_1882().length() >= 3 ? Color.green : Color.red).getRGB());
      context.method_25300(this.field_22787.field_1772, "Add an Account", this.field_22789 / 2, this.field_22790 / 2 - 120, -1);
   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.field_22787.method_1507(this.parent);
         return true;
      } else {
         return super.method_25404(keyCode, scanCode, modifiers);
      }
   }
}
