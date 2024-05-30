/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.sharedinterface;

/**
 * An observer is notified when the object it observes is changed.
 *
 */
public interface Observer {

	/**
	 * This method is called whenever the observed object is changed.
	 *
	 * @see PowerService#notifyObservers()
	 */
	void update();
}
