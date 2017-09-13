/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.rest.v2;

import com.microsoft.rest.v2.annotations.Host;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for creating individual Swagger interface method parsers from a Swagger
 * interface.
 */
class SwaggerInterfaceParser {
    private final Class<?> swaggerInterface;
    private final String host;
    private final Map<Method, SwaggerMethodParser> methodParsers = new HashMap<>();

    /**
     * Create a new SwaggerInterfaceParser object with the provided fully qualified interface
     * name.
     * @param swaggerInterface The interface that will be parsed.
     */
    SwaggerInterfaceParser(Class<?> swaggerInterface, String host) {
        this.swaggerInterface = swaggerInterface;

        final Host hostAnnotation = swaggerInterface.getAnnotation(Host.class);
        if (hostAnnotation == null) {
            throw new MissingRequiredAnnotationException(Host.class, swaggerInterface);
        }
        else if (host != null) {
            this.host = host;
        } else {
            this.host = hostAnnotation.value();
        }
    }

    /**
     * Get the method parser that is associated with the provided swaggerMethod. The method parser
     * can be used to get details about the Swagger REST API call.
     * @param swaggerMethod The method to generate a parser for.
     * @return The SwaggerMethodParser associated with the provided swaggerMethod.
     */
    public SwaggerMethodParser methodParser(Method swaggerMethod) {
        SwaggerMethodParser result = methodParsers.get(swaggerMethod);
        if (result == null) {
            result = new SwaggerMethodParser(swaggerMethod, host());
            methodParsers.put(swaggerMethod, result);
        }
        return result;
    }

    /**
     * Get the desired host that the provided Swagger interface will target with its REST API
     * calls. This value is retrieved from the @Host annotation placed on the Swagger interface.
     * @return The value of the @Host annotation.
     */
    String host() {
        return host;
    }
}