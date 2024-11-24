package net.shoreline.client.impl.manager.world.sound;

import net.minecraft.class_1111;
import net.minecraft.class_1113;
import net.minecraft.class_1144;
import net.minecraft.class_1146;
import net.minecraft.class_2960;
import net.minecraft.class_3419;
import net.minecraft.class_5862;
import net.minecraft.class_1111.class_1112;
import net.minecraft.class_1113.class_1114;
import net.shoreline.client.util.Globals;
import org.jetbrains.annotations.Nullable;

public class SoundManager implements Globals {
   public static final class_1146 EMPTY_SOUND_SET = new class_1146(new class_2960("minecraft", "intentionally_empty"), (String)null);

   public void playSound(SoundEvents sound) {
      mc.method_1483().method_4873(new class_1113() {
         public class_2960 method_4775() {
            return sound.getId();
         }

         @Nullable
         public class_1146 method_4783(class_1144 soundManager) {
            return SoundManager.EMPTY_SOUND_SET;
         }

         public class_1111 method_4776() {
            return new class_1111(sound.name(), class_5862.method_33908(1.0F), class_5862.method_33908(1.0F), 1, class_1112.field_5473, false, false, 16);
         }

         public class_3419 method_4774() {
            return class_3419.field_15248;
         }

         public boolean method_4786() {
            return false;
         }

         public boolean method_4787() {
            return false;
         }

         public int method_4780() {
            return 0;
         }

         public float method_4781() {
            return 1.0F;
         }

         public float method_4782() {
            return 1.0F;
         }

         public double method_4784() {
            return Globals.mc.field_1724 != null ? (double)((float)Globals.mc.field_1724.method_23317()) : 0.0D;
         }

         public double method_4779() {
            return Globals.mc.field_1724 != null ? (double)((float)Globals.mc.field_1724.method_23318()) : 0.0D;
         }

         public double method_4778() {
            return Globals.mc.field_1724 != null ? (double)((float)Globals.mc.field_1724.method_23321()) : 0.0D;
         }

         public class_1114 method_4777() {
            return class_1114.field_5476;
         }
      });
   }
}
