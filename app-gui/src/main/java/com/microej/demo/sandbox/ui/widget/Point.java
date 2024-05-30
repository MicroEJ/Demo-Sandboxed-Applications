/*
 * Java
 *
 * Copyright 2015-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui.widget;

/**
 * Represents a point (x, y).
 * <p>
 * For heap optimization the size is stored as a <code>short</code> and therefore is limited between <code>-32768</code>
 * and <code>32767</code>.
 */
public class Point {
	short x;
	short y;

	/**
	 * Creates coordinate specifying it's values.
	 *
	 * @param x
	 *            the x position of the point.
	 * @param y
	 *            the y position of the point.
	 */
	public Point(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
	}

	/**
	 * Gets the x position of the point.
	 *
	 * @return the x position.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Sets the x position of the point.
	 *
	 * @param x
	 *            the x position to set.
	 */
	public void setX(int x) {
		this.x = (short) x;
	}

	/**
	 * Gets the y position of the point.
	 *
	 * @return the y position.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Sets the y position of the point.
	 *
	 * @param y
	 *            the y position to set.
	 */
	public void setY(int y) {
		this.y = (short) y;
	}

	/**
	 * Sets both the x and y position of the point.
	 *
	 * @param x
	 *            the x position to set.
	 * @param y
	 *            the y position to set.
	 */
	public void setPosition(int x, int y) {
		this.x = (short) x;
		this.y = (short) y;
	}
}