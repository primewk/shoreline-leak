package net.shoreline.client.impl.gui.account;

import net.minecraft.class_2561;
import net.minecraft.class_320;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_3675;
import net.minecraft.class_410;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.shoreline.client.impl.gui.account.list.AccountEntry;
import net.shoreline.client.impl.gui.account.list.AccountListWidget;
import net.shoreline.client.impl.manager.client.AccountManager;
import net.shoreline.client.init.Managers;

public final class AccountSelectorScreen extends class_437 {
   private final class_437 parent;
   private AccountListWidget accountListWidget;
   private class_342 searchWidget;

   public AccountSelectorScreen(class_437 parent) {
      super(class_2561.method_30163("Account Selector"));
      this.parent = parent;
   }

   protected void method_25426() {
      this.accountListWidget = new AccountListWidget(this.field_22787, this.field_22789, this.field_22790 - 64 - 32, 32, 25);
      this.method_37067();
      this.accountListWidget.method_55444(this.field_22789, this.field_22790 - 64 - 32, 0, 32);
      this.accountListWidget.populateEntries();
      this.accountListWidget.setSearchFilter((String)null);
      this.method_37063(this.searchWidget = new class_342(this.field_22787.field_1772, 135, 20, class_2561.method_30163("Search...")));
      this.searchWidget.method_48229(this.field_22789 / 2 - this.searchWidget.method_25368() / 2, 4);
      this.searchWidget.method_47404(class_2561.method_30163("Search..."));
      int buttonWidth = true;
      int buttonHeight = true;
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Add"), (action) -> {
         this.field_22787.method_1507(new AccountAddAccountScreen(this));
      }).method_46434(this.field_22789 / 2 + 2, this.accountListWidget.method_25364() + 40, 110, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Login"), (action) -> {
         AccountEntry entry = (AccountEntry)this.accountListWidget.method_25334();
         if (entry != null) {
            class_320 session = entry.getAccount().login();
            if (session != null) {
               Managers.ACCOUNT.setSession(session);
            }
         }

      }).method_46434(this.field_22789 / 2 - 110 - 2, this.accountListWidget.method_25364() + 40, 110, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Back"), (action) -> {
         this.field_22787.method_1507(this.parent);
      }).method_46434(this.field_22789 / 2 - 110 - 2, this.accountListWidget.method_25364() + 40 + 20 + 2, 110, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Delete"), (action) -> {
         AccountEntry entry = (AccountEntry)this.accountListWidget.method_25334();
         if (entry != null) {
            if (class_3675.method_15987(this.field_22787.method_22683().method_4490(), 340)) {
               Managers.ACCOUNT.unregister(entry.getAccount());
               this.field_22787.method_1507(this);
            } else {
               this.field_22787.method_1507(new class_410((value) -> {
                  if (value) {
                     Managers.ACCOUNT.unregister(entry.getAccount());
                  }

                  this.field_22787.method_1507(this);
               }, class_2561.method_30163("Delete account?"), class_2561.method_30163("Are you sure you would like to delete " + entry.getAccount().username() + "?"), class_2561.method_30163("Yes"), class_2561.method_30163("No")));
            }
         }
      }).method_46434(this.field_22789 / 2 + 2, this.accountListWidget.method_25364() + 40 + 20 + 2, 110, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163(Managers.ACCOUNT.isEncrypted() ? "Decrypt" : "Encrypt"), (action) -> {
         if (!Managers.ACCOUNT.isEncrypted()) {
            this.field_22787.method_1507(new AccountEncryptionScreen(this));
         }

      }).method_46434(this.field_22789 - 110 - 4, 6, 110, 20).method_46431());
   }

   public void method_49589() {
      if (this.accountListWidget != null) {
         this.accountListWidget.populateEntries();
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      this.accountListWidget.method_25394(context, mouseX, mouseY, delta);
      context.method_27535(this.field_22787.field_1772, class_2561.method_30163(this.getLoginInfo()), 2, 2, 11184810);
      if (this.searchWidget.method_25367()) {
         String content = this.searchWidget.method_1882();
         if (content == null || content.isEmpty()) {
            this.accountListWidget.setSearchFilter((String)null);
            return;
         }

         this.accountListWidget.setSearchFilter(content.replaceAll("\\s*", ""));
      }

   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      this.accountListWidget.method_25402(mouseX, mouseY, button);
      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      this.accountListWidget.method_25406(mouseX, mouseY, button);
      return super.method_25406(mouseX, mouseY, button);
   }

   private String getLoginInfo() {
      return AccountManager.MSA_AUTHENTICATOR.getLoginStage().isEmpty() ? "Logged in as " + this.field_22787.method_1548().method_1676() : AccountManager.MSA_AUTHENTICATOR.getLoginStage();
   }
}
