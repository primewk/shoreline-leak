package net.shoreline.client.impl.gui.account.list;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4280;
import net.shoreline.client.api.account.type.MinecraftAccount;
import net.shoreline.client.init.Managers;

public final class AccountListWidget extends class_4280<AccountEntry> {
   private String searchFilter;

   public AccountListWidget(class_310 minecraftClient, int i, int j, int k, int l) {
      super(minecraftClient, i, j, k, l);
   }

   public void populateEntries() {
      this.method_25339();
      List<MinecraftAccount> accounts = Managers.ACCOUNT.getAccounts();
      if (!accounts.isEmpty()) {
         Iterator var2 = accounts.iterator();

         while(var2.hasNext()) {
            MinecraftAccount account = (MinecraftAccount)var2.next();
            this.method_25321(new AccountEntry(account));
         }

         this.method_25313((AccountEntry)this.method_25326(0));
      }

   }

   protected void method_25311(class_332 context, int mouseX, int mouseY, float delta) {
      List<AccountEntry> entries = this.method_25396();
      if (this.searchFilter != null && !this.searchFilter.isEmpty()) {
         entries = entries.stream().filter((entryx) -> {
            return entryx.getAccount().username().toLowerCase().contains(this.searchFilter.toLowerCase());
         }).toList();
      }

      int x = this.method_25342();
      int width = this.method_25322();
      int height = this.field_22741 - 4;
      int size = entries.size();

      for(int i = 0; i < size; ++i) {
         int y = this.method_25337(i);
         int m = this.method_25319(i);
         if (m >= this.method_46427() && y <= this.method_55443()) {
            AccountEntry entry = (AccountEntry)entries.get(i);
            boolean isHovered = Objects.equals(this.method_37019(), entry);
            entry.method_49568(context, i, y, x, width, height, mouseX, mouseY, isHovered, delta);
            if (Objects.equals(this.method_25334(), entry)) {
               int color = this.method_25370() ? -1 : -8355712;
               this.method_44398(context, y, width, height, color, -16777216);
            }

            boolean selected = this.field_22740.method_1548() != null && this.field_22740.method_1548().method_1676().equalsIgnoreCase(entry.getAccount().username());
            entry.method_25343(context, i, y, x, width, height, mouseX, mouseY, selected, delta);
         }
      }

   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      this.method_25318(mouseX, mouseY, button);
      AccountEntry entry = (AccountEntry)this.method_25308(mouseX, mouseY);
      if (entry != null) {
         this.method_25313(entry);
      }

      return this.method_25334() != null ? ((AccountEntry)this.method_25334()).method_25402(mouseX, mouseY, button) : true;
   }

   public void setSearchFilter(String searchFilter) {
      this.searchFilter = searchFilter;
   }
}
