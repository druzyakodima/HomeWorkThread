package Task2;


import java.util.Arrays;
import java.util.concurrent.*;

public class TestExecute {
    static int size = 10000000;
    static float[] arr = new float[size];
    static int h = size / 2;
    static final CountDownLatch cdl = new CountDownLatch(1);


    public static void main(String[] args) {

        firstMethod();
        secondMethod();

    }

    public static void firstMethod() {

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1.0f;
        }

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) ((arr[i] * Math.sin(0.2f + i / 5)) * Math.cos(0.2f + i / 5) * Math.cos(0.4 + i / 2));
        }

        System.out.println("One thread time: " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    public static void secondMethod() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1.0f;
        }

        float[] leftHalf = new float[h];
        float[] rightHalf = new float[h];


        long splittingArr = System.currentTimeMillis();

        System.arraycopy(TestExecute.arr, 0, leftHalf, 0, h);
        System.arraycopy(TestExecute.arr, h, rightHalf, 0, h);

        System.out.println("Splitting the array time: " + (System.currentTimeMillis() - splittingArr) + " ms.");
        CountDownLatch cdl = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {

            long startTimeExecutor1 = System.currentTimeMillis();

            for (int i = 0; i < leftHalf.length; i++) {
                leftHalf[i] = (float) ((leftHalf[i] * Math.sin(0.2f + i / 5)) * Math.cos(0.2f + i / 5) * Math.cos(0.4 + i / 2));
            }

            System.out.println("Thread t1  time: " + (System.currentTimeMillis() - startTimeExecutor1) + " ms.");

            long startTimeExecutor2 = System.currentTimeMillis();

            for (int i = rightHalf.length; i < arr.length; i++) {
                rightHalf[i - rightHalf.length] = (float) ((rightHalf[i - rightHalf.length] * Math.sin(0.2f + i / 5)) * Math.cos(0.2f + i / 5) * Math.cos(0.4 + i / 2));
            }

            System.out.println("Thread t2  time: " + (System.currentTimeMillis() - startTimeExecutor2) + " ms.");
        });

        executorService.shutdown();

        float[] mergeArray = new float[size];
        long gluingLeftHalfAndRightHalfStart = System.currentTimeMillis();

        System.arraycopy(leftHalf, 0, mergeArray, 0, h);
        System.arraycopy(rightHalf, 0, mergeArray, h, h);

        long endGlueArrays = System.currentTimeMillis() - gluingLeftHalfAndRightHalfStart;
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Glue the left half and the right half time: " + endGlueArrays + " ms.");
        System.out.println("Multithreaded time: " + endTime + " ms.");

        // System.out.println(Arrays.toString(mergeArray) + " array 2");
    }
}
