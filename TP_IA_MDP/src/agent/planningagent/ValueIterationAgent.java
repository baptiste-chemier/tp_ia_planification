package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import environnement.gridworld.ActionGridworld;
import java.util.HashMap;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;
        protected Map<Etat, Double> vMap;
	//*** VOTRE CODE


	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,MDP mdp) {
		super(mdp);
		this.gamma = gamma;	
                vMap = new HashMap<Etat, Double>();
                
                for(int i = 0; i < mdp.getNbEtatsAccessibles(); i++)
                {
                    vMap.put(mdp.getEtatsAccessibles().get(i), 0.0);
                }
	}
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration 
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta=0.0;
		//*** VOTRE CODE
		
	
		
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur de max pour tout s de V
		//vmin est la valeur de min pour tout s de V
		// ...
		
		//******************* a laisser a la fin de la methode
		this.notifyObs();
	}
	
	
    /**
     * renvoi l'action executee par l'agent dans l'etat e
     * Si aucune actions possibles, renvoi NONE ou null.
     */
	@Override
	public Action getAction(Etat e) {
            
            List<Action> listAction = this.getPolitique(e);

            Random rand = new Random();
            int nombre = rand.nextInt(listAction.size() - 1);
            
            if(listAction.size() > 0) {
                return listAction.get(nombre);
            }
            return null;
	}
        
	@Override
	public double getValeur(Etat _e) {
		
                return vMap.get(_e);
		
		//return 0.0;
	}
	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
         * 
         * Valeur état = valeur max action
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		List<Action> l = new ArrayList<Action>();
		//*** VOTRE CODE
		
		
		return l;
		
	}
	
	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		
		
		
		
		
		/*-----------------*/
		this.notifyObs();

	}


	@Override
	public void setGamma(double arg0) {
		this.gamma = arg0;
	}

	
}
