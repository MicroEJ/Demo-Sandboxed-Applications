/*
 * Java
 *
 * Copyright 2023-2025 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.provider;

import java.util.Random;
import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.PowerService;
import com.microej.demo.sandbox.sharedinterface.PowerServiceListener;

import ej.annotation.Nullable;
import ej.basictool.ArrayTools;
import ej.bon.Timer;
import ej.bon.TimerTask;

/**
 * The PowerProvider class generates dummy data to simulate meter power readings.
 */
public class PowerProvider implements PowerService {

	private static final Logger LOGGER = Logger.getLogger("PowerProvider"); //$NON-NLS-1$

	private static final int MIN_POWER = 0;
	private static final int MAX_POWER = 2000;
	private static final int REFRESH_RATE_IN_MS = 4000;

	private static final Random RANDOM = new Random(); // NOSONAR the random is generated for the demo purpose, it is not unsafe to use it
	private final Timer timer;
	private int power;

	@Nullable
	private TimerTask powerProviderTask;

	private PowerServiceListener[] listeners = new PowerServiceListener[0];

	/**
	 * Creates a PowerProvider.
	 */
	public PowerProvider() {
		this.timer = new Timer();
	}

	/**
	 * Starts the power provider Timer and the periodic polling of random power data.
	 */
	public void startTimer() {
		this.powerProviderTask = new TimerTask() {
			@Override
			public void run() {
				updatePower();
				notifyListeners();
			}
		};
		this.timer.schedule(this.powerProviderTask, 0, REFRESH_RATE_IN_MS);
	}

	/**
	 * Stops the power provider Timer task when the feature is stopped.
	 */
	public void stopTimer() {
		if (this.powerProviderTask != null) {
			this.powerProviderTask.cancel();
			this.powerProviderTask = null;
		}
		notifyListeners();
	}

	private void updatePower() {
		// Generate a random power value between MIN_POWER and MAX_POWER
		this.power = RANDOM.nextInt((MAX_POWER - MIN_POWER) + 1) + MIN_POWER;
		LOGGER.info("Generated Power: " + this.power); //$NON-NLS-1$
	}

	@Override
	public int getPower() {
		return this.power;
	}

	@Override
	public int getMinPower() {
		return MIN_POWER;
	}

	@Override
	public int getMaxPower() {
		return MAX_POWER;
	}

	@Override
	public void notifyListeners() {
		for (PowerServiceListener listener : this.listeners) {
			listener.update();
		}
	}

	@Override
	public void addListener(PowerServiceListener listener) {
		PowerServiceListener[] listeners = this.listeners;
		if (!ArrayTools.contains(listeners, listener)) {
			this.listeners = ArrayTools.add(listeners, listener);
		} else {
			throw new IllegalArgumentException("Listener is already added."); //$NON-NLS-1$
		}
	}

	@Override
	public void removeListener(PowerServiceListener listener) {
		this.listeners = ArrayTools.remove(this.listeners, listener);
	}

}
