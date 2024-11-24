package net.shoreline.client.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2350;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3532;
import net.minecraft.class_3887;
import net.minecraft.class_4050;
import net.minecraft.class_4587;
import net.minecraft.class_4608;
import net.minecraft.class_591;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_892;
import net.minecraft.class_922;
import net.minecraft.class_293.class_5596;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.render.entity.RenderCrystalEvent;
import net.shoreline.client.impl.event.render.entity.RenderEntityEvent;
import net.shoreline.client.impl.event.render.item.RenderArmEvent;
import net.shoreline.client.util.world.EntityUtil;
import org.joml.Quaternionf;

public class ChamsModule extends ToggleModule {
   Config<ChamsModule.ChamsMode> modeConfig;
   Config<Boolean> handsConfig;
   Config<Boolean> selfConfig;
   Config<Boolean> playersConfig;
   Config<Boolean> monstersConfig;
   Config<Boolean> animalsConfig;
   Config<Boolean> otherConfig;
   Config<Boolean> invisiblesConfig;
   Config<Color> colorConfig;
   private static final float SINE_45_DEGREES = (float)Math.sin(0.7853981633974483D);

   public ChamsModule() {
      super("Chams", "Renders entity models through walls", ModuleCategory.RENDER);
      this.modeConfig = new EnumConfig("Mode", "The rendering mode for the chams", ChamsModule.ChamsMode.NORMAL, ChamsModule.ChamsMode.values());
      this.handsConfig = new BooleanConfig("Hands", "Render chams on first-person hands", true);
      this.selfConfig = new BooleanConfig("Self", "Render chams on the player", true);
      this.playersConfig = new BooleanConfig("Players", "Render chams on other players", true);
      this.monstersConfig = new BooleanConfig("Monsters", "Render chams on monsters", true);
      this.animalsConfig = new BooleanConfig("Animals", "Render chams on animals", true);
      this.otherConfig = new BooleanConfig("Others", "Render chams on crystals", true);
      this.invisiblesConfig = new BooleanConfig("Invisibles", "Render chams on invisible entities", true);
      this.colorConfig = new ColorConfig("Color", "The color of the chams", new Color(255, 0, 0, 60));
   }

   private static float getYaw(class_2350 direction) {
      switch(direction) {
      case field_11035:
         return 90.0F;
      case field_11039:
         return 0.0F;
      case field_11043:
         return 270.0F;
      case field_11034:
         return 180.0F;
      default:
         return 0.0F;
      }
   }

