package task.testClasses;

public class ClassWithTwoSameDependencies {
    public ClassWithoutDependencies dependency1;
    public ClassWithoutDependencies dependency2;

    public ClassWithTwoSameDependencies(ClassWithoutDependencies dependency1,
                                        ClassWithoutDependencies dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }
}
