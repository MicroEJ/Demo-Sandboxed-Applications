/*
 * Java
 *
 * Copyright 2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.demo.sandbox.sharedinterface;

/**
 * Shared interface to access power data and notify observers.
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
	 * Notifies the observers when the power value is changed.
	 */
	void notifyObservers();

	/**
	 * Adds a new observer.
	 *
	 * @param observer
	 *            observer to add
	 */
	void addObserver(Observer observer);

	/**
	 * Removes an observer from the set of observers of this observable.
	 *
	 * @param observer
	 *            observer to remove
	 */
	void removeObserver(Observer observer);

}
