package net.shoreline.client.impl.manager.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.macro.Macro;
import net.shoreline.client.impl.event.MouseClickEvent;
import net.shoreline.client.impl.event.keyboard.KeyboardInputEvent;
import net.shoreline.client.util.Globals;

public class MacroManager implements Globals {
   private final Set<Macro> macros = new HashSet();

   public MacroManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onKeyboardInput(KeyboardInputEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1755 == null) {
         if (!this.macros.isEmpty()) {
            Iterator var2 = this.macros.iterator();

            while(var2.hasNext()) {
               Macro macro = (Macro)var2.next();
               if (macro.getKeycode() < 1000 && event.getAction() == 1 && event.getKeycode() != -1 && event.getKeycode() == macro.getKeycode()) {
                  macro.runMacro();
               }
            }

         }
      }
   }

   @EventListener
   public void onMouseInput(MouseClickEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null && mc.field_1755 == null) {
         if (!this.macros.isEmpty()) {
            Iterator var2 = this.macros.iterator();

            while(var2.hasNext()) {
               Macro macro = (Macro)var2.next();
               if (macro.getKeycode() >= 1000 && event.getAction() == 1 && event.getButton() != -1 && event.getButton() + 1000 == macro.getKeycode()) {
                  macro.runMacro();
               }
            }

         }
      }
   }

   public void postInit() {
   }

   public void setMacro(Macro macro, int keycode) {
      Macro m1 = this.getMacro((m) -> {
         return m.getId().equals(macro.getId());
      });
      if (m1 != null) {
         m1.setKeycode(keycode);
      }

   }

   public void register(Macro... macros) {
      Macro[] var2 = macros;
      int var3 = macros.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Macro macro = var2[var4];
         this.register(macro);
      }

   }

   public void register(Macro macro) {
      this.macros.add(macro);
   }

   public Macro getMacro(Predicate<? super Macro> predicate) {
      return (Macro)this.macros.stream().filter(predicate).findFirst().orElse((Object)null);
   }

   public Collection<Macro> getMacros() {
      return this.macros;
   }
}