   @EventListener
   public void onRenderEntity(RenderEntityEvent event) {
      if (this.checkChams(event.entity)) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         class_289 tessellator = class_289.method_1348();
         class_287 vertexConsumer = tessellator.method_1349();
         RenderSystem.setShader(class_757::method_34539);
         RenderSystem.lineWidth(2.0F);
         vertexConsumer.method_1328(class_5596.field_27382, class_290.field_1592);
         Color color = (Color)this.colorConfig.getValue();
         event.matrixStack.method_22903();
         RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
         event.model.field_3447 = event.entity.method_6055(event.g);
         event.model.field_3449 = event.entity.method_5765();
         event.model.field_3448 = event.entity.method_6109();
         float h = class_3532.method_17821(event.g, event.entity.field_6220, event.entity.field_6283);
         float j = class_3532.method_17821(event.g, event.entity.field_6259, event.entity.field_6241);
         float k = j - h;
         float l;
         if (event.entity.method_5765() && event.entity.method_5854() instanceof class_1309) {
            class_1309 livingEntity2 = (class_1309)event.entity.method_5854();
            h = class_3532.method_17821(event.g, livingEntity2.field_6220, livingEntity2.field_6283);
            k = j - h;
            l = class_3532.method_15393(k);
            if (l < -85.0F) {
               l = -85.0F;
            }

            if (l >= 85.0F) {
               l = 85.0F;
            }

            h = j - l;
            if (l * l > 2500.0F) {
               h += l * 0.2F;
            }

            k = j - h;
         }

         float m = class_3532.method_16439(event.g, event.entity.field_6004, event.entity.method_36455());
         if (class_922.method_38563(event.entity)) {
            m *= -1.0F;
            k *= -1.0F;
         }

         float n;
         class_2350 direction;
         if (event.entity.method_41328(class_4050.field_18078) && (direction = event.entity.method_18401()) != null) {
            n = event.entity.method_18381(class_4050.field_18076) - 0.1F;
            event.matrixStack.method_46416((float)(-direction.method_10148()) * n, 0.0F, (float)(-direction.method_10165()) * n);
         }

         l = (float)event.entity.field_6012 + event.g;
         this.setupTransforms(event.entity, event.matrixStack, l, h, event.g);
         event.matrixStack.method_22905(-1.0F, -1.0F, 1.0F);
         event.matrixStack.method_22905(0.9375F, 0.9375F, 0.9375F);
         event.matrixStack.method_46416(0.0F, -1.501F, 0.0F);
         n = 0.0F;
         float o = 0.0F;
         if (!event.entity.method_5765() && event.entity.method_5805()) {
            n = event.entity.field_42108.method_48570(event.g);
            o = event.entity.field_42108.method_48572(event.g);
            if (event.entity.method_6109()) {
               o *= 3.0F;
            }

            if (n > 1.0F) {
               n = 1.0F;
            }
         }

         event.model.method_2816(event.entity, o, n, event.g);
         event.model.method_2819(event.entity, o, n, l, k, m);
         boolean bl = !event.entity.method_5767();
         boolean var10000;
         if (!bl && !event.entity.method_5756(mc.field_1724)) {
            var10000 = true;
         } else {
            var10000 = false;
         }

         int p = class_922.method_23622(event.entity, 0.0F);
         event.model.method_2828(event.matrixStack, vertexConsumer, event.i, p, 1.0F, 1.0F, 1.0F, 1.0F);
         tessellator.method_1350();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         if (!event.entity.method_7325()) {
            Iterator var16 = event.features.iterator();

            while(var16.hasNext()) {
               Object featureRenderer = var16.next();
               ((class_3887)featureRenderer).method_4199(event.matrixStack, event.vertexConsumerProvider, event.i, event.entity, o, n, event.g, l, k, m);
            }
         }

         event.matrixStack.method_22909();
         event.cancel();
      }
   }

   protected void setupTransforms(class_1309 entity, class_4587 matrices, float animationProgress, float bodyYaw, float tickDelta) {
      if (entity.method_32314()) {
         bodyYaw += (float)(Math.cos((double)entity.field_6012 * 3.25D) * 3.141592653589793D * 0.4000000059604645D);
      }

      if (!entity.method_41328(class_4050.field_18078)) {
         matrices.method_22907(class_7833.field_40716.rotationDegrees(180.0F - bodyYaw));
      }

      if (entity.field_6213 > 0) {
         float f = ((float)entity.field_6213 + tickDelta - 1.0F) / 20.0F * 1.6F;
         if ((f = class_3532.method_15355(f)) > 1.0F) {
            f = 1.0F;
         }

         matrices.method_22907(class_7833.field_40718.rotationDegrees(f * 90.0F));
      } else if (entity.method_6123()) {
         matrices.method_22907(class_7833.field_40714.rotationDegrees(-90.0F - entity.method_36455()));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(((float)entity.field_6012 + tickDelta) * -75.0F));
      } else if (entity.method_41328(class_4050.field_18078)) {
         class_2350 direction = entity.method_18401();
         float g = direction != null ? getYaw(direction) : bodyYaw;
         matrices.method_22907(class_7833.field_40716.rotationDegrees(g));
         matrices.method_22907(class_7833.field_40718.rotationDegrees(90.0F));
         matrices.method_22907(class_7833.field_40716.rotationDegrees(270.0F));
      } else if (class_922.method_38563(entity)) {
         matrices.method_46416(0.0F, entity.method_17682() + 0.1F, 0.0F);
         matrices.method_22907(class_7833.field_40718.rotationDegrees(180.0F));
      }

   }

   @EventListener
   public void onRenderCrystal(RenderCrystalEvent event) {
      if ((Boolean)this.otherConfig.getValue()) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         event.matrixStack.method_22903();
         float h = class_892.method_23155(event.endCrystalEntity, event.g);
         float j = ((float)event.endCrystalEntity.field_7034 + event.g) * 3.0F;
         class_289 tessellator = class_289.method_1348();
         class_287 vertexConsumer = tessellator.method_1349();
         RenderSystem.setShader(class_757::method_34539);
         RenderSystem.lineWidth(2.0F);
         vertexConsumer.method_1328(class_5596.field_27382, class_290.field_1592);
         event.matrixStack.method_22903();
         Color color = (Color)this.colorConfig.getValue();
         RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
         event.matrixStack.method_22905(2.0F, 2.0F, 2.0F);
         event.matrixStack.method_46416(0.0F, -0.5F, 0.0F);
         int k = class_4608.field_21444;
         event.matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
         event.matrixStack.method_46416(0.0F, 1.5F + h / 2.0F, 0.0F);
         event.matrixStack.method_22907((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         event.frame.method_22698(event.matrixStack, vertexConsumer, event.i, k);
         float l = 0.875F;
         event.matrixStack.method_22905(0.875F, 0.875F, 0.875F);
         event.matrixStack.method_22907((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         event.matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
         event.frame.method_22698(event.matrixStack, vertexConsumer, event.i, k);
         event.matrixStack.method_22905(0.875F, 0.875F, 0.875F);
         event.matrixStack.method_22907((new Quaternionf()).setAngleAxis(1.0471976F, SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
         event.matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
         event.core.method_22698(event.matrixStack, vertexConsumer, event.i, k);
         event.matrixStack.method_22909();
         event.matrixStack.method_22909();
         tessellator.method_1350();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         RenderSystem.enableCull();
         event.cancel();
      }
   }

   @EventListener
   public void onRenderArm(RenderArmEvent event) {
      if ((Boolean)this.handsConfig.getValue()) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         class_289 tessellator = class_289.method_1348();
         class_287 vertexConsumer = tessellator.method_1349();
         RenderSystem.setShader(class_757::method_34539);
         RenderSystem.lineWidth(2.0F);
         vertexConsumer.method_1328(class_5596.field_27382, class_290.field_1592);
         event.matrices.method_22903();
         Color color = (Color)this.colorConfig.getValue();
         RenderSystem.setShaderColor((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, class_3532.method_15363(((float)color.getAlpha() + 40.0F) / 255.0F, 0.0F, 1.0F));
         boolean bl = event.arm != class_1306.field_6182;
         float f = bl ? 1.0F : -1.0F;
         float g = class_3532.method_15355(event.swingProgress);
         float h = -0.3F * class_3532.method_15374(g * 3.1415927F);
         float i = 0.4F * class_3532.method_15374(g * 6.2831855F);
         float j = -0.4F * class_3532.method_15374(event.swingProgress * 3.1415927F);
         event.matrices.method_46416(f * (h + 0.64000005F), i + -0.6F + event.equipProgress * -0.6F, j + -0.71999997F);
         event.matrices.method_22907(class_7833.field_40716.rotationDegrees(f * 45.0F));
         float k = class_3532.method_15374(event.swingProgress * event.swingProgress * 3.1415927F);
         float l = class_3532.method_15374(g * 3.1415927F);
         event.matrices.method_22907(class_7833.field_40716.rotationDegrees(f * l * 70.0F));
         event.matrices.method_22907(class_7833.field_40718.rotationDegrees(f * k * -20.0F));
         event.matrices.method_46416(f * -1.0F, 3.6F, 3.5F);
         event.matrices.method_22907(class_7833.field_40718.rotationDegrees(f * 120.0F));
         event.matrices.method_22907(class_7833.field_40714.rotationDegrees(200.0F));
         event.matrices.method_22907(class_7833.field_40716.rotationDegrees(f * -135.0F));
         event.matrices.method_46416(f * 5.6F, 0.0F, 0.0F);
         event.playerEntityRenderer.method_4218(mc.field_1724);
         ((class_591)event.playerEntityRenderer.method_4038()).field_3447 = 0.0F;
         ((class_591)event.playerEntityRenderer.method_4038()).field_3400 = false;
         ((class_591)event.playerEntityRenderer.method_4038()).field_3396 = 0.0F;
         ((class_591)event.playerEntityRenderer.method_4038()).method_17087(mc.field_1724, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         if (event.arm == class_1306.field_6183) {
            ((class_591)event.playerEntityRenderer.method_4038()).field_3401.field_3654 = 0.0F;
            ((class_591)event.playerEntityRenderer.method_4038()).field_3401.method_22698(event.matrices, vertexConsumer, event.light, class_4608.field_21444);
            ((class_591)event.playerEntityRenderer.method_4038()).field_3486.field_3654 = 0.0F;
            ((class_591)event.playerEntityRenderer.method_4038()).field_3486.method_22698(event.matrices, vertexConsumer, event.light, class_4608.field_21444);
         } else {
            ((class_591)event.playerEntityRenderer.method_4038()).field_27433.field_3654 = 0.0F;
            ((class_591)event.playerEntityRenderer.method_4038()).field_27433.method_22698(event.matrices, vertexConsumer, event.light, class_4608.field_21444);
            ((class_591)event.playerEntityRenderer.method_4038()).field_3484.field_3654 = 0.0F;
            ((class_591)event.playerEntityRenderer.method_4038()).field_3484.method_22698(event.matrices, vertexConsumer, event.light, class_4608.field_21444);
         }

         tessellator.method_1350();
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
         event.matrices.method_22909();
         event.cancel();
      }

   }

   private boolean checkChams(class_1309 entity) {
      if (entity instanceof class_1657 && (Boolean)this.playersConfig.getValue()) {
         return (Boolean)this.selfConfig.getValue() || entity != mc.field_1724;
      } else {
         return (!entity.method_5767() || (Boolean)this.invisiblesConfig.getValue()) && (EntityUtil.isMonster(entity) && (Boolean)this.monstersConfig.getValue() || (EntityUtil.isNeutral(entity) || EntityUtil.isPassive(entity)) && (Boolean)this.animalsConfig.getValue());
      }
   }

   public static enum ChamsMode {
      NORMAL;

      // $FF: synthetic method
      private static ChamsModule.ChamsMode[] $values() {
         return new ChamsModule.ChamsMode[]{NORMAL};
      }
   }
}
