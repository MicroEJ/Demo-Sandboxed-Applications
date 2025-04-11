/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.publisher;

import java.util.logging.Logger;

import android.net.ConnectivityManager;
import android.net.Network;

import com.microej.demo.sandbox.sharedinterface.PowerService;

import ej.kf.FeatureEntryPoint;
import ej.net.PollerConnectivityManager;
import ej.net.util.connectivity.ConnectivityUtil;
import ej.service.ServiceFactory;

/**
 * EntryPoint for the MQTT publisher feature application.
 */
public class MqttPublisherEntryPoint implements FeatureEntryPoint {

	private static final Logger LOGGER = Logger.getLogger("MqttPublisherEntryPoint"); //$NON-NLS-1$

	private final MqttPublisher mqttPublisher = new MqttPublisher();

	private final ConnectivityManager connectivityManager = new PollerConnectivityManager();

	@Override
	public void start() {
		ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
			@Override
			public void onAvailable(Network network) {
				MqttPublisherEntryPoint.this.mqttPublisher.connect();
				LOGGER.info("started"); //$NON-NLS-1$
			}
		};

		ConnectivityUtil.registerAndCall(connectivityManager, callback);
	}

	@Override
	public void stop() {
		this.mqttPublisher.disconnect();

		PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("Power service not found."); //$NON-NLS-1$
			return;
		}
		powerService.removeListener(this.mqttPublisher);

		LOGGER.info("stopped"); //$NON-NLS-1$
	}

}
