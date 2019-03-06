package task.testClasses;

public class ClassForCycleDependency2 {
    public ClassForCycleDependency1 dependency;

    public ClassForCycleDependency2(ClassForCycleDependency1 dependency) {
        this.dependency = dependency;
    }
}
