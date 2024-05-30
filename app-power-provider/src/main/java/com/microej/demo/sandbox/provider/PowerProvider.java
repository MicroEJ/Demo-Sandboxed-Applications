/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.provider;

import java.util.Random;
import java.util.logging.Logger;

import com.microej.demo.sandbox.sharedinterface.Observer;
import com.microej.demo.sandbox.sharedinterface.PowerService;

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

	private static final Random RANDOM = new Random();
	private final Timer timer;
	private int power;

	@Nullable
	private TimerTask powerProviderTask;

	private Observer[] observers = new Observer[0];

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
				notifyObservers();
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
	public void notifyObservers() {
		for (Observer observer : this.observers) {
			observer.update();
		}
	}

	@Override
	public void addObserver(Observer observer) {
		Observer[] observers = this.observers;
		if (!ArrayTools.contains(observers, observer)) {
			this.observers = ArrayTools.add(observers, observer);
		} else {
			throw new IllegalArgumentException("Observer is already added."); //$NON-NLS-1$
		}
	}

	@Override
	public void removeObserver(Observer observer) {
		this.observers = ArrayTools.remove(this.observers, observer);
	}

}
