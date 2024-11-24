package net.shoreline.client.impl.manager.client;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.class_320;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.account.config.AccountFile;
import net.shoreline.client.api.account.config.EncryptedAccountFile;
import net.shoreline.client.api.account.msa.MSAAuthenticator;
import net.shoreline.client.api.account.type.MinecraftAccount;
import net.shoreline.client.mixin.accessor.AccessorMinecraftClient;
import net.shoreline.client.util.Globals;

public final class AccountManager implements Globals {
   public static final MSAAuthenticator MSA_AUTHENTICATOR = new MSAAuthenticator();
   private final List<MinecraftAccount> accounts = new LinkedList();
   private AccountFile configFile;

   public void postInit() {
      Path runDir = Shoreline.CONFIG.getClientDirectory();
      if (runDir.resolve("accounts_enc.json").toFile().exists()) {
         System.out.println("Encrypted account file exists");
         this.configFile = new EncryptedAccountFile(runDir);
      } else {
         System.out.println("Normal account file");
         this.configFile = new AccountFile(runDir);
      }

      Shoreline.CONFIG.addFile(this.configFile);
   }

   public void register(MinecraftAccount account) {
      this.accounts.add(account);
   }

   public void unregister(MinecraftAccount account) {
      this.accounts.remove(account);
   }

   public void setSession(class_320 session) {
      ((AccessorMinecraftClient)mc).setSession(session);
      Shoreline.info("Set session to {} ({})", session.method_1676(), session.method_44717());
   }

   public List<MinecraftAccount> getAccounts() {
      return this.accounts;
   }

   public boolean isEncrypted() {
      return this.configFile instanceof EncryptedAccountFile;
   }
}
