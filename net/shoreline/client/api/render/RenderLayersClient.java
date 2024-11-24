package net.shoreline.client.api.render;

import com.google.common.collect.ImmutableMap;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4668;
import net.minecraft.class_918;
import net.minecraft.class_1921.class_4688;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4668.class_4672;
import net.minecraft.class_4668.class_4676;
import net.minecraft.class_4668.class_4683;
import net.shoreline.client.mixin.accessor.AccessorRenderPhase;
import net.shoreline.client.util.Globals;
import org.lwjgl.opengl.GL11;

public class RenderLayersClient implements Globals {
   public static final class_293 POSITION_COLOR_TEXTURE_OVERLAY;
   public static final class_1921 GLINT;
   public static final class_1921 ITEM_ENTITY_TRANSLUCENT_CULL;

   static {
      POSITION_COLOR_TEXTURE_OVERLAY = new class_293(ImmutableMap.builder().put("Position", class_290.field_1587).put("Color", class_290.field_1581).put("UV0", class_290.field_1591).put("Padding", class_290.field_1578).put("UV1", class_290.field_1583).put("UV2", class_290.field_20886).build());
      GLINT = class_1921.method_24048("glint", class_290.field_1585, class_5596.field_27382, 256, class_4688.method_23598().method_34578(class_4668.field_29422).method_34577(new class_4683(class_918.field_43087, true, false)).method_23616(class_4668.field_21350).method_23603(class_4668.field_21345).method_23604(new RenderLayersClient.DepthTest()).method_23615(class_4668.field_21368).method_23614(class_4668.field_21381).method_23617(false));
      ITEM_ENTITY_TRANSLUCENT_CULL = class_1921.method_24048("item_entity_translucent_cull", POSITION_COLOR_TEXTURE_OVERLAY, class_5596.field_27382, 1536, class_4688.method_23598().method_34578(class_4668.field_29405).method_34577(class_4668.field_21377).method_23608(new RenderLayersClient.Lightmap()).method_23610(class_4668.field_25643).method_23616(class_4668.field_21349).method_23617(true));
   }

   protected static class DepthTest extends class_4672 {
      public DepthTest() {
         super("depth_test", 519);
      }

      public void method_23516() {
         GL11.glEnable(2929);
         GL11.glDepthFunc(514);
      }

      public void method_23518() {
         GL11.glDisable(2929);
         GL11.glDepthFunc(515);
         GL11.glDepthFunc(519);
      }
   }

   protected static class Lightmap extends class_4676 {
      public Lightmap() {
         super(false);
         ((AccessorRenderPhase)this).hookSetBeginAction(() -> {
            Globals.mc.field_1773.method_22974().method_3316();
         });
         ((AccessorRenderPhase)this).hookSetEndAction(() -> {
            Globals.mc.field_1773.method_22974().method_3315();
         });
      }
   }
}
