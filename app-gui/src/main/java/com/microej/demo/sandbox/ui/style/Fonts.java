/*
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui.style;

import ej.microui.display.Font;

/**
 * Fonts used by the application.
 */
public class Fonts {

	private static final String SOURCE_12_400 = "/fonts/SourceSansPro_12px-400.ejf"; //$NON-NLS-1$
	private static final String SOURCE_82_700 = "/fonts/SourceSansPro_82px-700.ejf"; //$NON-NLS-1$

	/**
	 * Hides the constructor in order to prevent instantiating a class containing only static methods.
	 */
	private Fonts() {
		// Prevent instantiation.
	}

	/**
	 * Gets the Source Sans Pro font with a height of 12px and a weight of 400 (Regular).
	 * <p>
	 * Height (Cap to descender): 12<br>
	 * Font-weight: 400<br>
	 * Height: 19<br>
	 * Baseline: 13<br>
	 * Space size: 4<br>
	 *
	 * @return the font with the settings given above.
	 *
	 */
	public static Font getSourceSansPro12px400() {
		return Font.getFont(SOURCE_12_400);
	}

	/**
	 * Gets the Source Sans Pro font with a height of 82px and a weight of 700 (Bold).
	 * <p>
	 * Height (Cap to descender): 82<br>
	 * Font-weight: 700<br>
	 * Height: 82<br>
	 * Baseline: 66<br>
	 * Space size: 5<br>
	 *
	 * @return the font with the settings given above.
	 */
	public static Font getSourceSansPro82px700() {
		return Font.getFont(SOURCE_82_700);
	}

}
