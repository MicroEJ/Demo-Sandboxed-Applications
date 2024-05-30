/*
 * Java
 *
 * Copyright 2015-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui.widget;

import ej.bon.XMath;
import ej.drawing.ShapePainter;
import ej.drawing.ShapePainter.Cap;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.mwt.util.Alignment;
import ej.widget.util.render.StringPainter;

/**
 * Helper class containing several static methods for calculations and drawing of the gauge.
 */
public class GaugeHelper {
	/** Full circle value. */
	public static final int FULL_CIRCLE = 360;
	/** One hundred percent value. */
	public static final int HUNDRED_PERCENT = 100;

	/**
	 * Hides the constructor in order to prevent instantiating a class containing only static methods.
	 */
	private GaugeHelper() {
		// Prevent instantiation.
	}

	/**
	 * Calculates the value for a given percentage.
	 *
	 * @param percent
	 *            the percent to calculate the value for.
	 * @param min
	 *            the minimum value.
	 * @param max
	 *            the maximum value.
	 * @param inverted
	 *            if the calculation should be inverted.
	 *
	 * @return the resulting value for the given percentage.
	 */
	public static float computeValueFromPercent(float percent, int min, int max, boolean inverted) {
		return computeValueFromPercent(percent, inverted ? max : min, inverted ? min : max);
	}

	/**
	 * Calculates the value for a given percentage.
	 *
	 * @param percent
	 *            the percent to calculate the value for.
	 * @param min
	 *            the minimum value.
	 * @param max
	 *            the maximum value.
	 *
	 * @return the resulting value for the given percentage.
	 */
	public static float computeValueFromPercent(float percent, int min, int max) {
		return (percent * (max - min) / HUNDRED_PERCENT) + min;
	}

	/**
	 * Calculates the percentage from the given value.
	 *
	 * @param value
	 *            the value for which to calculate the percentage.
	 * @param min
	 *            the minimum value.
	 * @param max
	 *            the maximum value.
	 *
	 * @return the resulting percentage.
	 */
	public static float computePercentFromValue(float value, int min, int max) {
		return ((value - min) * HUNDRED_PERCENT) / (max - min);
	}

	/**
	 * Calculates the point on a circle with the offset specified in {@link ArcParameter}.
	 *
	 * @param angle
	 *            the angle for which to calculate the point.
	 * @param param
	 *            the {@link ArcParameter} containing radius and offset of the circle.
	 *
	 * @return the {@link Point} coordinate.
	 */
	public static Point computeOffsetPointOnCircle(float angle, ArcParameter param) {
		float fixedAngle = FULL_CIRCLE - angle; // Angle rotation in painter is counterclockwise.
		int radius = param.getRadius();
		double pX = radius * XMath.cos(XMath.toRadians(fixedAngle));
		double pY = radius * XMath.sin(XMath.toRadians(fixedAngle));
		return new Point((int) Math.round(pX + radius + param.getXOffset()),
				(int) Math.round(pY + radius + param.getYOffset()));
	}

	/**
	 * Calculates the angle from a given value and the given angle / value bounds.
	 * <p>
	 * The value is between the minimum and maximum value bound and the resulting angle will be between the minimum
	 * value
	 *
	 * @param value
	 *            the value to calculate the angle for.
	 * @param minVal
	 *            the minimum value bound.
	 * @param maxVal
	 *            the maximum value bound.
	 * @param startAngle
	 *            the starting angle.
	 * @param endAngle
	 *            the ending angle.
	 * @return the angle between start and end angle that represents the value between min value and max value.
	 */
	public static float computeAngleFromValue(float value, float minVal, float maxVal, float startAngle,
			float endAngle) {
		if (startAngle < endAngle) {
			throw new IllegalArgumentException("Start angle not smaller than end angle."); //$NON-NLS-1$
		}
		if (value < minVal || value > maxVal) {
			throw new IllegalArgumentException(
					"Value out of bounds. Value: " + value + " min: " + minVal + " max: " + maxVal); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		float percent = (value - minVal) / (maxVal - minVal);
		return startAngle - ((startAngle - endAngle) * percent);
	}

	/**
	 * Draws a label on a point on the circle.
	 *
	 * @param g
	 *            the {@link GraphicsContext} to draw on.
	 * @param arc
	 *            the {@link ArcParameter} containing the radius and offset of the circle.
	 * @param pointerAngle
	 *            the angle at which to draw the text.
	 * @param label
	 *            the text to draw.
	 * @param font
	 *            the font to draw with.
	 */
	public static void drawLabelOnArc(GraphicsContext g, ArcParameter arc, float pointerAngle, String label,
			Font font) {
		Point aPointer = GaugeHelper.computeOffsetPointOnCircle(pointerAngle, arc);
		StringPainter.drawStringAtPoint(g, label, font, aPointer.getX(), aPointer.getY(), Alignment.HCENTER,
				Alignment.VCENTER);
	}

	/**
	 * Draws a line at a specific angle between two circles.
	 *
	 * @param g
	 *            the {@link GraphicsContext} to draw on.
	 * @param outer
	 *            the {@link ArcParameter} of the outer circle.
	 * @param inner
	 *            the {@link ArcParameter} of the inner circle.
	 * @param pointerAngle
	 *            the angle at which to draw the line.
	 * @param thickness
	 *            the thickness of the line.
	 * @param fade
	 *            the fade of the line.
	 */
	public static void drawLineBetweenArcs(GraphicsContext g, ArcParameter outer, ArcParameter inner,
			float pointerAngle, int thickness, int fade) {
		Point aPointer = GaugeHelper.computeOffsetPointOnCircle(pointerAngle, outer);
		Point cPointer = GaugeHelper.computeOffsetPointOnCircle(pointerAngle, inner);
		ShapePainter.drawThickFadedLine(g, aPointer.getX(), aPointer.getY(), cPointer.getX(), cPointer.getY(),
				thickness, fade, Cap.ROUNDED, Cap.ROUNDED);
	}
}
