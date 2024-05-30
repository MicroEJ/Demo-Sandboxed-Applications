/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.ui;

import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.PowerService;

import ej.kf.FeatureEntryPoint;
import ej.service.ServiceFactory;

/**
 * EntryPoint for the UI feature application.
 */
public class GuiEntryPoint implements FeatureEntryPoint {

	private static final Logger LOGGER = Logger.getLogger("GuiEntryPoint"); //$NON-NLS-1$
	private final UI ui = new UI();

	@Override
	public void start() {
		this.ui.show();

		PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("Power service not found."); //$NON-NLS-1$
			return;
		}

		powerService.addObserver(this.ui);
		LOGGER.info("started"); //$NON-NLS-1$
	}

	@Override
	public void stop() {
		PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("Power service not found."); //$NON-NLS-1$
			return;
		}
		powerService.removeObserver(this.ui);

		LOGGER.info("stopped"); //$NON-NLS-1$
	}

}
