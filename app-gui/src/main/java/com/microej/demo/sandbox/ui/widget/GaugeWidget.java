/*
 * Java
 *
 * Copyright 2015-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui.widget;

import ej.annotation.Nullable;
import ej.basictool.ThreadUtils;
import ej.bon.XMath;
import ej.drawing.ShapePainter;
import ej.drawing.ShapePainter.Cap;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Painter;
import ej.microui.display.ResourceImage;
import ej.motion.Motion;
import ej.motion.quad.QuadEaseInOutFunction;
import ej.mwt.Widget;
import ej.mwt.style.Style;
import ej.mwt.util.Alignment;
import ej.mwt.util.Size;
import ej.widget.util.motion.MotionAnimation;
import ej.widget.util.motion.MotionAnimationListener;
import ej.widget.util.render.StringPainter;

/**
 * Dynamic Gauge Widget.
 */
public class GaugeWidget extends Widget implements MotionAnimationListener {

	/** Style ID for the progress highlight color. */
	public static final int STYLE_COLOR_PROGRESS = 0;
	/** Style ID for the progress background color. */
	public static final int STYLE_COLOR_PROGRESS_BG = 1;
	/** Style ID for the color of the unit label. */
	public static final int STYLE_COLOR_UNIT_LABEL = 2;
	/** Style ID for the color of the unit icon. */
	public static final int STYLE_COLOR_UNIT_ICON = 3;
	/** Style ID for the color of the arc labels. */
	public static final int STYLE_COLOR_ARC_LABELS = 4;

	/** Style ID for the font of the unit label. */
	public static final int STYLE_FONT_UNIT_LABEL = 5;
	/** Style ID for the font of the arc labels. */
	public static final int STYLE_FONT_ARC_LABELS = 6;

	// Default style values
	private static final int DEFAULT_PROGRESS_COLOR = 0xE91C73;
	private static final int DEFAULT_PROGRESS_BG_COLOR = 0x382349;

	// Animation
	private static final int ANIMATION_DURATION = 1000;
	private static final int ANIMATION_FACTOR = 10;

	// Icon & Unit
	/**
	 * Offset of the icon from bottom of the gauge.
	 * <p>
	 * The higher the offset is the closer it is to the centre.<br>
	 * This offset takes the radius of the outer gauge and divides it. That means, the distance to the centre relative
	 * to the widegts size is preserved.
	 */
	private static final float UNIT_ICON_OFFSET_DIVIDER = 2.5f;
	/**
	 * Offset of the unit text from bottom of the gauge.
	 * <p>
	 * The higher the offset is the closer it is to the centre.<br>
	 * This offset takes the radius of the outer gauge and divides it. That means, the distance to the centre relative
	 * to the widegts size is preserved.
	 */
	private static final float UNIT_TEXT_OFFSET_DIVIDER = 5f;

	// Line drawing related
	/**
	 * Divider used to calculate the offset centre.
	 * <p>
	 * Since the circle of the gauge is open at the bottom we have more space to draw at the bottom and don't want to
	 * exactly centre it.<br>
	 * Normally a divider of 2 is used to get the centre, but the smaller divider means we position the centre of the
	 * gauge further to the bottom.
	 */
	private static final float OFFSET_CENTRE_DIVIDER = 1.72f;

	/** Fade size used for the circle arcs. */
	private static final int FADE = 1;
	/** Fade size for short lines. */
	private static final int FADE_SHORT_LINE = 1;
	/** Fade size for long lines. */
	private static final int FADE_LONG_LINE = 1;

	private static final int OUTER_ARC_THICKNESS = 4;
	private static final int INNER_ARC_THICKNESS = 2;
	private static final int LINE_THICKNESS = 1;

	/** Distance between outer circle inside and inner circle outside in pixel. */
	private static final int INNER_OUTER_ARC_DISTANCE = 10;
	private static final int BIG_LINE_EVERY_X_PERCENT = 10;
	private static final int SMALL_LINE_EVERY_X_PERCENT = 2;
	private static final int LINE_SHORT_LENGTH = 6;
	private static final int LINE_LONG_LENGTH = 12;
	/** Distance between the end of a long line and the centre of the associated label. */
	private static final int LINE_LABEL_DISTANCE = 18;

