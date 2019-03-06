package task.testClasses;

public class ClassForCycleDependency1 {
    public ClassForCycleDependency2 dependency;

    public ClassForCycleDependency1(ClassForCycleDependency2 dependency) {
        this.dependency = dependency;
    }
}