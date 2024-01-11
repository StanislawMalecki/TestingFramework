package org.testing;

import org.junit.*;

public abstract class WDAbstractUtilitiesForTest
{
    @Before
    public abstract void init();

    @Test
    public abstract void caller();

    @After
    public abstract void cleanUp();

}