	private static final Cap CAP = Cap.ROUNDED;

	/** Reduces the top of the background drawn for the main label to adjust for fonts with empty space. */
	private static final int MAIN_LABEL_BG_TOP_OFFSET = 10;
	/** Reduces the bottom of the background drawn for the main label to adjust for fonts with empty space. */
	private static final int MAIN_LABEL_BG_BOTTOM_OFFSET = 10;

	// Offsets
	/*
	 * The position of thick faded circle arcs is always calculated from the centre of the circle line so we need to
	 * calculate the offset depending on the thickness and fade.
	 *
	 * FADE | THICKNESS / 2 | actual position we want | THICKNESS / 2 | FADE
	 */
	private static final int OUTER_ARC_OFFSET = OUTER_ARC_THICKNESS / 2 + FADE;
	private static final int INNER_OUTER_ARC_OFFSET = INNER_OUTER_ARC_DISTANCE + OUTER_ARC_OFFSET + FADE * 2;
	private static final int INNER_ARC_OFFSET = INNER_ARC_THICKNESS / 2 + FADE + INNER_OUTER_ARC_OFFSET;

	private static final int LINE_SHORT_ARC_OFFSET = LINE_SHORT_LENGTH + INNER_ARC_OFFSET + FADE * 2;
	private static final int LINE_LONG_ARC_OFFSET = LINE_LONG_LENGTH + INNER_ARC_OFFSET + FADE * 2;
	private static final int LINE_LABEL_ARC_OFFSET = LINE_LONG_ARC_OFFSET + LINE_LABEL_DISTANCE;

	// Arc Angles
	private static final int START_ANGLE = 220;
	private static final int END_ANGLE = -40;
	private static final int ARC_ANGLE = END_ANGLE - START_ANGLE;

	private final String unit;
	private final String unitIconPath;

	private int value;
	private int animValue;
	private int minValue;
	private int maxValue;
	private final boolean inverted;

	@Nullable
	private MotionAnimation motionAnimation;
	@Nullable
	private ResourceImage unitIcon;

	@Nullable
	private DrawParameters drawParam;

	/**
	 * Creates the GaugeWidget without an specified direction.
	 *
	 * @param minValue
	 *            Minimum possible value for the progress line.
	 * @param maxValue
	 *            Maximum possible value for the progress line.
	 * @param unit
	 *            Unit for the value.
	 */
	public GaugeWidget(int minValue, int maxValue, String unit) {
		this(minValue, maxValue, unit, "", false); //$NON-NLS-1$
	}

	/**
	 * Creates the GaugeWidget without an specified direction.
	 *
	 * @param minValue
	 *            Minimum possible value for the progress line.
	 * @param maxValue
	 *            Maximum possible value for the progress line.
	 * @param unit
	 *            Unit for the value.
	 * @param unitIconPath
	 *            path to the unit icon to draw.
	 */
	public GaugeWidget(int minValue, int maxValue, String unit, String unitIconPath) {
		this(minValue, maxValue, unit, unitIconPath, false);
	}

	/**
	 * Creates the GaugeWidget with an specified direction.
	 *
	 * @param minValue
	 *            Minimum possible value for the progress line.
	 * @param maxValue
	 *            Maximum possible value for the progress line.
	 * @param unit
	 *            Unit for the value.
	 * @param unitIconPath
	 *            path to the unit icon to draw.
	 * @param invertDirection
	 *            If true, the progress animation direction is right to left instead of left to right.
	 */
	public GaugeWidget(int minValue, int maxValue, String unit, String unitIconPath, boolean invertDirection) {
		this.minValue = minValue;
		this.maxValue = maxValue;

		this.unit = unit;
		this.unitIconPath = unitIconPath;

		this.inverted = invertDirection;
	}

