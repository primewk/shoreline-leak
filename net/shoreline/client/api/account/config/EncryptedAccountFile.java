package net.shoreline.client.api.account.config;

import java.nio.file.Path;

public final class EncryptedAccountFile extends AccountFile {
   private String encryptionKey;

   public EncryptedAccountFile(Path dir) {
      super(dir, "accounts_enc");
   }

   public void save() {
   }

   public void load() {
   }

   public void setEncryptionKey(String encryptionKey) {
      this.encryptionKey = encryptionKey;
   }
}
