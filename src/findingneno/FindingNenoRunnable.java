package findingneno;

import Prism.core.AbstractDispatch;
import Prism.core.Architecture;

public class FindingNenoRunnable implements Runnable {
    private final Architecture architecture;
    private final AbstractDispatch dispatch;

    public FindingNenoRunnable(Architecture architecture, AbstractDispatch dispatch) {
	this.architecture = architecture;
	this.dispatch = dispatch;
    }

    @Override
    public void run() {
	dispatch.start();
	architecture.start();
    }

}
