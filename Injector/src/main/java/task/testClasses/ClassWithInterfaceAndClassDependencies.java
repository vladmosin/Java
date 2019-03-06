package task.testClasses;

public class ClassWithInterfaceAndClassDependencies {
    public Interface iDependency;
    public ClassWithoutDependencies cDependency;

    public ClassWithInterfaceAndClassDependencies(Interface iDependency, ClassWithoutDependencies cDependency) {
        this.iDependency = iDependency;
        this.cDependency = cDependency;
    }
}
