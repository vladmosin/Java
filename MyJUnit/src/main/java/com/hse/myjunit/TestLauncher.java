package com.hse.myjunit;

import com.hse.myjunit.annotations.*;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestLauncher {
    private static final int numberOfThreads = 4;

    @NotNull
    private List<Method> before = new ArrayList<Method>();
    @NotNull
    private List<Method> beforeClass = new ArrayList<Method>();
    @NotNull
    private List<Method> after = new ArrayList<Method>();
    @NotNull
    private List<Method> afterClass = new ArrayList<Method>();
    @NotNull
    private List<Method> tests = new ArrayList<Method>();

    private Class<?> currentLoadedClass;

    @NotNull
    private ExecutorService pool = Executors.newWorkStealingPool(numberOfThreads);

    public String runTests(@NotNull List<Class<?>> loadedClasses) throws InterruptedException, ExecutionException {
        var stringBuilder = new StringBuilder();
        for (var loadedClass : loadedClasses) {
            currentLoadedClass = loadedClass;
            pool = Executors.newWorkStealingPool(numberOfThreads);
            fillMethodLists(loadedClass);
            stringBuilder.append(runTests());
            clearMethodLists();
        }

        return stringBuilder.toString();
    }

    private String runTests() throws InterruptedException, ExecutionException {
        var tasks = createTasks();
        var resultList = pool.invokeAll(tasks);
        var resultOfTestClass = new ClassTestingResult();
        var stringBuilder = new StringBuilder();

        for (var futureTestResult : resultList) {
            var testResult = futureTestResult.get();
            stringBuilder.append(testResult.toString());
            resultOfTestClass.updateStatistics(testResult);
        }

        return resultOfTestClass.toString() + stringBuilder.toString();
    }

    private List<TestingTask> createTasks() {
        var tasks = new ArrayList<TestingTask>();
        for (var method : tests) {
            tasks.add(new TestingTask(method));
        }

        return tasks;
    }

    private List<Method> getAnnotatedMethods(@NotNull Class<?> clazz, @NotNull Class<? extends Annotation> annotation) {
        var annotatedMethods = new ArrayList<Method>();
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(method);
            }
        }

        return annotatedMethods;
    }

    private void fillMethodLists(@NotNull Class<?> clazz) {
        before = getAnnotatedMethods(clazz, Before.class);
        after = getAnnotatedMethods(clazz, After.class);
        beforeClass = getAnnotatedMethods(clazz, BeforeClass.class);
        afterClass = getAnnotatedMethods(clazz, AfterClass.class);
        tests = getAnnotatedMethods(clazz, Test.class);
    }

    private void clearMethodLists() {
        before.clear();
        beforeClass.clear();
        after.clear();
        afterClass.clear();
        tests.clear();
    }

    private class TestingTask implements Callable<TestingResult> {
        @NotNull private Method testingMethod;

        public TestingTask(@NotNull Method testingMethod) {
            this.testingMethod = testingMethod;
        }

        @Override
        public TestingResult call() throws FailedBeforeTestRunsException {
            Test testAnnotation = testingMethod.getAnnotation(Test.class);
            if (!testAnnotation.ignore().equals("")) {
                return new TestingResult(testingMethod, TestingResult.ResultType.IGNORED,
                        0,"Ignored: " + testAnnotation.ignore());
            }

            var object = createObject();
            long startTime = System.currentTimeMillis();
            for (var method : before) {
                try {
                    method.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return new TestingResult(testingMethod, TestingResult.ResultType.FAILED, 0,
                            "Failed before annotated method");
                }
            }

            try {
                testingMethod.invoke(object);
                if (testAnnotation.expected() != Test.NoException.class) {
                    return new TestingResult(testingMethod, TestingResult.ResultType.FAILED,
                            System.currentTimeMillis() - startTime,
                            "Exception " + testAnnotation.expected().getName() + " was not thrown");
                }
            } catch (IllegalAccessException e) {
                return new TestingResult(testingMethod, TestingResult.ResultType.FAILED, 0,
                        "Failed method invocation");
            } catch (InvocationTargetException e) {
                if (e.getTargetException().getClass() != testAnnotation.expected()) {
                    return new TestingResult(testingMethod, TestingResult.ResultType.FAILED,
                            System.currentTimeMillis() - startTime,
                            "Exception " + e.getClass().getName() + " was thrown instead of "
                                    + testAnnotation.expected().getName());
                }
            }

            for (var method : after) {
                try {
                    method.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return new TestingResult(testingMethod, TestingResult.ResultType.FAILED, 0,
                            "Failed after annotated method");
                }
            }

            var result = new TestingResult(testingMethod, TestingResult.ResultType.PASSED,
                    System.currentTimeMillis() - startTime, null);

            endTest(object);
            return result;
        }

        private void endTest(@NotNull Object object) throws FailedBeforeTestRunsException {
            for (var method : afterClass) {
                try {
                    method.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new FailedBeforeTestRunsException("Failed after all method");
                }
            }
        }

        private Object createObject() throws FailedBeforeTestRunsException {
            try {
                var constructor = currentLoadedClass.getConstructor();
                var object = constructor.newInstance();

                for (var method : beforeClass) {
                    method.invoke(object);
                }

                return object;
            } catch (NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException | InstantiationException ignored) {
                throw new FailedBeforeTestRunsException("Cannot create object");
            }
        }
    }

    private static class TestingResult {
        private enum ResultType { PASSED, FAILED, IGNORED }

        private String message;
        @NotNull private ResultType resultType;
        @NotNull private String methodName;
        private long timeMillis;

        public TestingResult(@NotNull Method method, @NotNull ResultType resultType, long timeMillis, String message) {
            this.timeMillis = timeMillis;
            this.resultType = resultType;
            this.message = message;
            methodName = method.getName();
        }

        @Override
        public String toString() {
            if (resultType == ResultType.PASSED) {
                return methodName + " PASSED\n";
            } else if (resultType == ResultType.FAILED) {
                return methodName + " FAILED " + message + "\n";
            } else {
                return methodName + " IGNORED " + message + "\n";
            }
        }
    }

    private static class ClassTestingResult {
        private int passed = 0;
        private int ignored = 0;
        private int failed = 0;
        private long timeMillis = 0;

        public void updateStatistics(@NotNull TestingResult testingResult) {
            timeMillis += testingResult.timeMillis;

            if (testingResult.resultType == TestingResult.ResultType.PASSED) {
                passed++;
            } else if (testingResult.resultType == TestingResult.ResultType.FAILED) {
                failed++;
            } else if (testingResult.resultType == TestingResult.ResultType.IGNORED) {
                ignored++;
            }
        }

        @Override
        public String toString() {
            return "PASSED: " + passed + " FAILED: " + failed +
                   " IGNORED: " + ignored + " TIME: " + timeMillis + " milliseconds\n";
        }
    }
}
