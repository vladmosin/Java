package task.testClasses;

public class ClassWithOneChildrenClassDependency {

    public final ClassWithParent dependency;

    public ClassWithOneChildrenClassDependency(ClassWithParent dependency) {
        this.dependency = dependency;
    }
}