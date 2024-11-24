package net.shoreline.client.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
   private static final int EXP_INT_TABLE_MAX_INDEX = 750;
   private static final int EXP_INT_TABLE_LEN = 1500;
   private static final int EXP_FRAC_TABLE_LEN = 1025;
   private static final double[] FACT = new double[]{1.0D, 1.0D, 2.0D, 6.0D, 24.0D, 120.0D, 720.0D, 5040.0D, 40320.0D, 362880.0D, 3628800.0D, 3.99168E7D, 4.790016E8D, 6.2270208E9D, 8.71782912E10D, 1.307674368E12D, 2.0922789888E13D, 3.55687428096E14D, 6.402373705728E15D, 1.21645100408832E17D};

   public static double round(double value, int places) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(places, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   private static double expint(int p, double[] result) {
      double[] xs = new double[2];
      double[] as = new double[2];
      double[] ys = new double[2];
      xs[0] = 2.718281828459045D;
      xs[1] = 1.4456468917292502E-16D;
      split(1.0D, ys);

      while(p > 0) {
         if ((p & 1) != 0) {
            quadMult(ys, xs, as);
            ys[0] = as[0];
            ys[1] = as[1];
         }

         quadMult(xs, xs, as);
         xs[0] = as[0];
         xs[1] = as[1];
         p >>= 1;
      }

      if (result != null) {
         result[0] = ys[0];
         result[1] = ys[1];
         resplit(result);
      }

      return ys[0] + ys[1];
   }

   public static double slowexp(double x, double[] result) {
      double[] xs = new double[2];
      double[] ys = new double[2];
      double[] facts = new double[2];
      double[] as = new double[2];
      split(x, xs);
      ys[0] = ys[1] = 0.0D;

      for(int i = FACT.length - 1; i >= 0; --i) {
         splitMult(xs, ys, as);
         ys[0] = as[0];
         ys[1] = as[1];
         split(FACT[i], as);
         splitReciprocal(as, facts);
         splitAdd(ys, facts, as);
         ys[0] = as[0];
         ys[1] = as[1];
      }

      if (result != null) {
         result[0] = ys[0];
         result[1] = ys[1];
      }

      return ys[0] + ys[1];
   }

   private static void quadMult(double[] a, double[] b, double[] result) {
      double[] xs = new double[2];
      double[] ys = new double[2];
      double[] zs = new double[2];
      split(a[0], xs);
      split(b[0], ys);
      splitMult(xs, ys, zs);
      result[0] = zs[0];
      result[1] = zs[1];
      split(b[1], ys);
      splitMult(xs, ys, zs);
      double tmp = result[0] + zs[0];
      result[1] -= tmp - result[0] - zs[0];
      result[0] = tmp;
      tmp = result[0] + zs[1];
      result[1] -= tmp - result[0] - zs[1];
      result[0] = tmp;
      split(a[1], xs);
      split(b[0], ys);
      splitMult(xs, ys, zs);
      tmp = result[0] + zs[0];
      result[1] -= tmp - result[0] - zs[0];
      result[0] = tmp;
      tmp = result[0] + zs[1];
      result[1] -= tmp - result[0] - zs[1];
      result[0] = tmp;
      split(a[1], xs);
      split(b[1], ys);
      splitMult(xs, ys, zs);
      tmp = result[0] + zs[0];
      result[1] -= tmp - result[0] - zs[0];
      result[0] = tmp;
      tmp = result[0] + zs[1];
      result[1] -= tmp - result[0] - zs[1];
      result[0] = tmp;
   }

   private static void splitMult(double[] a, double[] b, double[] ans) {
      ans[0] = a[0] * b[0];
      ans[1] = a[0] * b[1] + a[1] * b[0] + a[1] * b[1];
      resplit(ans);
   }

   private static void split(double d, double[] split) {
      double a;
      if (d < 8.0E298D && d > -8.0E298D) {
         a = d * 1.073741824E9D;
         split[0] = d + a - a;
         split[1] = d - split[0];
      } else {
         a = d * 9.313225746154785E-10D;
         split[0] = (d + a - d) * 1.073741824E9D;
         split[1] = d - split[0];
      }

   }

   private static void resplit(double[] a) {
      double c = a[0] + a[1];
      double d = -(c - a[0] - a[1]);
      double z;
      if (c < 8.0E298D && c > -8.0E298D) {
         z = c * 1.073741824E9D;
         a[0] = c + z - z;
         a[1] = c - a[0] + d;
      } else {
         z = c * 9.313225746154785E-10D;
         a[0] = (c + z - c) * 1.073741824E9D;
         a[1] = c - a[0] + d;
      }

   }

   private static void splitAdd(double[] a, double[] b, double[] ans) {
      ans[0] = a[0] + b[0];
      ans[1] = a[1] + b[1];
      resplit(ans);
   }

   private static void splitReciprocal(double[] in, double[] result) {
      double b = 2.384185791015625E-7D;
      double a = 0.9999997615814209D;
      if (in[0] == 0.0D) {
         in[0] = in[1];
         in[1] = 0.0D;
      }

      result[0] = 0.9999997615814209D / in[0];
      result[1] = (2.384185791015625E-7D * in[0] - 0.9999997615814209D * in[1]) / (in[0] * in[0] + in[0] * in[1]);
      if (result[1] != result[1]) {
         result[1] = 0.0D;
      }

      resplit(result);

      for(int i = 0; i < 2; ++i) {
         double err = 1.0D - result[0] * in[0] - result[0] * in[1] - result[1] * in[0] - result[1] * in[1];
         err *= result[0] + result[1];
         result[1] += err;
      }

   }

   private static double exp(double x) {
      int intVal = (int)x;
      if (x < 0.0D) {
         if (x < -746.0D) {
            return 0.0D;
         }

         if (intVal < -709) {
            return exp(x + 40.19140625D) / 2.85040095144011776E17D;
         }

         if (intVal == -709) {
            return exp(x + 1.494140625D) / 4.455505956692757D;
         }

         --intVal;
      } else if (intVal > 709) {
         return Double.POSITIVE_INFINITY;
      }

      double intPartA = MathUtil.ExpIntTable.EXP_INT_TABLE_A[750 + intVal];
      double intPartB = MathUtil.ExpIntTable.EXP_INT_TABLE_B[750 + intVal];
      int intFrac = (int)((x - (double)intVal) * 1024.0D);
      double fracPartA = MathUtil.ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
      double fracPartB = MathUtil.ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
      double epsilon = x - ((double)intVal + (double)intFrac / 1024.0D);
      double z = 0.04168701738764507D;
      z = z * epsilon + 0.1666666505023083D;
      z = z * epsilon + 0.5000000000042687D;
      z = z * epsilon + 1.0D;
      z = z * epsilon + -3.940510424527919E-20D;
      double tempA = intPartA * fracPartA;
      double tempB = intPartA * fracPartB + intPartB * fracPartA + intPartB * fracPartB;
      double tempC = tempB + tempA;
      return tempC == Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : tempC * z + tempB + tempA;
   }

   private static class ExpIntTable {
      private static final double[] EXP_INT_TABLE_A = new double[1500];
      private static final double[] EXP_INT_TABLE_B = new double[1500];

      static {
         double[] tmp = new double[2];
         double[] recip = new double[2];

         for(int i = 0; i < 750; ++i) {
            MathUtil.expint(i, tmp);
            EXP_INT_TABLE_A[i + 750] = tmp[0];
            EXP_INT_TABLE_B[i + 750] = tmp[1];
            if (i != 0) {
               MathUtil.splitReciprocal(tmp, recip);
               EXP_INT_TABLE_A[750 - i] = recip[0];
               EXP_INT_TABLE_B[750 - i] = recip[1];
            }
         }

      }
   }

   private static class ExpFracTable {
      private static final double[] EXP_FRAC_TABLE_A = new double[1025];
      private static final double[] EXP_FRAC_TABLE_B = new double[1025];

      static {
         double[] tmp = new double[2];
         double factor = 9.765625E-4D;

         for(int i = 0; i < EXP_FRAC_TABLE_A.length; ++i) {
            MathUtil.slowexp((double)i * 9.765625E-4D, tmp);
            EXP_FRAC_TABLE_A[i] = tmp[0];
            EXP_FRAC_TABLE_B[i] = tmp[1];
         }

      }
   }
}
