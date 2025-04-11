# Overview

The ``app-mqtt-publisher`` application publishes the power values to a MQTT topic.

# Requirements

The requirements for this application are available in the `Requirements` section from the [project level README](../README.rst).

# Usage

Follow the `Usage` section from the [project level README](../README.rst) to run this application.

The data publisher application implements a subscriber which listens to the MQTT topic, reads the incoming message and prints it to the console.
The data publisher application targets an existing MicroEJ test broker which can be immediately used as is. It is available under the address `tcp://test.mosquitto.org:1883`.

A custom MQTT tool and broker can also be used instead of the pre-configured ones.

## Monitoring the Published Data

If you're looking to establish MQTT communication using your preferred tool, follow these steps:

1. Start by installing your chosen MQTT tool on your system and launching it. Some examples of popular MQTT tools are:
    - **MQTT.fx:** https://softblade.de/en/mqtt-fx/
    - **Mosquitto:** https://mosquitto.org/
    - **HiveMQ:** https://www.hivemq.com/
2. In your MQTT tool configure the MQTT broker.
    - Default Broker: `tcp://test.mosquitto.org:1883`
    - The broker for the publisher application is set inside the `EntryPoint.java` with the `BROKER` constant.
3. Subscribe to the topic of the publisher application.
    - You can find the topic in the console output after starting the publisher application.
        - For example: `com.microej.demo.sandbox.publisher.powersubscriber INFO: Message received on topic microej/demo/sandbox/power_579410572 => 528.0`
        - The topic in this example is `microej/demo/sandbox/power_579410572`.
        - The number at the end is always randomly generated and changes every time.
    - To set your own topic in the publisher application edit the constant `TOPIC_POWER` in `EntryPoint.java`.
4. Publish data to the topic. The publisher application does also subscribe to the same topic it publishes to. This means you will see any messages you publish with your MQTT tool in the console output of the application.

# Dependencies

_All dependencies are retrieved transitively by Gradle._

# Source

N/A

# Restrictions

None.

---  
_Markdown_
_Copyright 2023-2024 MicroEJ Corp. All rights reserved._   
_Use of this source code is governed by a BSD-style license that can be found with this software._  