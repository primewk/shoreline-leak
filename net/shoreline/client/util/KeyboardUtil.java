package net.shoreline.client.util;

import org.lwjgl.glfw.GLFW;

public class KeyboardUtil {
   public static String getKeyName(int keycode, int scancode) {
      String var10000;
      switch(keycode) {
      case 32:
         var10000 = "SPACE";
         break;
      case 257:
         var10000 = "ENTER";
         break;
      case 258:
         var10000 = "TAB";
         break;
      case 259:
         var10000 = "BACKSPACE";
         break;
      case 260:
         var10000 = "INSERT";
         break;
      case 261:
         var10000 = "DELETE";
         break;
      case 262:
         var10000 = "RIGHT";
         break;
      case 263:
         var10000 = "LEFT";
         break;
      case 264:
         var10000 = "DOWN";
         break;
      case 265:
         var10000 = "UP";
         break;
      case 266:
         var10000 = "PAGE_UP";
         break;
      case 267:
         var10000 = "PAGE_DOWN";
         break;
      case 268:
         var10000 = "HOME";
         break;
      case 269:
         var10000 = "END";
         break;
      case 280:
         var10000 = "CAPS_LOCK";
         break;
      case 340:
         var10000 = "LSHIFT";
         break;
      case 341:
         var10000 = "LCONTROL";
         break;
      case 342:
         var10000 = "LALT";
         break;
      case 344:
         var10000 = "RSHIFT";
         break;
      case 345:
         var10000 = "RCONTROL";
         break;
      case 346:
         var10000 = "RALT";
         break;
      case 1000:
         var10000 = "MOUSE0";
         break;
      case 1001:
         var10000 = "MOUSE1";
         break;
      case 1002:
         var10000 = "MOUSE2";
         break;
      case 1003:
         var10000 = "MOUSE3";
         break;
      case 1004:
         var10000 = "MOUSE4";
         break;
      case 1005:
         var10000 = "MOUSE5";
         break;
      case 1006:
         var10000 = "MOUSE6";
         break;
      case 1007:
         var10000 = "MOUSE7";
         break;
      default:
         var10000 = GLFW.glfwGetKeyName(keycode, scancode);
      }

      return var10000;
   }

   public static String getKeyName(int keycode) {
      return getKeyName(keycode, keycode < 1000 ? GLFW.glfwGetKeyScancode(keycode) : 0);
   }

   public static int getKeyCode(String key) {
      if (key.equalsIgnoreCase("NONE")) {
         return -1;
      } else {
         int i;
         for(i = 32; i < 97; ++i) {
            if (key.equalsIgnoreCase(getKeyName(i, GLFW.glfwGetKeyScancode(i)))) {
               return i;
            }
         }

         for(i = 256; i < 349; ++i) {
            if (key.equalsIgnoreCase(getKeyName(i, GLFW.glfwGetKeyScancode(i)))) {
               return i;
            }
         }

         for(i = 1000; i < 1010; ++i) {
            if (key.equalsIgnoreCase(getKeyName(i, GLFW.glfwGetKeyScancode(i)))) {
               return i;
            }
         }

         return -1;
      }
   }
}
