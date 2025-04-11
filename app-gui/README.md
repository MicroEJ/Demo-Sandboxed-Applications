# Overview

The `ap-gui` application displays a gauge with the values provided by the `provider` app.
Values are retrieved through the `PowerService` shared interface provided by the `sharedinterface` library.

# Requirements

The requirements for this application are available in the `Requirements` section from the [project level README](../README.rst).

In order to improve performances, this application caches some of the drawings into a [BufferedImage](https://docs.microej.com/en/latest/VEEPortingGuide/uiBufferedImage.html).
Thus, the application needs to be run on a firmware that has an Images Heap defined. The required Images heap size can be up to the size of the screen (`screenWidth * screenHeight * colorDepth / 8` bytes).

# Usage

Follow the `Usage` section from the [project level README](../README.rst) to run this application.

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