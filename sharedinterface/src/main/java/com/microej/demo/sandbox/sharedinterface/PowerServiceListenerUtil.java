/*
 * Java
 *
 * Copyright 2023-2024 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */

package com.microej.demo.sandbox.sharedinterface;

import ej.service.ServiceFactory;

import java.util.logging.Logger;

/**
 * Power service utility class.
 */
public class PowerServiceListenerUtil {

    private static final Logger LOGGER = Logger.getLogger("PowerServiceListenerUtil"); //$NON-NLS-1$

    private static final int POWER_SERVICE_POLLING_PERIOD = 2_000;

    /**
     * Waits for the power service to be available and returns the power service instance.
     * <p>
     * This method is blocking and could be called in its own thread.
     *
     * @return the power service instance.
     */
    public PowerService waitAndGetPowerService() {
        PowerService powerService = ServiceFactory.getService(PowerService.class);

        while (powerService == null) {
            LOGGER.info("waiting for power service");
            try {
                Thread.sleep(POWER_SERVICE_POLLING_PERIOD);
            } catch (InterruptedException e) {
                LOGGER.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
            powerService = ServiceFactory.getService(PowerService.class);
        }

        return powerService;
    }

}
