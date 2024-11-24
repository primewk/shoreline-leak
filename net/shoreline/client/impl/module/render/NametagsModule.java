package net.shoreline.client.impl.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1087;
import net.minecraft.class_1091;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1775;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1921;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4720;
import net.minecraft.class_640;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.Interpolation;
import net.shoreline.client.api.render.RenderLayersClient;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.impl.event.render.entity.RenderLabelEvent;
import net.shoreline.client.init.Fonts;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorItemRenderer;
import net.shoreline.client.util.render.ColorUtil;
import net.shoreline.client.util.world.FakePlayerEntity;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class NametagsModule extends ToggleModule {
   Config<Boolean> armorConfig = new BooleanConfig("Armor", "Displays the player's armor", true);
   Config<Boolean> enchantmentsConfig = new BooleanConfig("Enchantments", "Displays a list of the item's enchantments", true);
   Config<Boolean> durabilityConfig = new BooleanConfig("Durability", "Displays item durability", true);
   Config<Boolean> itemNameConfig = new BooleanConfig("ItemName", "Displays the player's current held item name", false);
   Config<Boolean> entityIdConfig = new BooleanConfig("EntityId", "Displays the player's entity id", false);
   Config<Boolean> gamemodeConfig = new BooleanConfig("Gamemode", "Displays the player's gamemode", false);
   Config<Boolean> pingConfig = new BooleanConfig("Ping", "Displays the player's server connection ping", true);
   Config<Boolean> healthConfig = new BooleanConfig("Health", "Displays the player's current health", true);
   Config<Boolean> totemsConfig = new BooleanConfig("Totems", "Displays the player's popped totem count", false);
   Config<Float> scalingConfig = new NumberConfig("Scaling", "The nametag label scale", 0.001F, 0.003F, 0.01F);
   Config<Boolean> invisiblesConfig = new BooleanConfig("Invisibles", "Renders nametags on invisible players", true);
   Config<Boolean> borderedConfig = new BooleanConfig("TextBorder", "Renders a border behind the nametag", true);

   public NametagsModule() {
      super("Nametags", "Renders info on player nametags", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1773 != null && mc.method_1560() != null) {
         class_243 interpolate = Interpolation.getRenderPosition(mc.method_1560(), mc.method_1488());
         class_4184 camera = mc.field_1773.method_19418();
         class_243 pos = camera.method_19326();
         Iterator var5 = mc.field_1687.method_18112().iterator();

         while(true) {
            class_1657 player;
            do {
               do {
                  do {
                     class_1297 entity;
                     do {
                        if (!var5.hasNext()) {
                           RenderSystem.enableBlend();
                           return;
                        }

                        entity = (class_1297)var5.next();
                     } while(!(entity instanceof class_1657));

                     player = (class_1657)entity;
                  } while(player == mc.field_1724 && !Modules.FREECAM.isEnabled());
               } while(!player.method_5805());
            } while(!(Boolean)this.invisiblesConfig.getValue() && player.method_5767());

            String info = this.getNametagInfo(player);
            class_243 pinterpolate = Interpolation.getRenderPosition(player, mc.method_1488());
            double rx = player.method_23317() - pinterpolate.method_10216();
            double ry = player.method_23318() - pinterpolate.method_10214();
            double rz = player.method_23321() - pinterpolate.method_10215();
            int width = RenderManager.textWidth(info);
            float hwidth = (float)width / 2.0F;
            double dx = pos.method_10216() - interpolate.method_10216() - rx;
            double dy = pos.method_10214() - interpolate.method_10214() - ry;
            double dz = pos.method_10215() - interpolate.method_10215() - rz;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (!(dist > 4096.0D)) {
               float scaling = 0.0018F + (Float)this.scalingConfig.getValue() * (float)dist;
               if (dist <= 8.0D) {
                  scaling = 0.0245F;
               }

               this.renderInfo(info, hwidth, player, rx, ry, rz, camera, scaling);
            }
         }
      }
   }

   @EventListener
   public void onRenderLabel(RenderLabelEvent event) {
      if (event.getEntity() instanceof class_1657 && event.getEntity() != mc.field_1724) {
         event.cancel();
      }

   }

   private void renderInfo(String info, float width, class_1657 entity, double x, double y, double z, class_4184 camera, float scaling) {
      class_243 pos = camera.method_19326();
      class_4587 matrices = new class_4587();
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0F));
      matrices.method_22904(x - pos.method_10216(), y + (double)entity.method_17682() + (double)(entity.method_5715() ? 0.4F : 0.43F) - pos.method_10214(), z - pos.method_10215());
      matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
      matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
      matrices.method_22905(-scaling, -scaling, -1.0F);
      if ((Boolean)this.borderedConfig.getValue()) {
         double var10001 = (double)(-width - 1.0F);
         double var10003 = (double)(width * 2.0F + 2.0F);
         Objects.requireNonNull(mc.field_1772);
         RenderManager.rect(matrices, var10001, -1.0D, var10003, (double)(9.0F + 1.0F), 0.0D, 1426064384);
      }

      int color = this.getNametagColor(entity);
      RenderManager.post(() -> {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         GL11.glDepthFunc(519);
         Fonts.VANILLA.drawWithShadow(matrices, info, -width, 0.0F, color);
         if ((Boolean)this.armorConfig.getValue()) {
            this.renderItems(matrices, entity);
         }

         GL11.glDepthFunc(515);
         RenderSystem.disableBlend();
      });
   }

   private void renderItems(class_4587 matrixStack, class_1657 player) {
      List<class_1799> displayItems = new CopyOnWriteArrayList();
      if (!player.method_6079().method_7960()) {
         displayItems.add(player.method_6079());
      }

      player.method_31548().field_7548.forEach((armorStack) -> {
         if (!armorStack.method_7960()) {
            displayItems.add(armorStack);
         }

      });
      if (!player.method_6047().method_7960()) {
         displayItems.add(player.method_6047());
      }

      Collections.reverse(displayItems);
      float n10 = 0.0F;
      int n11 = 0;
      Iterator var6 = displayItems.iterator();

      class_1799 heldItem;
      while(var6.hasNext()) {
         heldItem = (class_1799)var6.next();
         n10 -= 8.0F;
         if (heldItem.method_7921().size() > n11) {
            n11 = heldItem.method_7921().size();
         }
      }

      float m2 = this.enchantOffset(n11);

      for(Iterator var10 = displayItems.iterator(); var10.hasNext(); n10 += 16.0F) {
         class_1799 stack = (class_1799)var10.next();
         matrixStack.method_22903();
         matrixStack.method_46416(n10, m2, 0.0F);
         matrixStack.method_46416(8.0F, 8.0F, 0.0F);
         matrixStack.method_22905(16.0F, 16.0F, 0.0F);
         matrixStack.method_34425((new Matrix4f()).scaling(1.0F, -1.0F, 0.0F));
         this.renderItem(stack, class_811.field_4317, 16777215, 10, matrixStack, mc.method_22940().method_23000(), mc.field_1687, 0);
         mc.method_22940().method_23000().method_22993();
         matrixStack.method_22909();
         this.renderItemOverlay(matrixStack, stack, (int)n10, (int)m2);
         matrixStack.method_22905(0.5F, 0.5F, 0.5F);
         if ((Boolean)this.durabilityConfig.getValue()) {
            this.renderDurability(matrixStack, stack, n10 + 2.0F, m2 - 4.5F);
         }

         if ((Boolean)this.enchantmentsConfig.getValue()) {
            this.renderEnchants(matrixStack, stack, n10 + 2.0F, m2);
         }

         matrixStack.method_22905(2.0F, 2.0F, 2.0F);
      }

      heldItem = player.method_6047();
      if (!heldItem.method_7960()) {
         matrixStack.method_22905(0.5F, 0.5F, 0.5F);
         if ((Boolean)this.itemNameConfig.getValue()) {
            this.renderItemName(matrixStack, heldItem, 0.0F, (Boolean)this.durabilityConfig.getValue() ? m2 - 9.0F : m2 - 4.5F);
         }

         matrixStack.method_22905(2.0F, 2.0F, 2.0F);
      }
   }

   private void renderItem(class_1799 stack, class_811 renderMode, int light, int overlay, class_4587 matrices, class_4597 vertexConsumers, class_1937 world, int seed) {
      class_1087 bakedModel = mc.method_1480().method_4019(stack, world, (class_1309)null, seed);
      if (!stack.method_7960()) {
         boolean bl = renderMode == class_811.field_4317 || renderMode == class_811.field_4318 || renderMode == class_811.field_4319;
         if (bl) {
            if (stack.method_31574(class_1802.field_8547)) {
               bakedModel = mc.method_1480().method_4012().method_3303().method_4742(class_1091.method_45910("trident", "inventory"));
            } else if (stack.method_31574(class_1802.field_27070)) {
               bakedModel = mc.method_1480().method_4012().method_3303().method_4742(class_1091.method_45910("spyglass", "inventory"));
            }
         }

         bakedModel.method_4709().method_3503(renderMode).method_23075(false, matrices);
         matrices.method_46416(-0.5F, -0.5F, -0.5F);
         if (!bakedModel.method_4713() && (!stack.method_31574(class_1802.field_8547) || bl)) {
            ((AccessorItemRenderer)mc.method_1480()).hookRenderBakedItemModel(bakedModel, stack, light, overlay, matrices, getItemGlintConsumer(vertexConsumers, RenderLayersClient.ITEM_ENTITY_TRANSLUCENT_CULL, stack.method_7958()));
         } else {
            ((AccessorItemRenderer)mc.method_1480()).hookGetBuiltinModelItemRenderer().method_3166(stack, renderMode, matrices, vertexConsumers, light, overlay);
         }

      }
   }

   public static class_4588 getItemGlintConsumer(class_4597 vertexConsumers, class_1921 layer, boolean glint) {
      return glint ? class_4720.method_24037(vertexConsumers.getBuffer(RenderLayersClient.GLINT), vertexConsumers.getBuffer(layer)) : vertexConsumers.getBuffer(layer);
   }

   private void renderItemOverlay(class_4587 matrixStack, class_1799 stack, int x, int y) {
      matrixStack.method_22903();
      if (stack.method_7947() != 1) {
         String string = String.valueOf(stack.method_7947());
         Fonts.VANILLA.drawWithShadow(matrixStack, string, (float)(x + 17 - mc.field_1772.method_1727(string)), (float)y + 9.0F, -1);
      }

      if (stack.method_31578()) {
         int i = stack.method_31579();
         int j = stack.method_31580();
         int k = x + 2;
         int l = y + 13;
         RenderManager.rect(matrixStack, (double)k, (double)l, 13.0D, 1.0D, -16777216);
         RenderManager.rect(matrixStack, (double)k, (double)l, (double)i, 1.0D, j | -16777216);
      }

      matrixStack.method_22909();
   }

   private void renderDurability(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
      if (itemStack.method_7963()) {
         int n = itemStack.method_7936();
         int n2 = itemStack.method_7919();
         int durability = (int)((float)(n - n2) / (float)n * 100.0F);
         Fonts.VANILLA.drawWithShadow(matrixStack, durability + "%", x * 2.0F, y * 2.0F, ColorUtil.hslToColor((float)(n - n2) / (float)n * 120.0F, 100.0F, 50.0F, 1.0F).getRGB());
      }
   }

   private void renderEnchants(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
      if (itemStack.method_7909() instanceof class_1775) {
         Fonts.VANILLA.drawWithShadow(matrixStack, "God", x * 2.0F, y * 2.0F, -3977663);
      } else if (itemStack.method_7942()) {
         Map<class_1887, Integer> enchants = class_1890.method_8222(itemStack);
         float n2 = 0.0F;

         for(Iterator var7 = enchants.keySet().iterator(); var7.hasNext(); n2 += 4.5F) {
            class_1887 enchantment = (class_1887)var7.next();
            int lvl = (Integer)enchants.get(enchantment);
            StringBuilder enchantString = new StringBuilder();
            String translatedName = enchantment.method_8179(lvl).getString();
            if (translatedName.contains("Vanish")) {
               enchantString.append("Van");
            } else if (translatedName.contains("Bind")) {
               enchantString.append("Bind");
            } else {
               int maxLen = lvl > 1 ? 2 : 3;
               if (translatedName.length() > maxLen) {
                  translatedName = translatedName.substring(0, maxLen);
               }

               enchantString.append(translatedName);
               enchantString.append(lvl);
            }

            Fonts.VANILLA.drawWithShadow(matrixStack, enchantString.toString(), x * 2.0F, (y + n2) * 2.0F, -1);
         }

      }
   }

   private float enchantOffset(int n) {
      if ((Boolean)this.enchantmentsConfig.getValue() && n > 3) {
         float n2 = -14.0F;
         n2 -= (float)(n - 3) * 4.5F;
         return n2;
      } else {
         return -18.0F;
      }
   }

   private void renderItemName(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
      String itemName = itemStack.method_7964().getString();
      float width = (float)mc.field_1772.method_1727(itemName) / 4.0F;
      Fonts.VANILLA.drawWithShadow(matrixStack, itemName, (x - width) * 2.0F, y * 2.0F, -1);
   }

   private String getNametagInfo(class_1657 player) {
      StringBuilder info = new StringBuilder(player.method_5477().getString());
      info.append(" ");
      if ((Boolean)this.entityIdConfig.getValue()) {
         info.append("ID: ");
         info.append(player.method_5628());
         info.append(" ");
      }

      if ((Boolean)this.gamemodeConfig.getValue()) {
         if (player.method_7337()) {
            info.append("[C] ");
         } else if (player.method_7325()) {
            info.append("[I] ");
         } else {
            info.append("[S] ");
         }
      }

      if ((Boolean)this.pingConfig.getValue() && mc.method_1562() != null) {
         class_640 playerEntry = mc.method_1562().method_2871(player.method_7334().getId());
         if (playerEntry != null) {
            info.append(playerEntry.method_2959());
            info.append("ms ");
         }
      }

      if ((Boolean)this.healthConfig.getValue()) {
         double health = Math.ceil((double)(player.method_6032() + player.method_6067()));
         class_124 hcolor;
         if (health > 18.0D) {
            hcolor = class_124.field_1060;
         } else if (health > 16.0D) {
            hcolor = class_124.field_1077;
         } else if (health > 12.0D) {
            hcolor = class_124.field_1054;
         } else if (health > 8.0D) {
            hcolor = class_124.field_1065;
         } else if (health > 4.0D) {
            hcolor = class_124.field_1061;
         } else {
            hcolor = class_124.field_1079;
         }

         int phealth = (int)health;
         info.append(hcolor);
         info.append(phealth);
         info.append(" ");
      }

      if ((Boolean)this.totemsConfig.getValue() && player != mc.field_1724) {
         int totems = Managers.TOTEM.getTotems(player);
         if (totems > 0) {
            class_124 pcolor = class_124.field_1060;
            if (totems > 1) {
               pcolor = class_124.field_1077;
            }

            if (totems > 2) {
               pcolor = class_124.field_1054;
            }

            if (totems > 3) {
               pcolor = class_124.field_1065;
            }

            if (totems > 4) {
               pcolor = class_124.field_1061;
            }

            if (totems > 5) {
               pcolor = class_124.field_1079;
            }

            info.append(pcolor);
            info.append(-totems);
            info.append(" ");
         }
      }

      return info.toString().trim();
   }

   private int getNametagColor(class_1657 player) {
      if (player == mc.field_1724) {
         return Modules.COLORS.getRGB(255);
      } else if (Managers.SOCIAL.isFriend(player.method_5477())) {
         return -10027009;
      } else if (player.method_5767()) {
         return -56064;
      } else if (player instanceof FakePlayerEntity) {
         return -1113785;
      } else {
         return player.method_5715() ? -26368 : -1;
      }
   }

   public float getScaling() {
      return (Float)this.scalingConfig.getValue();
   }
}
