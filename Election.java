import visidia.simulation.SimulationConstants;
import visidia.simulation.process.algorithm.LC0_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

/***
 * Election of a leader in a tree with ViSiDia
 * @author dorine
 *
 */
public class Election extends LC0_Algorithm {

	@Override
	protected void beforeStart() {
		
		setLocalProperty("label", vertex.getLabel());
		// Creation of a new parameters : the number of neighbors in real time
		setLocalProperty("neighbors", getArity());
	}

	@Override
	public String getDescription() {
		
		return "Election algorithm with the follow rules: \n"+"Rule 1 : N(1)---N(x) -> F---N(x-1)\n"+"Rule 2 : N(1)---N(1) -> E(0)---F(0)";
	}

	@Override
	protected void onStarCenter() {
		
		// If the leaf is at the state of "not elected", we can stop the communication with the others
		if (getLocalProperty("label").equals("F")) {
			localTermination();
		}
		
		// Test for the first rule
		if(getLocalProperty("label").equals("N") && getNeighborProperty("label").equals("N")) {
			// If there is only one neighbors, the leaf can take the "not elected" state
			if ((((int)getLocalProperty("neighbors"))==1) && (((int)getNeighborProperty("neighbors"))>1)) {
				setLocalProperty("label","F");
				setDoorState(new MarkedState(true), neighborDoor);
				// Set the number of neighbors of the neighbors to take away this leaf
				setNeighborProperty("neighbors", ((int)getNeighborProperty("neighbors")-1));
				// Set the number of neighbors at 0 if the leaf is not elected
				setLocalProperty("neighbors", 0);
			} 
			// Test for the second Rule : the election
			else if ((((int)getLocalProperty("neighbors"))==1) && (((int)getNeighborProperty("neighbors"))==1)) {
				// After the Handshake, we choose a leaf as a "leader" and put it at the "elected" state and put all the neighbors number at 0
				setLocalProperty("label","E");
				setNeighborProperty("label","F");
				setLocalProperty("neighbors", 0);
				setNeighborProperty("neighbors", 0);
			}
		}
		// Display the neighbors number
		putProperty("Affichage", getLocalProperty("neighbors"), SimulationConstants.PropertyStatus.DISPLAYED);
	}

	@Override
	public Object clone() {
		
		return new Election();
	}

}
