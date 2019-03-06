package task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**Class, which emulates container Dependency injection*/
public class Injector {
    /**Stores already built objects*/
    private static HashMap<Class<?>, Object> builtObjects = new HashMap<>();

    /**Stores used implementations*/
    private static HashSet<Class<?>> usedImplementations = new HashSet<>();

    /**Initialize object of class with given name
     * @param rootClassName Name of object's type
     * @param implementationClassNames List of possible implementations
     * */
    public static Object initialize(String rootClassName, List<String> implementationClassNames)
            throws InjectionCycleException, ClassNotFoundException,
            AmbiguousImplementationException, ImplementationNotFoundException,
            IllegalAccessException, InstantiationException, InvocationTargetException {
        builtObjects.clear();
        usedImplementations.clear();

        var clazz = Class.forName(rootClassName);
        List<Class<?>> implementationClasses = new ArrayList<>();

        for (var implementationClassName : implementationClassNames) {
            implementationClasses.add(Class.forName(implementationClassName));
        }

        return initialize(clazz, implementationClasses, true);
    }

    /**Initialize object of class with given name
     * @param clazz Information about object's type
     * @param implementationClasses List of possible implementations
     * @param isRoot True if building class is purpose
     * */
    private static Object initialize(Class<?> clazz, List<Class<?>> implementationClasses, boolean isRoot)
            throws InjectionCycleException, AmbiguousImplementationException, ImplementationNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (builtObjects.containsKey(clazz)) {
            return builtObjects.get(clazz);
        }

        Class<?> implementation = clazz;

        if (!isRoot) {
            implementation = findImplementation(clazz, implementationClasses);
        }

        if (usedImplementations.contains(implementation)) {
            throw new InjectionCycleException();
        }

        var constructor = clazz.getDeclaredConstructors()[0];
        List<Object> arguments = new ArrayList<>();

        constructor.setAccessible(true);
        usedImplementations.add(implementation);
        for (var parameter : constructor.getParameterTypes()) {
            if (isInterfaceOrAbstractClass(parameter)) {
                arguments.add(initializeInterface(parameter, implementationClasses));
            } else {
                arguments.add(initialize(parameter, implementationClasses, false));
            }
        }

        usedImplementations.remove(implementation);

        var builtObject = constructor.newInstance(arguments.toArray());

        builtObjects.put(implementation, builtObject);
        return builtObject;
    }

    /**Initialize interface*/
    private static Object initializeInterface(Class<?> clazz, List<Class<?>> implementationClasses)
            throws AmbiguousImplementationException, ImplementationNotFoundException,
            InjectionCycleException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> implementation = findImplementation(clazz, implementationClasses);

        return initialize(implementation, implementationClasses, false);
    }

    /**Finds possible implementation*/
    private static Class<?> findImplementation(Class<?> clazz, List<Class<?>> implementationClasses)
            throws AmbiguousImplementationException, ImplementationNotFoundException {
        Class<?> possibleImplementation = null;
        for (var implementation : implementationClasses) {
            if (!isInterfaceOrAbstractClass(implementation)) {
                if (clazz.isAssignableFrom(implementation)) {
                    if (possibleImplementation != null) {
                        throw new AmbiguousImplementationException();
                    } else {
                        possibleImplementation = implementation;
                    }
                }
            }
        }

        if (possibleImplementation == null) {
            throw new ImplementationNotFoundException();
        } else {
            return possibleImplementation;
        }
    }

    /**Checks if given class is interface or abstract class*/
    private static boolean isInterfaceOrAbstractClass(Class<?> clazz) {
        int modifiers = clazz.getModifiers();

        return Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers);
    }
}