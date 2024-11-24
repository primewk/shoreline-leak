package net.shoreline.client.impl.gui.account;

import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_7919;
import net.shoreline.client.init.Managers;

public final class AccountEncryptionScreen extends class_437 {
   private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-={}[]\\|'\";:/?.>,<`";
   private static final String[] REQUIREMENTS = new String[]{"8+ Characters", "A Special Character", "A Number", "An Uppercase Letter"};
   private final class_437 parent;
   private class_342 passwordTextField;

   public AccountEncryptionScreen(class_437 parent) {
      super(class_2561.method_30163("Account Encryption"));
      this.parent = parent;
   }

   protected void method_25426() {
      this.method_37067();
      this.passwordTextField = new class_342(this.field_22787.field_1772, 145, 20, class_2561.method_43473());
      this.passwordTextField.method_47404(class_2561.method_30163("Enter Password..."));
      this.passwordTextField.method_48229(this.field_22789 / 2 - this.passwordTextField.method_25368() / 2, this.field_22790 / 2 - 60);
      this.method_37063(this.passwordTextField);
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Encrypt"), (action) -> {
      }).method_46434(this.field_22789 / 2 - 72, this.passwordTextField.method_46427() + 90, 145, 20).method_46436(class_7919.method_47407(class_2561.method_30163("This will require you to enter a password every time you enter the account manager the first time!"))).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_30163("Go Back"), (action) -> {
         this.field_22787.method_1507(this.parent);
      }).method_46434(this.field_22789 / 2 - 72, this.passwordTextField.method_46427() + 112, 145, 20).method_46431());
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      class_327 var10001 = this.field_22787.field_1772;
      int var10002 = Managers.ACCOUNT.getAccounts().size();
      context.method_25300(var10001, "Encrypt Accounts (" + var10002 + ")", this.field_22789 / 2, this.field_22790 / 2 - 125, -1);
      var10001 = this.field_22787.field_1772;
      String var7 = (this.isPasswordSecure(this.passwordTextField.method_1882()) ? class_124.field_1060 : class_124.field_1061) + "*";
      int var10003 = this.field_22789 / 2 - this.passwordTextField.method_25368() / 2 - 6;
      int var10004 = this.passwordTextField.method_46427() + 10;
      Objects.requireNonNull(this.field_22787.field_1772);
      context.method_25303(var10001, var7, var10003, var10004 - 9 / 2, -1);
      context.method_25303(this.field_22787.field_1772, "Minimum Requirements:", this.passwordTextField.method_46426() + 1, this.passwordTextField.method_46427() + this.passwordTextField.method_25364() + 10, -1);

      for(int i = 0; i < REQUIREMENTS.length; ++i) {
         String requirement = REQUIREMENTS[i];
         var10001 = this.field_22787.field_1772;
         var7 = "- " + requirement;
         var10003 = this.passwordTextField.method_46426() + 6;
         var10004 = this.passwordTextField.method_46427() + this.passwordTextField.method_25364() + 10;
         Objects.requireNonNull(this.field_22793);
         context.method_25303(var10001, var7, var10003, var10004 + (9 + 2) * (1 + i), -1);
      }

   }

   private boolean isPasswordSecure(String password) {
      if (password.length() < 8) {
         return false;
      } else {
         boolean hasUppercase = false;
         boolean hasNumber = false;
         boolean hasSpecial = false;
         char[] characters = password.toCharArray();
         char[] var6 = characters;
         int var7 = characters.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            char c = var6[var8];
            if (Character.isUpperCase(c)) {
               hasUppercase = true;
            }

            if ("!@#$%^&*()_+-={}[]\\|'\";:/?.>,<`".indexOf(c) != -1) {
               hasSpecial = true;
            }

            if (c >= '0' && c <= '9') {
               hasNumber = true;
            }
         }

         return hasUppercase && hasNumber && hasSpecial;
      }
   }
}
