package net.shoreline.client.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.class_1007;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_922;
import net.minecraft.class_293.class_5596;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.Interpolation;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Modules;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SkeletonModule extends ToggleModule {
   public SkeletonModule() {
      super("Skeleton", "Renders a skeleton to show player limbs", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent.Game event) {
      class_4587 matrixStack = event.getMatrices();
      float g = event.getTickDelta();
      RenderSystem.setShader(class_757::method_34540);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(class_310.method_1517());
      RenderSystem.enableCull();
      Iterator var4 = mc.field_1687.method_18112().iterator();

      while(true) {
         class_1297 entity;
         class_1657 playerEntity;
         do {
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        RenderSystem.disableCull();
                        RenderSystem.disableBlend();
                        RenderSystem.enableDepthTest();
                        RenderSystem.depthMask(true);
                        RenderSystem.setShader(class_757::method_34540);
                        return;
                     }

                     entity = (class_1297)var4.next();
                  } while(entity == null);
               } while(!entity.method_5805());
            } while(!(entity instanceof class_1657));

            playerEntity = (class_1657)entity;
         } while(mc.field_1690.method_31044().method_31034() && playerEntity == mc.field_1724);

         class_243 skeletonPos = Interpolation.getInterpolatedPosition(entity, g);
         class_1007 livingEntityRenderer = (class_1007)((class_922)mc.method_1561().method_3953(playerEntity));
         class_591<class_1657> playerEntityModel = (class_591)livingEntityRenderer.method_4038();
         float h = class_3532.method_17821(g, playerEntity.field_6220, playerEntity.field_6283);
         float j = class_3532.method_17821(g, playerEntity.field_6259, playerEntity.field_6241);
         float q = playerEntity.field_42108.method_48569() - playerEntity.field_42108.method_48566() * (1.0F - g);
         float p = playerEntity.field_42108.method_48570(g);
         float o = (float)playerEntity.field_6012 + g;
         float k = j - h;
         float m = playerEntity.method_5695(g);
         playerEntityModel.method_17086(playerEntity, q, p, g);
         playerEntityModel.method_17087(playerEntity, q, p, o, k, m);
         boolean swimming = playerEntity.method_20232();
         boolean sneaking = playerEntity.method_5715();
         boolean flying = playerEntity.method_6128();
         class_630 head = playerEntityModel.field_3398;
         class_630 leftArm = playerEntityModel.field_27433;
         class_630 rightArm = playerEntityModel.field_3401;
         class_630 leftLeg = playerEntityModel.field_3397;
         class_630 rightLeg = playerEntityModel.field_3392;
         matrixStack.method_22903();
         matrixStack.method_22904(skeletonPos.field_1352, skeletonPos.field_1351, skeletonPos.field_1350);
         if (swimming) {
            matrixStack.method_46416(0.0F, 0.35F, 0.0F);
         }

         matrixStack.method_22907((new Quaternionf()).setAngleAxis((double)(h + 180.0F) * 3.141592653589793D / 180.0D, 0.0D, -1.0D, 0.0D));
         if (swimming || flying) {
            matrixStack.method_22907((new Quaternionf()).setAngleAxis((double)(90.0F + m) * 3.141592653589793D / 180.0D, -1.0D, 0.0D, 0.0D));
         }

         if (swimming) {
            matrixStack.method_46416(0.0F, -0.95F, 0.0F);
         }

         class_289 tessellator = class_289.method_1348();
         class_287 bufferBuilder = tessellator.method_1349();
         bufferBuilder.method_1328(class_5596.field_29344, class_290.field_1576);
         Matrix4f matrix4f = matrixStack.method_23760().method_23761();
         Color skeletonColor = Modules.COLORS.getColor();
         bufferBuilder.method_22918(matrix4f, 0.0F, sneaking ? 0.6F : 0.7F, sneaking ? 0.23F : 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.0F, sneaking ? 1.05F : 1.4F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, -0.37F, sneaking ? 1.05F : 1.35F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.37F, sneaking ? 1.05F : 1.35F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, -0.15F, sneaking ? 0.6F : 0.7F, sneaking ? 0.23F : 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.15F, sneaking ? 0.6F : 0.7F, sneaking ? 0.23F : 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         matrixStack.method_22903();
         matrixStack.method_46416(0.0F, sneaking ? 1.05F : 1.4F, 0.0F);
         this.rotateSkeleton(matrixStack, head);
         matrix4f = matrixStack.method_23760().method_23761();
         bufferBuilder.method_22918(matrix4f, 0.0F, 0.0F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.0F, 0.25F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         matrixStack.method_22909();
         matrixStack.method_22903();
         matrixStack.method_46416(0.15F, sneaking ? 0.6F : 0.7F, sneaking ? 0.23F : 0.0F);
         this.rotateSkeleton(matrixStack, rightLeg);
         matrix4f = matrixStack.method_23760().method_23761();
         bufferBuilder.method_22918(matrix4f, 0.0F, 0.0F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.0F, -0.6F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         matrixStack.method_22909();
         matrixStack.method_22903();
         matrixStack.method_46416(-0.15F, sneaking ? 0.6F : 0.7F, sneaking ? 0.23F : 0.0F);
         this.rotateSkeleton(matrixStack, leftLeg);
         matrix4f = matrixStack.method_23760().method_23761();
         bufferBuilder.method_22918(matrix4f, 0.0F, 0.0F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.0F, -0.6F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         matrixStack.method_22909();
         matrixStack.method_22903();
         matrixStack.method_46416(0.37F, sneaking ? 1.05F : 1.35F, 0.0F);
         this.rotateSkeleton(matrixStack, rightArm);
         matrix4f = matrixStack.method_23760().method_23761();
         bufferBuilder.method_22918(matrix4f, 0.0F, 0.0F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.0F, -0.55F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         matrixStack.method_22909();
         matrixStack.method_22903();
         matrixStack.method_46416(-0.37F, sneaking ? 1.05F : 1.35F, 0.0F);
         this.rotateSkeleton(matrixStack, leftArm);
         matrix4f = matrixStack.method_23760().method_23761();
         bufferBuilder.method_22918(matrix4f, 0.0F, 0.0F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         bufferBuilder.method_22918(matrix4f, 0.0F, -0.55F, 0.0F).method_22915((float)skeletonColor.getRed() / 255.0F, (float)skeletonColor.getGreen() / 255.0F, (float)skeletonColor.getBlue() / 255.0F, 1.0F).method_1344();
         matrixStack.method_22909();
         tessellator.method_1350();
         if (swimming) {
            matrixStack.method_46416(0.0F, 0.95F, 0.0F);
         }

         if (swimming || flying) {
            matrixStack.method_22907((new Quaternionf()).setAngleAxis((double)(90.0F + m) * 3.141592653589793D / 180.0D, 1.0D, 0.0D, 0.0D));
         }

         if (swimming) {
            matrixStack.method_46416(0.0F, -0.35F, 0.0F);
         }

         matrixStack.method_22907((new Quaternionf()).setAngleAxis((double)(h + 180.0F) * 3.141592653589793D / 180.0D, 0.0D, 1.0D, 0.0D));
         matrixStack.method_22904(-skeletonPos.field_1352, -skeletonPos.field_1351, -skeletonPos.field_1350);
         matrixStack.method_22909();
      }
   }

   private void rotateSkeleton(class_4587 matrix, class_630 modelPart) {
      if (modelPart.field_3674 != 0.0F) {
         matrix.method_22907(class_7833.field_40718.rotation(modelPart.field_3674));
      }

      if (modelPart.field_3675 != 0.0F) {
         matrix.method_22907(class_7833.field_40715.rotation(modelPart.field_3675));
      }

      if (modelPart.field_3654 != 0.0F) {
         matrix.method_22907(class_7833.field_40713.rotation(modelPart.field_3654));
      }

   }
}
