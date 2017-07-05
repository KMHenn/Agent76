package agent76;

import java.util.ArrayList;
import java.util.List;

import negotiator.Bid;
import negotiator.actions.Action;
import negotiator.parties.AbstractNegotiationParty;

public class OpponentTracker extends AbstractNegotiationParty{
	private Bid mostRecentBid;
	private double mostRecentUtility;
	static ArrayList<Double> offeredUtilities = new ArrayList<>();
	
	/**
	 * Update the most recent bid.
	 * 
	 * @param bid
	 */
	protected void updateMostRecentBid (Bid bid){
		mostRecentBid = bid;
		updateMostRecentUtility();
	}
	
	/**
	 * Update the most recent utility, using the most recent bid.
	 */
	protected void updateMostRecentUtility(){
		mostRecentUtility = getUtilitySpace().getUtility(mostRecentBid);
		offeredUtilities.add(mostRecentUtility);
	}
	
	/**
	 * Get the most recent bid.
	 * 
	 * @return mostRecentBid
	 */
	protected Bid getMostRecentBid(){
		return mostRecentBid;
	}
	
	/**
	 * Get the utility of the most recent bid.
	 * 
	 * @return mostRecentUtility
	 */
	protected double getMostRecentUtility(){
		return mostRecentUtility;
	}
	
	/**
	 * Calculate the average of all utilities.
	 * 
	 * @return averageUtility
	 */
	protected double getAverageUtilities(){
		double totalUtility = 0;
		double averageUtility;
		
		for (int i = 0; i < offeredUtilities.size(); i++)
			totalUtility += offeredUtilities.get(i);
		
		averageUtility = totalUtility / offeredUtilities.size();
		return averageUtility;
	}

	/**
	 * Generated to allow extension of AbstractNegotiationParty, enabling
	 * use of getUtilitySpace.
	 * 
	 * @return action
	 */
	@Override
	public Action chooseAction(List<Class<? extends Action>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get the description of the class.
	 * 
	 * @return description
	 */
	@Override
	public String getDescription() {
		return "Opponent Log";
	}
}
