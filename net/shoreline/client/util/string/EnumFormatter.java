package net.shoreline.client.util.string;

import net.minecraft.class_2350;
import net.minecraft.class_2350.class_2351;

public class EnumFormatter {
   public static String formatEnum(Enum<?> in) {
      String name = in.name();
      if (!name.contains("_")) {
         char firstChar = name.charAt(0);
         String suffixChars = name.split(String.valueOf(firstChar), 2)[1];
         String var10000 = String.valueOf(firstChar).toUpperCase();
         return var10000 + suffixChars.toLowerCase();
      } else {
         String[] names = name.split("_");
         StringBuilder nameToReturn = new StringBuilder();
         String[] var4 = names;
         int var5 = names.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String n = var4[var6];
            char firstChar = n.charAt(0);
            String suffixChars = n.split(String.valueOf(firstChar), 2)[1];
            nameToReturn.append(String.valueOf(firstChar).toUpperCase()).append(suffixChars.toLowerCase());
         }

         return nameToReturn.toString();
      }
   }

   public static String formatDirection(class_2350 direction) {
      String var10000;
      switch(direction) {
      case field_11036:
         var10000 = "Up";
         break;
      case field_11033:
         var10000 = "Down";
         break;
      case field_11043:
         var10000 = "North";
         break;
      case field_11035:
         var10000 = "South";
         break;
      case field_11034:
         var10000 = "East";
         break;
      case field_11039:
         var10000 = "West";
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }

   public static String formatAxis(class_2351 axis) {
      String var10000;
      switch(axis) {
      case field_11048:
         var10000 = "X";
         break;
      case field_11052:
         var10000 = "Y";
         break;
      case field_11051:
         var10000 = "Z";
         break;
      default:
         throw new IncompatibleClassChangeError();
      }

      return var10000;
   }
}
