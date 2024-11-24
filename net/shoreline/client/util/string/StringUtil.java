package net.shoreline.client.util.string;

public class StringUtil {
   public static String capitalize(String string) {
      if (string.length() != 0) {
         char var10000 = Character.toTitleCase(string.charAt(0));
         return var10000 + string.substring(1);
      } else {
         return "";
      }
   }
}
