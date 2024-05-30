/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui;

import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.Observer;
import com.microej.demo.sandbox.sharedinterface.PowerService;
import com.microej.demo.sandbox.ui.style.AppColors;
import com.microej.demo.sandbox.ui.style.Fonts;
import com.microej.demo.sandbox.ui.widget.GaugeWidget;

import ej.microui.MicroUI;
import ej.microui.display.Colors;
import ej.mwt.Desktop;
import ej.mwt.style.EditableStyle;
import ej.mwt.style.background.RectangularBackground;
import ej.mwt.style.outline.UniformOutline;
import ej.mwt.stylesheet.cascading.CascadingStylesheet;
import ej.mwt.stylesheet.selector.TypeSelector;
import ej.mwt.util.Alignment;
import ej.service.ServiceFactory;

/**
 * The PowerProvider class is responsible for displaying a user interface for the power readings.
 */
public class UI implements Observer {

	private static final Logger LOGGER = Logger.getLogger("UI"); //$NON-NLS-1$

	private static final String UNIT_NAME = "Watt"; //$NON-NLS-1$
	private static final String UNIT_ICON_PATH = "/images/watt_icon.png"; //$NON-NLS-1$
	private static final int MIN_POWER = 0;
	private static final int MAX_POWER = 1111;

	private static final int GAUGE_PADDING = 5;
	private final GaugeWidget gauge = new GaugeWidget(MIN_POWER, MAX_POWER, UNIT_NAME, UNIT_ICON_PATH, false);

	private boolean minMaxSet;

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

		desktop.setWidget(this.gauge);
		desktop.requestShow();
	}

	@Override
	public void update() {
		final PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("MeterData service not found."); //$NON-NLS-1$
			return;
		}

		MicroUI.callSerially(new Runnable() {
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

		style.setExtraObject(GaugeWidget.STYLE_FONT_UNIT_LABEL, Fonts.getSourceSansPro12px400());
		style.setExtraObject(GaugeWidget.STYLE_FONT_ARC_LABELS, Fonts.getSourceSansPro12px400());

		style.setExtraInt(GaugeWidget.STYLE_COLOR_PROGRESS, AppColors.PROGRESS_LINE_FOREGROUND);
		style.setExtraInt(GaugeWidget.STYLE_COLOR_PROGRESS_BG, AppColors.PROGRESS_LINE_BACKGROUND);

		style.setHorizontalAlignment(Alignment.HCENTER);
		style.setVerticalAlignment(Alignment.VCENTER);

		style.setPadding(new UniformOutline(GAUGE_PADDING));
		return css;
	}

}
