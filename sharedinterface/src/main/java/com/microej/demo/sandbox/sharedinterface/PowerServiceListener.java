/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.sharedinterface;

/**
 * An power service is notified when the object it observes is changed.
 */
public interface PowerServiceListener {

	/**
	 * This method is called whenever the observed object is changed.
	 *
	 * @see PowerService#notifyListeners()
	 */
	void update();
}
