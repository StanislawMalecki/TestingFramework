package org.testing;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class WDAbstractUtilitiesForTest
{
    protected final Logger logger = LogManager.getLogger(this.getClass());

    public abstract void init();

    public abstract void caller();

    public abstract void cleanUp();

}
