/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.publisher;

import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.PowerService;

import ej.kf.FeatureEntryPoint;
import ej.service.ServiceFactory;

/**
 * EntryPoint for the MQTT publisher feature application.
 */
public class MqttPublisherEntryPoint implements FeatureEntryPoint {

	private static final Logger LOGGER = Logger.getLogger("MqttPublisherEntryPoint"); //$NON-NLS-1$

	private final MqttPublisher mqttPublisher = new MqttPublisher();

	@Override
	public void start() {
		this.mqttPublisher.connect();

		// Obtain PowerService and add MQTT publisher as observer
		PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("Power service not found."); //$NON-NLS-1$
			return;
		}

		powerService.addObserver(this.mqttPublisher);

		LOGGER.info("started"); //$NON-NLS-1$
	}

	@Override
	public void stop() {
		this.mqttPublisher.disconnect();

		PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("Power service not found."); //$NON-NLS-1$
			return;
		}
		powerService.removeObserver(this.mqttPublisher);

		LOGGER.info("stopped"); //$NON-NLS-1$
	}

}
