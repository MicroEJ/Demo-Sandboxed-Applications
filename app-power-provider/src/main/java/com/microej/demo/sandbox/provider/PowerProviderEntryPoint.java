/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.provider;

import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.PowerService;

import ej.kf.FeatureEntryPoint;
import ej.service.ServiceFactory;

/**
 * EntryPoint for the PowerProvider application.
 */
public class PowerProviderEntryPoint implements FeatureEntryPoint {

	private static final Logger LOGGER = Logger.getLogger("PowerProviderEntryPoint"); //$NON-NLS-1$
	private final PowerProvider powerProvider = new PowerProvider();

	@Override
	public void start() {
		this.powerProvider.startTimer();
		ServiceFactory.register(PowerService.class, this.powerProvider);
		LOGGER.info("started"); //$NON-NLS-1$
	}

	@Override
	public void stop() {
		ServiceFactory.unregister(PowerService.class, this.powerProvider);
		this.powerProvider.stopTimer();
		LOGGER.info("stopped"); //$NON-NLS-1$
	}
}
