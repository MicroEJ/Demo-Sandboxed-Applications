/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.publisher;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * A topic listener that prints the received power data.
 */
public class PowerSubscriber implements MqttCallback {

	private static final Logger LOGGER = Logger.getLogger(PowerSubscriber.class.getName());

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		byte[] messageBytes = message.getPayload();
		LOGGER.info("Message received on topic " + topic + " => " + new String(messageBytes)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.severe("Connection lost"); //$NON-NLS-1$
	}
}