	/**
	 * Sets the minimum and maximum value.
	 *
	 * @param minValue
	 *            the minimum value to set.
	 * @param maxValue
	 *            the maximum value to set.
	 */
	public void setMinMax(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.drawParam = null; // Reset drawParam to re-render arc labels.
	}

	/**
	 * Sets the current progress in percent without animation.
	 *
	 * @param percent
	 *            the percentage to set
	 */
	public void setPercent(float percent) {
		setPercent(percent, false);
	}

	/**
	 * Sets the current progress in percent.
	 *
	 * @param percent
	 *            the value to set
	 * @param animate
	 *            to animate the progress to the given percent value
	 */
	public void setPercent(float percent, boolean animate) {
		setValue(Math.round(GaugeHelper.computeValueFromPercent(percent, this.minValue, this.maxValue)), animate);
	}

	/**
	 * Sets the current progress value.
	 *
	 * @param newValue
	 *            the value to set
	 */
	public void setValue(int newValue) {
		setValue(newValue, false);
	}

	/**
	 * Sets the current progress value with animation.
	 *
	 * @param newValue
	 *            the value to set
	 *
	 * @param animate
	 *            to animate the progress to the given percent value
	 */
	public void setValue(int newValue, boolean animate) {
		if (this.value == newValue) {
			return;
		}

		if (animate) {
			int oldValue = this.value;
			if (this.motionAnimation != null) {
				oldValue = this.animValue;
			}
			startAnimation(oldValue, newValue);
		}
		this.value = newValue;
	}

	private void startAnimation(int oldValue, int newValue) {
		stopAnimation();

		Motion motion = new Motion(QuadEaseInOutFunction.INSTANCE, oldValue * ANIMATION_FACTOR,
				newValue * ANIMATION_FACTOR, ANIMATION_DURATION);
		this.motionAnimation = new MotionAnimation(getDesktop().getAnimator(), motion, this);
		this.motionAnimation.start();
	}

	private void stopAnimation() {
		if (this.motionAnimation != null) {
			this.motionAnimation.stop();
			this.motionAnimation = null;
		}
	}

	@Override
	public void tick(int value, boolean finished) {
		this.animValue = value;
		requestRender();
		if (finished) {
			this.motionAnimation = null;
		}
	}

	@Override
	protected void onShown() {
		if (!this.unitIconPath.isEmpty()) {
			this.unitIcon = ResourceImage.loadImage(this.unitIconPath);
		}
	}

	@Override
	protected void onHidden() {
		stopAnimation(); // Cleanly close animation if running when widget is hidden.

		ResourceImage icon = this.unitIcon;
		if (icon != null) {
			icon.close();
			this.unitIcon = null;
		}
	}

	@Override
	protected void computeContentOptimalSize(Size size) {
		int minWidth = 0;
		int minHeight = 0;

		Style style = getStyle();

		Font mainFont = style.getFont();
		Font arcFont = style.getExtraObject(STYLE_FONT_ARC_LABELS, Font.class, mainFont);
		Font unitFont = style.getExtraObject(STYLE_FONT_UNIT_LABEL, Font.class, mainFont);

		String maxValueString = Integer.toString(this.maxValue);

		// The outer arc size (including fade on the inner and outer side of the arc line)
		minWidth += OUTER_ARC_THICKNESS + FADE * 2;
		minWidth += INNER_OUTER_ARC_DISTANCE;
		// The inner arc size (including fade on the inner and outer side of the arc line)
		minWidth += INNER_ARC_THICKNESS + FADE * 2;
		// The longer lines length and only one fade, since the other fade overlaps with the inner arc.
		minWidth += LINE_LONG_LENGTH + FADE_LONG_LINE;
		// The distance between the long lines and and the centre of the label + half the size of the label.
		minWidth += LINE_LABEL_DISTANCE + (arcFont.stringWidth(maxValueString) / 2);
		// Mirror the same on the other side.
		minWidth = minWidth * 2;
		// Add the space for the centre value or unit label, depending on which is longer.
		minWidth += XMath.max(mainFont.stringWidth(maxValueString), unitFont.stringWidth(this.unit));
		size.setWidth(minWidth);

		// The outer arc size (including fade on the inner and outer side of the arc line)
		minHeight += OUTER_ARC_THICKNESS + FADE * 2;
		minHeight += INNER_OUTER_ARC_DISTANCE;
		// The inner arc size (including fade on the inner and outer side of the arc line)
		minHeight += INNER_ARC_THICKNESS + FADE * 2;
		// The longer lines length and only one fade, since the other fade overlaps with the inner arc.
		minHeight += LINE_LONG_LENGTH + FADE_LONG_LINE;
		// The distance between the long lines and and the centre of the label + half the size of the label.
		minHeight += LINE_LABEL_DISTANCE + (arcFont.getHeight() / 2);
		// Add the value and unit label in the centre.
		minHeight += mainFont.getHeight();
		minHeight += unitFont.getHeight();
		// If we have an unit icon, take it's height into account.
		ResourceImage icon = this.unitIcon;
		if (icon != null) {
			minHeight += icon.getHeight();
		}
		size.setHeight(minHeight);

		// Reset draw parameter after changing size.
		this.drawParam = null;
	}

