# Overview

The ``sharedinterface`` project is a library shared across all the application projects.

Refer to the [Shared Interfaces](https://docs.microej.com/en/latest/ApplicationDeveloperGuide/sandboxedAppSharedInterface.html#chapter-shared-interfaces) documentation for more information.

# Usage

The ``sharedinterface`` project contains the following Shared Interface definition:

```
<sharedInterfaces>
 	<sharedInterface name="com.microej.demo.sandbox.sharedinterface.PowerService" />
 	<sharedInterface name="com.microej.demo.sandbox.sharedinterface.Observer" />
</sharedInterfaces>

```

This is a shared service definition which enables other applications to access the service via [ServiceFactory](https://repository.microej.com/javadoc/microej_5.x/apis/ej/service/ServiceFactory.html).  

To use this shared service in an application, add the following dependency to its `module.ivy`:

`<dependency org="com.microej.demo.sandbox" name="sharedinterface" rev="1.0.0" />`

# Requirements

Refer to the ``Requirements`` section of the project level [README](../README.rst#Requirements).

# Dependencies

_All dependencies are retrieved transitively by MicroEJ Module Manager_.

# Source

N/A.

# Restrictions

None.

---

_Copyright 2023-2024 MicroEJ Corp. All rights reserved._  
_Use of this source code is governed by a BSD-style license that can be found with this software._
