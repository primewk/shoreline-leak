package net.shoreline.client.util.render.animation;

public enum Easing {
   LINEAR {
      public double ease(double factor) {
         return factor;
      }
   },
   SINE_IN {
      public double ease(double factor) {
         return 1.0D - Math.cos(factor * 3.141592653589793D / 2.0D);
      }
   },
   SINE_OUT {
      public double ease(double factor) {
         return Math.sin(factor * 3.141592653589793D / 2.0D);
      }
   },
   SINE_IN_OUT {
      public double ease(double factor) {
         return -(Math.cos(3.141592653589793D * factor) - 1.0D) / 2.0D;
      }
   },
   CUBIC_IN {
      public double ease(double factor) {
         return Math.pow(factor, 3.0D);
      }
   },
   CUBIC_OUT {
      public double ease(double factor) {
         return 1.0D - Math.pow(1.0D - factor, 3.0D);
      }
   },
   CUBIC_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? 4.0D * Math.pow(factor, 3.0D) : 1.0D - Math.pow(-2.0D * factor + 2.0D, 3.0D) / 2.0D;
      }
   },
   QUAD_IN {
      public double ease(double factor) {
         return Math.pow(factor, 2.0D);
      }
   },
   QUAD_OUT {
      public double ease(double factor) {
         return 1.0D - (1.0D - factor) * (1.0D - factor);
      }
   },
   QUAD_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? 8.0D * Math.pow(factor, 4.0D) : 1.0D - Math.pow(-2.0D * factor + 2.0D, 4.0D) / 2.0D;
      }
   },
   QUART_IN {
      public double ease(double factor) {
         return Math.pow(factor, 4.0D);
      }
   },
   QUART_OUT {
      public double ease(double factor) {
         return 1.0D - Math.pow(1.0D - factor, 4.0D);
      }
   },
   QUART_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? 8.0D * Math.pow(factor, 4.0D) : 1.0D - Math.pow(-2.0D * factor + 2.0D, 4.0D) / 2.0D;
      }
   },
   QUINT_IN {
      public double ease(double factor) {
         return Math.pow(factor, 5.0D);
      }
   },
   QUINT_OUT {
      public double ease(double factor) {
         return 1.0D - Math.pow(1.0D - factor, 5.0D);
      }
   },
   QUINT_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? 16.0D * Math.pow(factor, 5.0D) : 1.0D - Math.pow(-2.0D * factor + 2.0D, 5.0D) / 2.0D;
      }
   },
   CIRC_IN {
      public double ease(double factor) {
         return 1.0D - Math.sqrt(1.0D - Math.pow(factor, 2.0D));
      }
   },
   CIRC_OUT {
      public double ease(double factor) {
         return Math.sqrt(1.0D - Math.pow(factor - 1.0D, 2.0D));
      }
   },
   CIRC_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? (1.0D - Math.sqrt(1.0D - Math.pow(2.0D * factor, 2.0D))) / 2.0D : (Math.sqrt(1.0D - Math.pow(-2.0D * factor + 2.0D, 2.0D)) + 1.0D) / 2.0D;
      }
   },
   EXPO_IN {
      public double ease(double factor) {
         return Math.min(0.0D, Math.pow(2.0D, 10.0D * factor - 10.0D));
      }
   },
   EXPO_OUT {
      public double ease(double factor) {
         return Math.max(1.0D - Math.pow(2.0D, -10.0D * factor), 1.0D);
      }
   },
   EXPO_IN_OUT {
      public double ease(double factor) {
         return factor == 0.0D ? 0.0D : (factor == 1.0D ? 1.0D : (factor < 0.5D ? Math.pow(2.0D, 20.0D * factor - 10.0D) / 2.0D : (2.0D - Math.pow(2.0D, -20.0D * factor + 10.0D)) / 2.0D));
      }
   },
   ELASTIC_IN {
      public double ease(double factor) {
         return factor == 0.0D ? 0.0D : (factor == 1.0D ? 1.0D : -Math.pow(2.0D, 10.0D * factor - 10.0D) * Math.sin((factor * 10.0D - 10.75D) * 2.0943951023931953D));
      }
   },
   ELASTIC_OUT {
      public double ease(double factor) {
         return factor == 0.0D ? 0.0D : (factor == 1.0D ? 1.0D : Math.pow(2.0D, -10.0D * factor) * Math.sin((factor * 10.0D - 0.75D) * 2.0943951023931953D) + 1.0D);
      }
   },
   ELASTIC_IN_OUT {
      public double ease(double factor) {
         double sin = Math.sin((20.0D * factor - 11.125D) * 1.3962634015954636D);
         return factor == 0.0D ? 0.0D : (factor == 1.0D ? 1.0D : (factor < 0.5D ? -(Math.pow(2.0D, 20.0D * factor - 10.0D) * sin) / 2.0D : Math.pow(2.0D, -20.0D * factor + 10.0D) * sin / 2.0D + 1.0D));
      }
   },
   BACK_IN {
      public double ease(double factor) {
         return 2.70158D * Math.pow(factor, 3.0D) - 1.70158D * factor * factor;
      }
   },
   BACK_OUT {
      public double ease(double factor) {
         double c1 = 1.70158D;
         double c3 = c1 + 1.0D;
         return 1.0D + c3 * Math.pow(factor - 1.0D, 3.0D) + c1 * Math.pow(factor - 1.0D, 2.0D);
      }
   },
   BACK_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? Math.pow(2.0D * factor, 2.0D) * (7.189819D * factor - 2.5949095D) / 2.0D : (Math.pow(2.0D * factor - 2.0D, 2.0D) * (3.5949095D * (factor * 2.0D - 2.0D) + 2.5949095D) + 2.0D) / 2.0D;
      }
   },
   BOUNCE_IN {
      public double ease(double factor) {
         return 1.0D - Easing.bounceOut(1.0D - factor);
      }
   },
   BOUNCE_OUT {
      public double ease(double factor) {
         return Easing.bounceOut(factor);
      }
   },
   BOUNCE_IN_OUT {
      public double ease(double factor) {
         return factor < 0.5D ? (1.0D - Easing.bounceOut(1.0D - 2.0D * factor)) / 2.0D : (1.0D + Easing.bounceOut(2.0D * factor - 1.0D)) / 2.0D;
      }
   };

   public abstract double ease(double var1);

   private static double bounceOut(double in) {
      double n1 = 7.5625D;
      double d1 = 2.75D;
      if (in < 1.0D / d1) {
         return n1 * in * in;
      } else if (in < 2.0D / d1) {
         return n1 * (in -= 1.5D / d1) * in + 0.75D;
      } else {
         return in < 2.5D / d1 ? n1 * (in -= 2.25D / d1) * in + 0.9375D : n1 * (in -= 2.625D / d1) * in + 0.984375D;
      }
   }

   // $FF: synthetic method
   private static Easing[] $values() {
      return new Easing[]{LINEAR, SINE_IN, SINE_OUT, SINE_IN_OUT, CUBIC_IN, CUBIC_OUT, CUBIC_IN_OUT, QUAD_IN, QUAD_OUT, QUAD_IN_OUT, QUART_IN, QUART_OUT, QUART_IN_OUT, QUINT_IN, QUINT_OUT, QUINT_IN_OUT, CIRC_IN, CIRC_OUT, CIRC_IN_OUT, EXPO_IN, EXPO_OUT, EXPO_IN_OUT, ELASTIC_IN, ELASTIC_OUT, ELASTIC_IN_OUT, BACK_IN, BACK_OUT, BACK_IN_OUT, BOUNCE_IN, BOUNCE_OUT, BOUNCE_IN_OUT};
   }
}