	@Override
	public void render(GraphicsContext g) {
		Style style = getStyle();
		assert style != null;
		Size contentSize = new Size(getWidth(), getHeight());
		style.getMargin().apply(g, contentSize);
		// Only apply the background the first time so we do not overwrite the drawn gauge label.
		// This will have a better drawing performance for animations, since the labels will not have to be re-drawn
		// every time.
		if (this.drawParam == null) {
			style.getBackground().apply(g, contentSize.getWidth(), contentSize.getHeight());
		}
		style.getBorder().apply(g, contentSize);
		style.getPadding().apply(g, contentSize);
		try {
			renderContent(g, contentSize.getWidth(), contentSize.getHeight());
		} catch (Exception e) {
			ThreadUtils.handleUncaughtException(e);
		}
	}

	@Override
	protected void renderContent(GraphicsContext g, int contentWidth, int contentHeight) {
		// Load styles
		Style style = getStyle();

		int valueColor = style.getColor();

		int progressColor = style.getExtraInt(STYLE_COLOR_PROGRESS, DEFAULT_PROGRESS_COLOR);
		int progressBgColor = style.getExtraInt(STYLE_COLOR_PROGRESS_BG, DEFAULT_PROGRESS_BG_COLOR);

		int unitLabelColor = style.getExtraInt(STYLE_COLOR_UNIT_LABEL, valueColor);
		int unitIconColor = style.getExtraInt(STYLE_COLOR_UNIT_LABEL, progressColor);

		int arcLabelColor = style.getExtraInt(STYLE_COLOR_ARC_LABELS, progressBgColor);

		Font mainFont = style.getFont();
		Font arcFont = style.getExtraObject(STYLE_FONT_ARC_LABELS, Font.class, mainFont);
		Font unitFont = style.getExtraObject(STYLE_FONT_UNIT_LABEL, Font.class, mainFont);

		DrawParameters param = this.drawParam;
		if (param == null) {
			// Calculate required positions
			int offsetHeight = (int) (contentHeight / OFFSET_CENTRE_DIVIDER);
			int radius = offsetHeight;
			int yDiff = offsetHeight - contentHeight / 2;
			int diameter = radius * 2;
			if (contentWidth < contentHeight || contentWidth < diameter) {
				offsetHeight = (int) (contentWidth / OFFSET_CENTRE_DIVIDER);
				radius = contentWidth / 2;
				diameter = radius * 2;
				yDiff = offsetHeight - contentWidth / 2;
			}

			int xPos = Alignment.computeLeftX(diameter, 0, contentWidth, style.getHorizontalAlignment());
			int yPos = Alignment.computeTopY(XMath.min(diameter - yDiff, contentHeight), 0, contentHeight,
					style.getVerticalAlignment());

			int xCenterAligned = xPos + radius;
			int yCenterAligned = yPos + radius;

			ArcParameter outer = new ArcParameter(radius - OUTER_ARC_OFFSET, xPos + OUTER_ARC_OFFSET,
					yPos + OUTER_ARC_OFFSET);

			ArcParameter inner = new ArcParameter(radius - INNER_ARC_OFFSET, xPos + INNER_ARC_OFFSET,
					yPos + INNER_ARC_OFFSET);

			ArcParameter lineStart = new ArcParameter(inner.getRadius() - FADE, inner.getXOffset() + FADE,
					inner.getYOffset() + FADE);
			ArcParameter lineShort = new ArcParameter(radius - LINE_SHORT_ARC_OFFSET, xPos + LINE_SHORT_ARC_OFFSET,
					yPos + LINE_SHORT_ARC_OFFSET);
			ArcParameter lineLong = new ArcParameter(radius - LINE_LONG_ARC_OFFSET, xPos + LINE_LONG_ARC_OFFSET,
					yPos + LINE_LONG_ARC_OFFSET);
			ArcParameter lineLabel = new ArcParameter(radius - LINE_LABEL_ARC_OFFSET, xPos + LINE_LABEL_ARC_OFFSET,
					yPos + LINE_LABEL_ARC_OFFSET);

			param = new DrawParameters(xCenterAligned, yCenterAligned, outer, inner, lineStart, lineShort, lineLong,
					lineLabel);
			this.drawParam = param;

			// Draw inner Arc, lines on inner Arc and labels for long line.
			renderInnerArc(g, param, arcFont, progressBgColor, arcLabelColor);

			// Draw unit label & icon
			renderUnit(g, param, unitFont, unitLabelColor, unitIconColor);
		}

		// Draw outer arc background
		ArcParameter outer = param.outer;

		g.setColor(progressBgColor);
		ShapePainter.drawThickFadedCircleArc(g, outer.getXOffset(), outer.getYOffset(), outer.getDiameter(),
				START_ANGLE, ARC_ANGLE, OUTER_ARC_THICKNESS, FADE, CAP, CAP);

		// Draw value
		int drawValue = this.value;
		if (this.motionAnimation != null) {
			drawValue = this.animValue / ANIMATION_FACTOR;
		}

		int baselineOffset = mainFont.getHeight() - mainFont.getBaselinePosition();
		// Since we do not draw the full background every time, we need to at least redraw the area where the value is.
		drawBackgroundForValue(g, param, mainFont, baselineOffset);

		g.setColor(unitLabelColor);
		StringPainter.drawStringAtPoint(g, Integer.toString(drawValue), mainFont, param.xCenterAligned,
				param.yCenterAligned + baselineOffset, Alignment.HCENTER, Alignment.BOTTOM);

		// Draw outer progress arc
		float angle = GaugeHelper.computeAngleFromValue(drawValue, this.minValue, this.maxValue, 0, ARC_ANGLE);
		int start = START_ANGLE;
		if (this.inverted) {
			angle = -angle;
			start = END_ANGLE;
		}

		g.setColor(progressColor);

		int backgroundColor = g.getBackgroundColor();
		g.removeBackgroundColor(); // Background needs to be removed since lines intersect with inner arc
		ShapePainter.drawThickFadedCircleArc(g, outer.getXOffset(), outer.getYOffset(), outer.getDiameter(), start,
				angle, OUTER_ARC_THICKNESS, FADE, CAP, CAP);
		g.setBackgroundColor(backgroundColor);
	}

