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
 * Proxy class for {@link Observer} shared interface.
 */
public class ObserverProxy extends Proxy<Observer> implements Observer {

	private static final Logger LOGGER = Logger.getLogger(ObserverProxy.class.getSimpleName());

	@Override
	public void update() {
		try {
			invoke();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
