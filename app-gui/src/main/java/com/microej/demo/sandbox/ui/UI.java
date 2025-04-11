/*
 * Java
 *
 * Copyright 2023-2025 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui;

import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.PowerServiceListener;
import com.microej.demo.sandbox.sharedinterface.PowerService;
import com.microej.demo.sandbox.sharedinterface.PowerServiceListenerUtil;
import com.microej.demo.sandbox.ui.style.AppColors;
import com.microej.demo.sandbox.ui.style.Fonts;
import com.microej.demo.sandbox.ui.widget.GaugeWidget;

import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.mwt.Desktop;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.dimension.OptimalDimension;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.TypeSelector;
import ej.mwt.util.Alignment;
import ej.service.ServiceFactory;
import ej.widget.container.LayoutOrientation;
import ej.widget.container.SimpleDock;

/**
 * The PowerProvider class is responsible for displaying a user interface for the power readings.
 */
public class UI implements PowerServiceListener {

	public static final int HIGH_RESOLUTION_MIN_WIDTH = 600;
	public static final int HIGH_RESOLUTION_MIN_HEIGHT = 600;

	private static final Logger LOGGER = Logger.getLogger("UI"); //$NON-NLS-1$

	private static final String UNIT_NAME = "Watt"; //$NON-NLS-1$
	private static final String UNIT_ICON_PATH = isLowResolution() ? "/images/watt_icon_ldpi.png":"/images/watt_icon_hdpi.png"; //$NON-NLS-1$
	private static final int MIN_POWER = 0;
	private static final int MAX_POWER = 1100;

	private static final int GAUGE_PADDING = 5;
	private final GaugeWidget gauge = new GaugeWidget(MIN_POWER, MAX_POWER, UNIT_NAME, UNIT_ICON_PATH, false);

	private final PowerServiceListenerUtil powerServiceListenerUtil = new PowerServiceListenerUtil();

	private boolean minMaxSet;

	/**
	 * Determines whether the current display has a low resolution or not.
	 *
	 * A display is considered to have a low resolution if its width or height is less than the
	 * specified minimum values for high-resolution displays.
	 *
	 * @return {@code true} if the display has a low resolution, {@code false} otherwise.
	 */
	public static boolean isLowResolution() {
		int displayWidth = Display.getDisplay().getWidth();
		int displayHeight = Display.getDisplay().getHeight();
		return (displayWidth < HIGH_RESOLUTION_MIN_WIDTH) || (displayHeight < HIGH_RESOLUTION_MIN_HEIGHT);
	}

	/**
	 * Shows the UI with the gauge as main widget.
	 *
	 * @see GaugeWidget
	 */
	public void show() {
		Desktop desktop = new Desktop();
		desktop.setStylesheet(createCSS());

		// Display gauge with fallback values until the real ones can be read from MeterData.
		this.minMaxSet = false;

		SimpleDock dock = new SimpleDock(LayoutOrientation.VERTICAL);
		dock.setCenterChild(this.gauge);

		desktop.setWidget(dock);
		desktop.requestShow();

		PowerService powerService = this.powerServiceListenerUtil.waitAndGetPowerService();
		powerService.addListener(this);
	}

	@Override
	public void update() {
		// Get current data from data provider
		final PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.warning("MeterData service not found."); //$NON-NLS-1$

			// PowerService was not found, wait for it and listen to it again
			new Thread(new Runnable() {				// NOSONAR due to a bug in the sonar plugin, the java sources are
													// set to java 8 and this anonymous class is considered as a code smell
				@Override
				public void run() {
					UI.this.powerServiceListenerUtil.waitAndGetPowerService().addListener(UI.this);
				}
			}).start();

			return;
		}

		MicroUI.callSerially(new Runnable() {	// NOSONAR due to a bug in the sonar plugin, the java sources are
												// set to java 8 and this anonymous class is considered as a code smell
			@Override
			public void run() {
				if (!UI.this.minMaxSet) {
					UI.this.minMaxSet = true;
					UI.this.gauge.setMinMax(powerService.getMinPower(), powerService.getMaxPower());
				}

				UI.this.gauge.setValue(powerService.getPower(), true);
			}
		});
	}

	private CascadingStylesheet createCSS() {
		CascadingStylesheet css = new CascadingStylesheet();
		EditableStyle style = css.getDefaultStyle();
		style.setBackground(new RectangularBackground(AppColors.CONTENT_BG_BLACK));

		style = css.getSelectorStyle(new TypeSelector(GaugeWidget.class));
		style.setColor(Colors.WHITE);
		style.setFont(Fonts.getSourceSansPro82px700());
		style.setDimension(OptimalDimension.OPTIMAL_DIMENSION_XY);

		if(isLowResolution()){
			style.setExtraObject(GaugeWidget.STYLE_FONT_UNIT_LABEL, Fonts.getSourceSansPro12px400());
			style.setExtraObject(GaugeWidget.STYLE_FONT_ARC_LABELS, Fonts.getSourceSansPro12px400());
		} else {
			style.setExtraObject(GaugeWidget.STYLE_FONT_UNIT_LABEL, Fonts.getSourceSansPro22px400());
			style.setExtraObject(GaugeWidget.STYLE_FONT_ARC_LABELS, Fonts.getSourceSansPro22px400());
		}


		style.setExtraInt(GaugeWidget.STYLE_COLOR_PROGRESS_MIN, AppColors.PROGRESS_LINE_FOREGROUND_MIN);
		style.setExtraInt(GaugeWidget.STYLE_COLOR_PROGRESS_MID, AppColors.PROGRESS_LINE_FOREGROUND_MID);
		style.setExtraInt(GaugeWidget.STYLE_COLOR_PROGRESS_MAX, AppColors.PROGRESS_LINE_FOREGROUND_MAX);
		style.setExtraInt(GaugeWidget.STYLE_COLOR_PROGRESS_BG, AppColors.PROGRESS_LINE_BACKGROUND);

		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);

		style.setPadding(new UniformOutline(GAUGE_PADDING));
		return css;
	}

}
