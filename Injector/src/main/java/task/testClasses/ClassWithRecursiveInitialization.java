package task.testClasses;

public class ClassWithRecursiveInitialization {
    public ClassWithInterfaceAndClassDependencies dependency;

    public ClassWithRecursiveInitialization(ClassWithInterfaceAndClassDependencies dependency) {
        this.dependency = dependency;
    }
}
