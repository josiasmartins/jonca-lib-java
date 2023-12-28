package com.jonca.libjava.utils;

import com.jonca.libjava.annotations.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class LoggerUtil {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LoggerUtil.class);

    private LoggerUtil() {}

    /**
     *  Extracts properties annoted with @Logger from an object and populates the property map.
     *
     * @param object The object to extrat properties from.
     * @return A map containing extracted properties.
     */
    private static Map<String, String> extractProperties(Object object) {
        Map<String, String> propertiesMap = new HashMap<>();
        extractPropertiesRecursively(object, "", propertiesMap);
        return  propertiesMap;
    }

    /**
     * Recursively extracts properties from an object and populates the property map.
     *
     * @param object         The object to extract properties from.
     * @param prefix         The prefix for the property name.
     * @param propertiesMap  The map to store extracted properties
     */
    private static void extractPropertiesRecursively(Object object, String prefix, Map<String, String> propertiesMap) {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (isFieldIgnorable(field)) {
                continue;
            }

            if (field.isAnnotationPresent(Logger.class)) {
                try {
                    Object value = getFieldValueUsingGetter(object, field);
                    String propertyName = dynamicPropertyName(field, prefix);

                    if (isSimpleType(value)) {
                        propertiesMap.put(propertyName, value != null ? value.toString() : "null");
                    } else if (value instanceof List<?>) {
                        processList((List<?>) value, propertyName, propertiesMap);
                    } else if (isCollectionImmutable(value)) {
                        Object mutableCopy = createMutableCopy(value);
                        extractPropertiesRecursively(mutableCopy, propertyName, propertiesMap);
                    } else {
                        extractPropertiesRecursively(value, propertyName, propertiesMap);
                    }

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
                    LOG.error("Error extracting property: " + field.getName(), ex);
                }
            }
        }
    }

    /**
     *  Checks if a field should be ignored (static, final, or synthetic).
     *
     * @param field The field to checks.
     * @return True if the field should be ignored, false otherwise.
     */
    private static boolean isFieldIgnorable(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || field.isSynthetic();
    }

    /**
     * Gets the field value using the corresponding getter method
     *
     * @param object  The object containing the field
     * @param field   The field for which to get the value
     * @return The value of the field
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static Object getFieldValueUsingGetter(Object object, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            String getterMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            Method getterMethod = object.getClass().getMethod(getterMethodName);
            return getterMethod.invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException("Error extrating property: " + field.getName(), ex);
        }
    }

    /**
     * Get dynamic property name field with prefix and Logger annotation
     *
     * @param field   The field
     * @param prefix  The prefix for the property name
     * @return A property name
     */
    private static String dynamicPropertyName(Field field, String prefix) {
        String valueLoggerAnnotation = field.getAnnotation(Logger.class).value();
        String dynamicValue = valueLoggerAnnotation.isEmpty() ? field.getName() : valueLoggerAnnotation;

        return prefix.isEmpty() ? dynamicValue : prefix + "." + dynamicValue;
    }

    /**
     * Checks if a value is a simple type (String, Number, Boolean or null)
     *
     * @param value The value to check.
     * @return True if the value is a simple type, false otherwise
     */
    private static boolean isSimpleType(Object value) {
        return (value instanceof String || value instanceof Number || value instanceof Boolean || value != null);
    }

    /**
     * Processes a list, extracting properties and updating the property map.
     *
     * @param list
     * @param propertyName
     * @param propertiesMap
     */
    private static void processList(List<?> list, String propertyName, Map<String, String> propertiesMap) {
        for (int i = 0; i < list.size(); i++) {
            extractPropertiesRecursively(list.get(i), concatFullNameProperty(propertyName, i), propertiesMap);
        }
    }

    /**
     * Concatenates the property name with the index for array or list elements.
     *
     * @param propertyName The name of the property
     * @param index        The index of the array or list elements
     * @return The concatenated property name
     */
    public static String concatFullNameProperty(String propertyName, int index) {
        return propertyName + "[" + index + "]";
    }

    /**
     * Creates a mutable copy of a collections if it is immutable
     * @param value The collection to copy
     * @return A mutable copy of the collection
     */
    private static Object createMutableCopy(Object value) {
        if (value instanceof List) {
            List<?> listImmutable = (List<?>) value;
            return new ArrayList<>(listImmutable);
        }
        return value;
    }

    /**
     * Checks if a collection is immutable
     * @param value The collection to check.
     * @return True if the collection is immutable, false otherwise.
     */
    private static boolean isCollectionImmutable(Object value) {
        return (value instanceof Collection) && value.getClass().getName().startsWith("java.util.ImmutableCollections$");
    }

}
