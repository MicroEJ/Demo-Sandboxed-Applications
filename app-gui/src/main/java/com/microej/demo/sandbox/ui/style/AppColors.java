/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui.style;

/**
 * Class containing the colors used to build the base stylesheet.
 */
public class AppColors {

	/** Black color for the content background. */
	public static final int CONTENT_BG_BLACK = 0x1d1d1d;
	/** Orange color for the progress line fill. */
	public static final int PROGRESS_LINE_FOREGROUND = 0xf04f25;
	/** Gray color for the progress line bg. */
	public static final int PROGRESS_LINE_BACKGROUND = 0x4e4e4e;
	/** Gray color for the secondary label fill. */
	public static final int SECONDARY_LABEL = 0xbfbfbf;

	/**
	 * Hides the constructor in order to prevent instantiating a class containing only static methods.
	 */
	private AppColors() {
		// prevent instantiation
	}
}