	private void renderInnerArc(GraphicsContext g, DrawParameters param, Font arcFont, int progressBgColor,
			int arcLabelColor) {

		// Draw lines and labels on inner arc
		g.setColor(progressBgColor);
		for (int currentPercent = 0; currentPercent <= GaugeHelper.HUNDRED_PERCENT; currentPercent++) {
			if (currentPercent % BIG_LINE_EVERY_X_PERCENT == 0) {
				float angle = GaugeHelper.computeValueFromPercent(currentPercent, START_ANGLE, END_ANGLE,
						this.inverted);
				GaugeHelper.drawLineBetweenArcs(g, param.lineStart, param.lineLong, angle, LINE_THICKNESS,
						FADE_LONG_LINE);

				float fCurrentValue = GaugeHelper.computeValueFromPercent(currentPercent, this.minValue, this.maxValue);
				int currentValue = Math.round(fCurrentValue);
				g.setColor(arcLabelColor);
				GaugeHelper.drawLabelOnArc(g, param.lineLabel, angle, String.valueOf(currentValue), arcFont);
				g.setColor(progressBgColor); // Reset color after label
				continue;
			}

			if (currentPercent % SMALL_LINE_EVERY_X_PERCENT == 0) {
				float angle = GaugeHelper.computeValueFromPercent(currentPercent, START_ANGLE, END_ANGLE,
						this.inverted);
				GaugeHelper.drawLineBetweenArcs(g, param.lineStart, param.lineShort, angle, LINE_THICKNESS,
						FADE_SHORT_LINE);
			}
		}

		ArcParameter inner = param.inner;
		// Draw inner arc
		int backgroundColor = g.getBackgroundColor();
		g.removeBackgroundColor(); // Background needs to be removed since lines intersect with inner arc
		ShapePainter.drawThickFadedCircleArc(g, inner.getXOffset(), inner.getYOffset(), inner.getDiameter(),
				START_ANGLE, ARC_ANGLE, INNER_ARC_THICKNESS, FADE, CAP, CAP);
		g.setBackgroundColor(backgroundColor);
	}

