// BV Ue3 SS2021 Vorgabe
//
// Copyright (C) 2021 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bv_ss21;

public class MorphologicFilter {

	public enum FilterType { 
		DILATION("Dilation"),
		EROSION("Erosion");

		private final String name;       
		private FilterType(String s) { name = s; }
		public String toString() { return this.name; }
	};

	// filter implementations go here:

	public void copy(RasterImage src, RasterImage dst) {
		// TODO: just copy the image
		for(int y_dest = 0; y_dest < src.height; y_dest++) {
			for(int x_dest = 0; x_dest < src.width; x_dest++) {
				int pos_dest = y_dest * src.width + x_dest;
				dst.argb[pos_dest] = src.argb[pos_dest];
			}
		}
	}

	public void dilation(RasterImage src, RasterImage dst, double radius) {
		// TODO: dilate the image using a structure element that is a neighborhood with the given radius
		copy(src, dst);

		for(int y_src = 0; y_src < src.height; y_src++) {
			for(int x_src = 0; x_src < src.width; x_src++) {
				int pos_src = y_src * src.width + x_src;
				int rad = (int) Math.round(radius);

				//Kernel H mit schwarzem Pixel als Hotspot 
				if(src.argb[pos_src] == 0xFF000000) {
					int Hx_0 = x_src - rad;
					int Hx_1 = x_src + rad;
					int Hy_0 = y_src - rad;
					int Hy_1 = y_src + rad;

					//eingrenzen / Randbehandlung:
					if(Hx_0 < 0) {
						Hx_0 = 0;
					}
					if(Hx_1 > src.width-1) {
						Hx_1 = src.width -1;
					}
					if(Hy_0 < 0) {
						Hy_0 = 0;
					}
					if(Hy_1 > src.height-1) {
						Hy_1 = src.height -1;
					}

					for(int y_dest = Hy_0; y_dest <= Hy_1; y_dest++) {
						for(int x_dest = Hx_0; x_dest <= Hx_1; x_dest++) {
							//Abstand mit Satz des Pythagoras rechnen: (Nachbarschaft)
							double dist = Math.sqrt((x_dest - x_src)*(x_dest - x_src) + (y_dest - y_src)*(y_dest - y_src));

							if(dist <= rad) {
								int pos_dest = y_dest * dst.width + x_dest;
								dst.argb[pos_dest] = src.argb[pos_src];
							}
						}
					}
				}				
			}
		}
	}

	public void erosion(RasterImage src, RasterImage dst, double radius) {
		// TODO: erode the image using a structure element that is a neighborhood with the given radius
		copy(src, dst);

		src.invert();
		dilation(src, dst, radius);
		src.invert();
		dst.invert();
	}
}
