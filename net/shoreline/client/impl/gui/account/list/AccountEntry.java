package net.shoreline.client.impl.gui.account.list;

import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_320;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4280.class_4281;
import net.shoreline.client.api.account.type.MinecraftAccount;
import net.shoreline.client.api.account.type.impl.CrackedAccount;
import net.shoreline.client.api.account.type.impl.MicrosoftAccount;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.network.TextureDownloader;

public class AccountEntry extends class_4281<AccountEntry> implements Globals {
   private static final TextureDownloader FACE_DOWNLOADER = new TextureDownloader();
   private final MinecraftAccount account;
   private long lastClickTime = -1L;

   public AccountEntry(MinecraftAccount account) {
      this.account = account;
   }

   public void method_25343(class_332 context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
      class_327 var10001 = mc.field_1772;
      class_2561 var10002 = class_2561.method_30163(this.account.username());
      int var10003 = x + 20;
      int var10004 = y + entryHeight / 2;
      Objects.requireNonNull(mc.field_1772);
      context.method_27535(var10001, var10002, var10003, var10004 - 9 / 2, hovered ? 5635925 : -1);
      if (!(this.account instanceof CrackedAccount)) {
         MinecraftAccount var12 = this.account;
         if (!(var12 instanceof MicrosoftAccount)) {
            return;
         }

         MicrosoftAccount msa = (MicrosoftAccount)var12;
         if (msa.getUsernameOrNull() == null) {
            return;
         }
      }

      String id = "face_" + this.account.username().toLowerCase();
      if (!FACE_DOWNLOADER.exists(id)) {
         if (!FACE_DOWNLOADER.isDownloading(id)) {
            FACE_DOWNLOADER.downloadTexture(id, "https://minotar.net/helm/" + this.account.username() + "/15", false);
         }

      } else {
         class_2960 texture = FACE_DOWNLOADER.get(id);
         if (texture != null) {
            context.method_25290(texture, x + 2, y + 2, 0.0F, 0.0F, 15, 15, 15, 15);
         }

      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (button == 0) {
         long time = System.currentTimeMillis() - this.lastClickTime;
         if (time > 0L && time < 500L) {
            class_320 session = this.account.login();
            if (session != null) {
               Managers.ACCOUNT.setSession(session);
            }
         }

         this.lastClickTime = System.currentTimeMillis();
         return false;
      } else {
         return super.method_25402(mouseX, mouseY, button);
      }
   }

   public class_2561 method_37006() {
      MinecraftAccount var2 = this.account;
      if (var2 instanceof MicrosoftAccount) {
         MicrosoftAccount msa = (MicrosoftAccount)var2;
         if (msa.username() == null) {
            return null;
         }
      }

      return class_2561.method_30163(this.account.username());
   }

   public MinecraftAccount getAccount() {
      return this.account;
   }
}
