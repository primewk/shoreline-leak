package net.shoreline.client.impl.module.render;

import net.minecraft.class_1268;
import net.minecraft.class_7833;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.impl.event.render.item.RenderFirstPersonEvent;

public class ViewModelModule extends ToggleModule {
   Config<Float> positionXConfig = new NumberConfig("X", "Translation in x-direction", -3.0F, 0.0F, 3.0F);
   Config<Float> positionYConfig = new NumberConfig("Y", "Translation in y-direction", -3.0F, 0.0F, 3.0F);
   Config<Float> positionZConfig = new NumberConfig("Z", "Translation in z-direction", -3.0F, 0.0F, 3.0F);
   Config<Float> scaleXConfig = new NumberConfig("ScaleX", "Scaling in x-direction", 0.1F, 1.0F, 2.0F);
   Config<Float> scaleYConfig = new NumberConfig("ScaleY", "Scaling in y-direction", 0.1F, 1.0F, 2.0F);
   Config<Float> scaleZConfig = new NumberConfig("ScaleZ", "Scaling in z-direction", 0.1F, 1.0F, 2.0F);
   Config<Float> rotateXConfig = new NumberConfig("RotateX", "Rotation in x-direction", -180.0F, 0.0F, 180.0F);
   Config<Float> rotateYConfig = new NumberConfig("RotateY", "Rotation in y-direction", -180.0F, 0.0F, 180.0F);
   Config<Float> rotateZConfig = new NumberConfig("RotateZ", "Rotation in z-direction", -180.0F, 0.0F, 180.0F);

   public ViewModelModule() {
      super("ViewModel", "Changes the first-person viewmodel", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderFirstPerson(RenderFirstPersonEvent event) {
      event.matrices.method_22905((Float)this.scaleXConfig.getValue(), (Float)this.scaleYConfig.getValue(), (Float)this.scaleZConfig.getValue());
      event.matrices.method_22907(class_7833.field_40714.rotationDegrees((Float)this.rotateXConfig.getValue()));
      if (event.hand == class_1268.field_5808) {
         event.matrices.method_46416((Float)this.positionXConfig.getValue(), (Float)this.positionYConfig.getValue(), (Float)this.positionZConfig.getValue());
         event.matrices.method_22907(class_7833.field_40716.rotationDegrees((Float)this.rotateYConfig.getValue()));
         event.matrices.method_22907(class_7833.field_40718.rotationDegrees((Float)this.rotateZConfig.getValue()));
      } else {
         event.matrices.method_46416(-(Float)this.positionXConfig.getValue(), (Float)this.positionYConfig.getValue(), (Float)this.positionZConfig.getValue());
         event.matrices.method_22907(class_7833.field_40716.rotationDegrees(-(Float)this.rotateYConfig.getValue()));
         event.matrices.method_22907(class_7833.field_40718.rotationDegrees(-(Float)this.rotateZConfig.getValue()));
      }

   }
}
