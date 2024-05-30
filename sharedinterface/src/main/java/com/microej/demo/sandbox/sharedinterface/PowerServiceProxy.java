/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.sharedinterface;

import java.util.logging.Level;
import java.util.logging.Logger;

import ej.kf.Proxy;

/**
 * Proxy class for {@link PowerService} shared interface.
 */
public class PowerServiceProxy extends Proxy<PowerService> implements PowerService {

	private static final Logger LOGGER = Logger.getLogger(PowerServiceProxy.class.getSimpleName());

	@Override
	public int getPower() {
		try {
			return invokeInt();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return -1;
		}
	}

	@Override
	public int getMinPower() {
		try {
			return invokeInt();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return -1;
		}
	}

	@Override
	public int getMaxPower() {
		try {
			return invokeInt();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return -1;
		}
	}

	@Override
	public void notifyObservers() {
		try {
			invoke();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void addObserver(Observer observer) {
		try {
			invoke();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void removeObserver(Observer observer) {
		try {
			invoke();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
