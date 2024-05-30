/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.publisher;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.microej.demo.sandbox.sharedinterface.Observer;
import com.microej.demo.sandbox.sharedinterface.PowerService;

import ej.service.ServiceFactory;

/**
 * The MqttPublisher class publishes the power readings from the PowerProvider into an MQTT queue.
 */
public class MqttPublisher implements Observer {

	private static final Logger LOGGER = Logger.getLogger("MqttPublisher"); //$NON-NLS-1$
	private static final Random RANDOM = new Random();

	/**
	 * URL of the MQTT broker.
	 */
	public static final String BROKER = "tcp://test.mosquitto.org:1883"; //$NON-NLS-1$

	/**
	 * Id to use as a client.
	 */
	public static final String PUBLISHER_ID = "publisher_" + RANDOM.nextInt(Integer.MAX_VALUE); //$NON-NLS-1$

	/**
	 * Topic to send the message to.
	 */
	public static final String TOPIC_POWER = "microej/demo/sandbox/power_" + RANDOM.nextInt(Integer.MAX_VALUE); //$NON-NLS-1$

	private final MqttClient client = new MqttClient(BROKER, PUBLISHER_ID);
	private final PowerSubscriber powerSubscriber = new PowerSubscriber();
	private int lastValue = -1;

	/**
	 * Connects the MQTT client to the BROKER and subscribes to the TOPIC.
	 */
	public void connect() {
		try {
			this.client.connect();
			LOGGER.info("Connected to " + BROKER); //$NON-NLS-1$

			this.client.subscribe(TOPIC_POWER);
			this.client.setCallback(this.powerSubscriber);
		} catch (MqttException e) {
			LOGGER.log(Level.SEVERE, "MQTT ERROR", e); //$NON-NLS-1$
		}
	}

	/**
	 * Disconnects the MQTT client.
	 */
	public void disconnect() {
		try {
			this.client.disconnect();
			this.client.close();
			LOGGER.info("Client disconnected"); //$NON-NLS-1$
		} catch (MqttException e) {
			LOGGER.log(Level.INFO, e.getMessage(), e);
		}
	}

	@Override
	public void update() {
		if (!MqttPublisher.this.client.isConnected()) {
			LOGGER.severe("Client is not connected."); //$NON-NLS-1$
			return;
		}

		// Get current data from data provider
		PowerService powerService = ServiceFactory.getService(PowerService.class);
		if (powerService == null) {
			LOGGER.severe("PowerManager service not found."); //$NON-NLS-1$
			return;
		}

		int power = powerService.getPower();
		if (power != this.lastValue) {
			this.lastValue = power;
			MqttPublisher.this.sendMessage(Float.toString(power));
		}
	}

	/**
	 * Sends an message with the provided power.
	 *
	 * @param message
	 *            the message
	 */
	private void sendMessage(String message) {
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());
		try {
			if (!this.client.isConnected()) {
				LOGGER.log(Level.WARNING, "Client is not connected"); //$NON-NLS-1$
				return;
			}
			this.client.publish(MqttPublisher.TOPIC_POWER, mqttMessage);
		} catch (MqttException e) {
			LOGGER.log(Level.WARNING, "Could not send message.", e); //$NON-NLS-1$
		}
	}

}
