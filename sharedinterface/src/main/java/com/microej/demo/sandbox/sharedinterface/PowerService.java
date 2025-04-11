/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.sharedinterface;

/**
 * Shared interface to access power data and notify listeners.
 */
public interface PowerService {

	/**
	 * Gets the current Power value.
	 *
	 * @return the power value in Watt or -1 if an error occurred.
	 */
	int getPower();

	/**
	 * Gets the minimum power value.
	 *
	 * @return the minimum power value in Watt or -1 if an error occurred.
	 */
	int getMinPower();

	/**
	 * Gets the maximum power value.
	 *
	 * @return the maximum power value in Watt or -1 if an error occurred.
	 */
	int getMaxPower();

	/**
	 * Notifies the listeners when the power value is changed.
	 */
	void notifyListeners();

	/**
	 * Adds a new listener.
	 *
	 * @param listener
	 *            listener to add
	 */
	void addListener(PowerServiceListener listener);

	/**
	 * Removes a listener from the set of listeners of this observable.
	 *
	 * @param listener
	 *            listener to remove
	 */
	void removeListener(PowerServiceListener listener);

}
