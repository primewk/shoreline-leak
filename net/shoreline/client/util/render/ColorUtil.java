package net.shoreline.client.util.render;

import java.awt.Color;

public class ColorUtil {
   public static Color hslToColor(float f, float f2, float f3, float f4) {
      if (!(f2 < 0.0F) && !(f2 > 100.0F)) {
         if (!(f3 < 0.0F) && !(f3 > 100.0F)) {
            if (!(f4 < 0.0F) && !(f4 > 1.0F)) {
               f %= 360.0F;
               float f5 = 0.0F;
               f5 = (double)f3 < 0.5D ? f3 * (1.0F + f2) : (f3 /= 100.0F) + (f2 /= 100.0F) - f2 * f3;
               f2 = 2.0F * f3 - f5;
               f3 = Math.max(0.0F, colorCalc(f2, f5, (f /= 360.0F) + 0.33333334F));
               float f6 = Math.max(0.0F, colorCalc(f2, f5, f));
               f2 = Math.max(0.0F, colorCalc(f2, f5, f - 0.33333334F));
               f3 = Math.min(f3, 1.0F);
               f6 = Math.min(f6, 1.0F);
               f2 = Math.min(f2, 1.0F);
               return new Color(f3, f6, f2, f4);
            } else {
               throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
            }
         } else {
            throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
         }
      } else {
         throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
      }
   }

   private static float colorCalc(float f, float f2, float f3) {
      if (f3 < 0.0F) {
         ++f3;
      }

      if (f3 > 1.0F) {
         --f3;
      }

      if (6.0F * f3 < 1.0F) {
         return f + (f2 - f) * 6.0F * f3;
      } else if (2.0F * f3 < 1.0F) {
         return f2;
      } else {
         return 3.0F * f3 < 2.0F ? f + (f2 - f) * 6.0F * (0.6666667F - f3) : f;
      }
   }
}
