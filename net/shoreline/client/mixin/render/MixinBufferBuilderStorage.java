package net.shoreline.client.mixin.render;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SortedMap;
import net.minecraft.class_1088;
import net.minecraft.class_156;
import net.minecraft.class_1921;
import net.minecraft.class_287;
import net.minecraft.class_4597;
import net.minecraft.class_4599;
import net.minecraft.class_4618;
import net.minecraft.class_4722;
import net.minecraft.class_750;
import net.minecraft.class_4597.class_4598;
import net.shoreline.client.api.render.RenderLayersClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4599.class})
public class MixinBufferBuilderStorage {
   @Shadow
   @Final
   private class_750 field_20956;
   @Final
   @Shadow
   @Mutable
   private class_4598 field_46901;
   @Final
   @Shadow
   @Mutable
   private class_4618 field_20961;

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void hookInit(int maxBlockBuildersPoolSize, CallbackInfo ci) {
      SortedMap sortedMap = (SortedMap)class_156.method_654(new Object2ObjectLinkedOpenHashMap(), (map) -> {
         map.put(class_4722.method_24073(), this.field_20956.method_3154(class_1921.method_23577()));
         map.put(class_4722.method_24074(), this.field_20956.method_3154(class_1921.method_23581()));
         map.put(class_4722.method_24059(), this.field_20956.method_3154(class_1921.method_23579()));
         map.put(class_4722.method_24076(), this.field_20956.method_3154(class_1921.method_23583()));
         map.put(class_4722.method_24067(), new class_287(class_4722.method_24067().method_22722()));
         map.put(class_4722.method_24069(), new class_287(class_4722.method_24069().method_22722()));
         map.put(class_4722.method_24071(), new class_287(class_4722.method_24071().method_22722()));
         map.put(class_4722.method_45783(), new class_287(class_4722.method_45783().method_22722()));
         map.put(class_4722.method_24072(), new class_287(786432));
         map.put(class_1921.method_27948(), new class_287(class_1921.method_27948().method_22722()));
         map.put(class_1921.method_27949(), new class_287(class_1921.method_27949().method_22722()));
         map.put(class_1921.method_23590(), new class_287(class_1921.method_23590().method_22722()));
         map.put(class_1921.method_29706(), new class_287(class_1921.method_29706().method_22722()));
         map.put(class_1921.method_30676(), new class_287(class_1921.method_30676().method_22722()));
         map.put(class_1921.method_23591(), new class_287(class_1921.method_23591().method_22722()));
         map.put(class_1921.method_29707(), new class_287(class_1921.method_29707().method_22722()));
         map.put(class_1921.method_23589(), new class_287(class_1921.method_23589().method_22722()));
         map.put(RenderLayersClient.GLINT, new class_287(RenderLayersClient.GLINT.method_22722()));
         class_1088.field_21772.forEach((renderLayer) -> {
            map.put(renderLayer, new class_287(renderLayer.method_22722()));
         });
      });
      this.field_46901 = class_4597.method_22992(sortedMap, new class_287(786432));
      this.field_20961 = new class_4618(this.field_46901);
   }
}
