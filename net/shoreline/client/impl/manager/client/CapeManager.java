package net.shoreline.client.impl.manager.client;

import com.mojang.authlib.GameProfile;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.shoreline.client.util.Globals;

public class CapeManager implements Globals {
   private static final Map<UUID, class_2960> CAPE_TEXTURE_CACHE = new HashMap();

   public void loadPlayerCape(GameProfile profile, CapeManager.CapeTexture texture) {
      if (CAPE_TEXTURE_CACHE.containsKey(profile.getId())) {
         texture.callback((class_2960)CAPE_TEXTURE_CACHE.get(profile.getId()));
      } else {
         class_156.method_18349().execute(() -> {
            String uuid = profile.getId().toString();
            String url = String.format("http://s.optifine.net/capes/%s.png", profile.getName());

            try {
               URL optifineUrl = new URL(url);
               InputStream stream = optifineUrl.openStream();
               class_1011 cape = class_1011.method_4309(stream);
               class_1011 nativeImage = this.imageFromStream(cape);
               class_1043 t = new class_1043(nativeImage);
               class_2960 identifier = mc.method_1531().method_4617("of-capes-" + uuid, t);
               texture.callback(identifier);
               stream.close();
               CAPE_TEXTURE_CACHE.put(profile.getId(), identifier);
            } catch (Exception var11) {
            }

         });
      }
   }

   private class_1011 imageFromStream(class_1011 image) {
      int imageWidth = 64;
      int imageHeight = 32;
      int imageSrcWidth = image.method_4307();
      int srcHeight = image.method_4323();

      for(int imageSrcHeight = image.method_4323(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
         imageWidth *= 2;
      }

      class_1011 img = new class_1011(imageWidth, imageHeight, true);

      for(int x = 0; x < imageSrcWidth; ++x) {
         for(int y = 0; y < srcHeight; ++y) {
            img.method_4305(x, y, image.method_4315(x, y));
         }
      }

      image.close();
      return img;
   }

   public interface CapeTexture {
      void callback(class_2960 var1);
   }
}
