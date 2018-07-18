import java.util.ArrayList;
import java.util.Random;

public class Program {

    final static int NUM_PROCS = 6; // How many concurrent processes
    final static int TOTAL_RESOURCES = 30; // Total resources in the system
    final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
    final static int ITERATIONS = 30; // How long to run the program
    static Random rand = new Random();
    static int totalHeldResources = 0;

    public static void main(String[] args) {

	// The list of processes:
	ArrayList<Proc> processes = new ArrayList<Proc>();
	for (int i = 0; i < NUM_PROCS; i++)
	    processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small
									   // range for its max

	// Run the simulation:
	for (int i = 0; i < ITERATIONS; i++) {
	    // loop through the processes and for each one get its request
	    for (int r = 0; r < processes.size(); r++) {
		// Get the request
		int currRequest = processes.get(r).resourceRequest(TOTAL_RESOURCES - totalHeldResources);

		// just ignore processes that don't ask for resources
		if (currRequest == 0)
		    continue;

		// Here you have to enter code to determine whether or not the request can be
		// granted,
		// negative request means processes is finished
		if (currRequest < 0) {
		    System.out.println("Proc " + r + " finished, returned " + Math.abs(currRequest));
		    totalHeldResources = totalHeldResources - Math.abs(currRequest);
		    continue;
		}
		/*
		 * cycle through the processes to see if we grant currReqest, Will it put us in
		 * a safe or unsafe state?
		 */
		boolean found = false;
		for (int p = 0; p < processes.size(); p++) {
		    int pMax = processes.get(p).getMaxResources();
		    int pCurrHeld = processes.get(p).getHeldResources();
		    int pToFinish = pMax - pCurrHeld;
		    int currAvailResources = TOTAL_RESOURCES - totalHeldResources;
		    int newCurrAvailResources = currAvailResources - currRequest;
		    // can this p finish with current request ?
		    // (pre rec currRequest must be less than total res in total system
		    if (currRequest < TOTAL_RESOURCES) {
			int rClaim = processes.get(r).getMaxResources() - processes.get(r).getHeldResources();

			if (newCurrAvailResources >= pToFinish) {
			    // for debugging
			    // System.out.println("1st if");
			    found = true;
			    break;
			}
			if (rClaim <= currAvailResources) {
			    // for debugging,
			    // System.out.println("2nd if");
			    found = true;
			    break;
			}

		    }
		}

		// and then grant the request if possible.
		// grant r request
		// Remember to give output to the console
		// this indicates what the request is, and whether or not its granted.
		// At the end of each iteration, give a summary of the current status:
		if (found) {
		    processes.get(r).addResources(currRequest);
		    totalHeldResources = totalHeldResources + currRequest;
		    System.out.println("Process " + r + " requested, " + currRequest + " granted");
		} else {
		    System.out.println("Process " + r + " requested, " + currRequest + " denied");
		}

		System.out.println("\n***** STATUS *****");
		System.out.println("Total Available: " + (TOTAL_RESOURCES - totalHeldResources));
		for (int k = 0; k < processes.size(); k++)
		    System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: "
			    + processes.get(k).getMaxResources() + ", claim: "
			    + (processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
		System.out.println("***** STATUS *****\n");
	    }
	}

    }

}
