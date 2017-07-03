package agent76;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import list.Tuple;
import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;
import negotiator.persistent.PersistentDataType;
import negotiator.persistent.StandardInfo;
import negotiator.persistent.StandardInfoList;

public class Agent76X extends AbstractNegotiationParty{
	private Bid lastReceivedBid = null;
	private int nrChosenActions = 0; // number of times chosenAction is called.
	private StandardInfoList history;
	
	@Override
	public void init(NegotiationInfo info){
		super.init(info);
		
		System.out.println("Discount Factor: " + info.getUtilitySpace().getDiscountFactor());
		System.out.println("Reservation Value: " + info.getUtilitySpace().getReservationValueUndiscounted());
		
		if (getData().getPersistentDataType() != PersistentDataType.STANDARD)
			throw new IllegalStateException("Need standard persistent data.");
		
		history = (StandardInfoList) getData().get();
		
		if(!history.isEmpty()){
			Map<String, Double> maxUtils = new HashMap<>();
			StandardInfo lastInfo = history.get(history.size() - 1);
			
			for (Tuple<String, Double> offered : lastInfo.getUtilities()){
				String party = offered.get1();
				double util = offered.get2();
				maxUtils.put(party,  maxUtils.containsKey(party) ? Math.max(maxUtils.get(party), util) : util);
			}
			
			System.out.println(maxUtils);
		}
	}
	
	@Override
	public Action chooseAction(List<Class<? extends Action>> validActions){
		nrChosenActions++;
		
		if (nrChosenActions > history.size() && lastReceivedBid != null)
			return new Accept(getPartyId(), lastReceivedBid);
		else
			return new Offer(getPartyId(), generateRandomBid());
	}
	
	@Override
	public void receiveMessage(AgentID sender, Action action){
		super.receiveMessage(sender, action);
		
		if(action instanceof Offer)
			lastReceivedBid = ((Offer) action).getBid();
	}
	
	@Override
	public String getDescription(){
		return "Agent76 that returns Nth offer.";
	}
}
