/*
 * Java
 *
 * Copyright 2015-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui.widget;

/**
 * Arc parameter of the gauge.
 */
public class ArcParameter {
	private final int radius;
	private final int xOffset;
	private final int yOffset;

	/**
	 * Creates an ArcParameter.
	 *
	 * @param radius
	 *            the radius of the circle.
	 * @param xOffset
	 *            the X offset of the circle.
	 * @param yOffset
	 *            the Y offset of the circle.
	 */
	public ArcParameter(int radius, int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.radius = radius;
	}

	/**
	 * Gets the radius of the arc.
	 *
	 * @return the radius.
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * Gets the diameter of the arc.
	 *
	 * @return the diameter.
	 */
	public int getDiameter() {
		return this.radius * 2;
	}

	/**
	 * Gets the X offset of the arc.
	 *
	 * @return top left X offset of the circle.
	 */
	public int getXOffset() {
		return this.xOffset;
	}

	/**
	 * Gets the Y offset of the arc.
	 *
	 * @return top left Y offset of the circle.
	 */
	public int getYOffset() {
		return this.yOffset;
	}
}
