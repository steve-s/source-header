package sourceheader.core;

/**
 * Listener of report progress during some time consuming operation.
 * 
 * This provides mechanism for reporting progress while not injecting gui code
 * into core.
 *
 * @author steve
 */
public interface ProgressReportConsumer {
    /**
     * This method is continuosly repeatedly called during the operation.
     */
    void progress();

    /**
     * This method is called when the operation is done.
     */
    void done();
}
