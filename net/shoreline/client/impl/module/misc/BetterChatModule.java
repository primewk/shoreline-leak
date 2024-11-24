package net.shoreline.client.impl.module.misc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_124;
import net.minecraft.class_2583;
import net.minecraft.class_303;
import net.minecraft.class_5481;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.gui.hud.ChatTextEvent;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.render.animation.Easing;
import net.shoreline.client.util.render.animation.TimeAnimation;

public class BetterChatModule extends ToggleModule {
   Config<BetterChatModule.Timestamp> timestampConfig;
   Config<Boolean> animationConfig;
   Config<Integer> timeConfig;
   Config<Boolean> noSignatureConfig;
   public final Map<class_303, TimeAnimation> animationMap;

   public BetterChatModule() {
      super("BetterChat", "Modifications for the chat", ModuleCategory.MISCELLANEOUS);
      this.timestampConfig = new EnumConfig("Timestamp", "Shows chat timestamps", BetterChatModule.Timestamp.OFF, BetterChatModule.Timestamp.values());
      this.animationConfig = new BooleanConfig("Animation", "Animates the chat", false);
      this.timeConfig = new NumberConfig("Anim-Time", "Time for the animation", 0, 200, 1000, () -> {
         return false;
      });
      this.noSignatureConfig = new BooleanConfig("NoSignatureIndicator", "Removes the message signature indicator", false);
      this.animationMap = new HashMap();
   }

   @EventListener
   public void onChatText(ChatTextEvent event) {
      if (this.timestampConfig.getValue() != BetterChatModule.Timestamp.OFF) {
         String time = (new SimpleDateFormat("k:mm")).format(new Date());
         class_5481 var10000;
         switch((BetterChatModule.Timestamp)this.timestampConfig.getValue()) {
         case NORMAL:
            var10000 = class_5481.method_34909(new class_5481[]{this.fromString("<", class_2583.field_24360.method_10977(class_124.field_1063)), this.fromString(time, class_2583.field_24360.method_10977(class_124.field_1080)), this.fromString("> ", class_2583.field_24360.method_10977(class_124.field_1063))});
            break;
         case COLOR:
            var10000 = class_5481.method_34909(new class_5481[]{this.fromString("<" + time + "> ", class_2583.field_24360.method_36139(Modules.COLORS.getRGB()))});
            break;
         case OFF:
            var10000 = class_5481.field_26385;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         class_5481 text = var10000;
         event.cancel();
         event.setText(class_5481.method_30742(text, event.getText()));
      }

   }

   private class_5481 fromString(String string, class_2583 style) {
      return class_5481.method_30747(string, style);
   }

   public Config<Boolean> getAnimationConfig() {
      return this.animationConfig;
   }

   public Config<Integer> getTimeConfig() {
      return this.timeConfig;
   }

   public Easing getEasingConfig() {
      return Easing.LINEAR;
   }

   public Config<Boolean> getNoSignatureConfig() {
      return this.noSignatureConfig;
   }

   public static enum Timestamp {
      NORMAL,
      COLOR,
      OFF;

      // $FF: synthetic method
      private static BetterChatModule.Timestamp[] $values() {
         return new BetterChatModule.Timestamp[]{NORMAL, COLOR, OFF};
      }
   }
}
