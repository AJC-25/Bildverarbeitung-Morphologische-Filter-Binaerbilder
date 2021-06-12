// BV Ue3 SS2021 Vorgabe
//
// Copyright (C) 2021 by Klaus Jung
// All rights reserved.
// Date: 2021-03-24

package bv_ss21;

import java.io.File;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class RasterImage {

	private static final int gray  = 0xffa0a0a0;

	public int[] argb;	// pixels represented as ARGB values in scanline order
	public int width;	// image width in pixels
	public int height;	// image height in pixels

	public RasterImage(int width, int height) {
		// creates an empty RasterImage of given size
		this.width = width;
		this.height = height;
		argb = new int[width * height];
		Arrays.fill(argb, gray);
	}

	public RasterImage(File file) {
		// creates an RasterImage by reading the given file
		Image image = null;
		if(file != null && file.exists()) {
			image = new Image(file.toURI().toString());
		}
		if(image != null && image.getPixelReader() != null) {
			width = (int)image.getWidth();
			height = (int)image.getHeight();
			argb = new int[width * height];
			image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
		} else {
			// file reading failed: create an empty RasterImage
			this.width = 256;
			this.height = 256;
			argb = new int[width * height];
			Arrays.fill(argb, gray);
		}
	}

	public RasterImage(ImageView imageView) {
		// creates a RasterImage from that what is shown in the given ImageView
		Image image = imageView.getImage();
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		argb = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
	}

	public void setToView(ImageView imageView) {
		// sets the current argb pixels to be shown in the given ImageView
		if(argb != null) {
			WritableImage wr = new WritableImage(width, height);
			PixelWriter pw = wr.getPixelWriter();
			pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
			imageView.setImage(wr);
		}
	}


	// image point operations to be added here

	public void binarize(int threshold) {
		// TODO: binarize the image with given threshold
		for(int y_src = 0; y_src < height; y_src++) {
			for(int x_src = 0; x_src < width; x_src++) {
				int pos_src = y_src * width + x_src;
				int rgb = argb[pos_src];

				int r = (rgb >> 16 ) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb) & 0xff;

				int avr = (r+g+b)/3;
				int binär;
				if(avr <= threshold) {
					binär = 0;
				} else { binär = 255; }
				argb[pos_src] = (0xff << 24) | (binär << 16) | (binär << 8) | (binär);
			}
		}
	}

	public void invert() {
		// TODO: invert the image (assuming an binary image)
		for(int i = 0; i<argb.length; i++) {

			//int pos_src = y_src * width + x_src;
			//int rgb = argb[pos_src];


			if(argb[i] == 0xFFFFFFFF) {
				argb[i] = 0xFF000000;
			} else if(argb[i] == 0xFF000000){
				argb[i] = 0xFFFFFFFF;
			}
		}
	}	
}
