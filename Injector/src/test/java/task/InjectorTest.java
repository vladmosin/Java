package task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import task.testClasses.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class InjectorTest {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies() throws ImplementationNotFoundException,
            InjectionCycleException, AmbiguousImplementationException, ClassNotFoundException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object = Injector.initialize("task.testClasses.ClassWithoutDependencies",
                                             Collections.<String>emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency() throws ImplementationNotFoundException,
            InjectionCycleException, AmbiguousImplementationException, ClassNotFoundException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("task.testClasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertNotNull(instance.dependency);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency() throws ImplementationNotFoundException,
            InjectionCycleException, AmbiguousImplementationException, ClassNotFoundException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("task.testClasses.InterfaceImpl")
        );

        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    public void testInitializeClassWithClassAndInterfaceDependencies() throws IllegalAccessException,
            AmbiguousImplementationException, ImplementationNotFoundException,
            InstantiationException, InjectionCycleException, InvocationTargetException, ClassNotFoundException {
        var implementationList = new ArrayList<String>();

        implementationList.add("task.testClasses.InterfaceImpl");
        implementationList.add("task.testClasses.ClassWithoutDependencies");
        Object object = Injector.initialize("task.testClasses.ClassWithInterfaceAndClassDependencies",
                implementationList);

        assertTrue(object instanceof ClassWithInterfaceAndClassDependencies);
        var instance = (ClassWithInterfaceAndClassDependencies) object;
        assertTrue(instance.iDependency instanceof InterfaceImpl);
        assertNotNull(instance.cDependency);
    }

    @Test
    public void testClassWithRecursiveDependencies() throws IllegalAccessException, AmbiguousImplementationException,
            ImplementationNotFoundException, InstantiationException, InjectionCycleException,
            InvocationTargetException, ClassNotFoundException {
        var implementationList = new ArrayList<String>();

        implementationList.add("task.testClasses.InterfaceImpl");
        implementationList.add("task.testClasses.ClassWithoutDependencies");
        implementationList.add("task.testClasses.ClassWithInterfaceAndClassDependencies");
        Object object = Injector.initialize("task.testClasses.ClassWithRecursiveInitialization",
                implementationList);

        assertTrue(object instanceof ClassWithRecursiveInitialization);
        var instance = (ClassWithRecursiveInitialization) object;

        assertNotNull(instance.dependency);
    }

    @Test
    public void testThrowsAmbiguousImplementationException() {
        var implementationList = new ArrayList<String>();

        implementationList.add("task.testClasses.ClassWithoutDependencies");
        implementationList.add("task.testClasses.ClassWithoutDependencies");

        assertThrows(AmbiguousImplementationException.class,
                () -> Injector.initialize("task.testClasses.ClassWithOneClassDependency",
                        implementationList));
    }

    @Test
    public void testImplementationNotFoundException() {
        var implementationList = new ArrayList<String>();

        assertThrows(ImplementationNotFoundException.class,
                () -> Injector.initialize("task.testClasses.ClassWithOneClassDependency",
                        implementationList));
    }

    @Test
    public void testInjectionCycleException() {
        var implementationList = new ArrayList<String>();

        implementationList.add("task.testClasses.ClassForCycleDependency1");
        implementationList.add("task.testClasses.ClassForCycleDependency2");

        assertThrows(InjectionCycleException.class,
                () -> Injector.initialize("task.testClasses.ClassForCycleDependency1",
                        implementationList));
    }

    @Test
    public void testChildrenClassInsteadParent() throws IllegalAccessException, AmbiguousImplementationException,
            ImplementationNotFoundException, InstantiationException,
            InjectionCycleException, InvocationTargetException, ClassNotFoundException {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("task.testClasses.ClassWithParent")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertNotNull(instance.dependency);
    }

    @Test
    public void testClassWithTwoSameDependencies() throws IllegalAccessException, AmbiguousImplementationException,
            ImplementationNotFoundException, InstantiationException, InjectionCycleException,
            InvocationTargetException, ClassNotFoundException {
        Object object = Injector.initialize(
                "task.testClasses.ClassWithTwoSameDependencies",
                Collections.singletonList("task.testClasses.ClassWithoutDependencies")
        );

        assertTrue(object instanceof ClassWithTwoSameDependencies);
        var instance = (ClassWithTwoSameDependencies) object;

        assertNotNull(instance.dependency1);
        assertNotNull(instance.dependency2);
    }

    @Test
    public void testClassAndSuperClassException() {
        var implementationList = new ArrayList<String>();

        implementationList.add("task.testClasses.ClassWithoutDependencies");
        implementationList.add("task.testClasses.ClassWithParent");

        assertThrows(AmbiguousImplementationException.class,
                () -> Injector.initialize("task.testClasses.ClassWithOneClassDependency",
                        implementationList));
    }

    @Test
    public void testClassAndSuperClassNotException() throws IllegalAccessException,
            AmbiguousImplementationException, ImplementationNotFoundException, InstantiationException,
            InjectionCycleException, InvocationTargetException, ClassNotFoundException {
        var implementationList = new ArrayList<String>();

        implementationList.add("task.testClasses.ClassWithoutDependencies");
        implementationList.add("task.testClasses.ClassWithParent");

        Object object = Injector.initialize("task.testClasses.ClassWithOneChildrenClassDependency",
                        implementationList);

        assertTrue(object instanceof ClassWithOneChildrenClassDependency);
        var instance = (ClassWithOneChildrenClassDependency) object;

        assertNotNull(instance.dependency);
    }
}