	private void renderUnit(GraphicsContext g, DrawParameters param, Font unitFont, int unitColor, int iconColor) {
		int xCenterAligned = param.xCenterAligned;
		int yCenterAligned = param.yCenterAligned;
		int outerRadius = param.outer.getRadius();

		g.setColor(unitColor);
		StringPainter.drawStringAtPoint(g, this.unit, unitFont, xCenterAligned,
				(int) (yCenterAligned + (outerRadius / UNIT_TEXT_OFFSET_DIVIDER)), Alignment.HCENTER,
				Alignment.VCENTER);

		ResourceImage icon = this.unitIcon;
		if (icon != null) {
			g.setColor(iconColor);
			Painter.drawImage(g, icon, xCenterAligned - (icon.getWidth() / 2),
					(int) (yCenterAligned + (outerRadius / UNIT_ICON_OFFSET_DIVIDER)));
		}
	}

	private void drawBackgroundForValue(GraphicsContext g, DrawParameters param, Font mainFont, int baselineOffset) {
		int bgWidth = mainFont.stringWidth(Integer.toString(this.maxValue));
		int bgHeight = mainFont.getHeight() - baselineOffset - MAIN_LABEL_BG_TOP_OFFSET - MAIN_LABEL_BG_BOTTOM_OFFSET;

		int xLabel = Alignment.computeLeftX(bgWidth, param.xCenterAligned, Alignment.HCENTER);
		int yLabel = Alignment.computeTopY(bgWidth, param.yCenterAligned + baselineOffset + MAIN_LABEL_BG_TOP_OFFSET,
				Alignment.VCENTER);

		g.setColor(g.getBackgroundColor());
		Painter.fillRectangle(g, xLabel, yLabel, bgWidth, bgHeight);
	}

	private static final class DrawParameters {
		int xCenterAligned;
		int yCenterAligned;
		ArcParameter outer;
		ArcParameter inner;
		ArcParameter lineStart;
		ArcParameter lineShort;
		ArcParameter lineLong;
		ArcParameter lineLabel;

		DrawParameters(int xCenterAligned, int yCenterAligned, ArcParameter outer, ArcParameter inner,
				ArcParameter lineStart, ArcParameter lineShort, ArcParameter lineLong, ArcParameter lineLabel) {
			this.xCenterAligned = xCenterAligned;
			this.yCenterAligned = yCenterAligned;
			this.outer = outer;
			this.inner = inner;
			this.lineStart = lineStart;
			this.lineShort = lineShort;
			this.lineLong = lineLong;
			this.lineLabel = lineLabel;
		}

	}
}
