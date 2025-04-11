/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.sharedinterface;

import java.util.logging.Level;
import java.util.logging.Logger;

import ej.kf.Proxy;

/**
 * Proxy class for {@link PowerServiceListener} shared interface.
 */
public class PowerServiceListenerProxy extends Proxy<PowerServiceListener> implements PowerServiceListener {

	private static final Logger LOGGER = Logger.getLogger(PowerServiceListenerProxy.class.getSimpleName());

	@Override
	public void update() {
		try {
			invoke();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
