package com.vector.diyprogress;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        AccelerationComputer accelerationComputer = new AccelerationComputer();


        for (int i = 0; i < 100; i++) {


            long time = 100 + new Random().nextInt(900);


            Thread.sleep(time);


            accelerationComputer.getAcceleration(i);


        }


        assertEquals(4, 2 + 2);
    }
}