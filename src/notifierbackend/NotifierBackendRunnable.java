package notifierbackend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Prism.core.AbstractDispatch;
import Prism.core.Architecture;

public class NotifierBackendRunnable implements Runnable {
    private static final Logger logger = LogManager.getLogger(NotifierBackendRunnable.class.getName());

    private final Architecture architecture;
    private final AbstractDispatch dispatch;

    public NotifierBackendRunnable(Architecture architecture, AbstractDispatch dispatch) {
	this.architecture = architecture;
	this.dispatch = dispatch;
    }

    @Override
    public void run() {
	logger.info("Starting architecture");
	dispatch.start();
	architecture.start();
    }

}
