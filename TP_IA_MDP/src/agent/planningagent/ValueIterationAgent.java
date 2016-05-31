package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import environnement.gridworld.ActionGridworld;
import java.util.HashMap;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration et choisit
 * ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {

    /**
     * discount facteur
     */
    private Double gamma;
    private Map<Etat, Double> olderValues;
    private Map<Etat, Double> currentValue;

    /**
     * @param gamma
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        
        //*** TODO CODE
        this.gamma = gamma;
        olderValues = new HashMap<Etat, Double>();
        currentValue = new HashMap<Etat, Double>();

        for (Etat etat : mdp.getEtatsAccessibles()) {
            olderValues.put(etat, 0.0);
            currentValue.put(etat, 0.0);
        }
    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);
    }

    /**
     * Mise a jour de V: effectue UNE iteration de value iteration
     */
    @Override
    public void updateV() {
        
        //delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon 
        this.delta = -Double.MAX_VALUE;
        
        //TODO VOTRE CODE
        Double maxGlobal = -Double.MAX_VALUE;
        Double min = Double.MAX_VALUE;

        for (Etat e : currentValue.keySet()) {
            olderValues.put(e, currentValue.get(e));
        }

        for (Etat etat : mdp.getEtatsAccessibles()) {
            Double tmp;
            Double epsilon;
            double recompense;
            Double max = -Double.MAX_VALUE;
            for (Action action : mdp.getActionsPossibles(etat)) {
                try {
                    Map<Etat, Double> mapProbas = mdp.getEtatTransitionProba(etat, action); //retourne la proba pour des états de sortie
                    tmp = 0.0;
                    epsilon = 0.0;
                    for (Etat etatArrive : mapProbas.keySet()) { //Permet de récupérer les clés de la map précéentes (donc les états)
                        Double proba = mapProbas.get(etatArrive); // Récupération de la probabilités depuis la map
                        recompense = mdp.getRecompense(etat, action, etatArrive); // Récupération de la récompense
                        Double oldValue = olderValues.get(etatArrive);//Récupération de l'ancienne valeur de V
                        Double current = currentValue.get(etatArrive); //
                        tmp += proba * (recompense + this.gamma * oldValue);
                        System.out.println("Tmp : " + tmp);
                        epsilon = Math.abs(oldValue - current);
                    }
                    if (epsilon > delta) {
                        delta = epsilon;
                    }
                    if (tmp > max) {
                        max = tmp;
                    }
                    if (tmp < min) {
                        min = tmp;
                    }
                    if (tmp > maxGlobal) {
                        maxGlobal = tmp;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            currentValue.put(etat, max);
        }
        vmax = maxGlobal;
        vmin = min;


        //******************* a laisser a la fin de la methode
        this.notifyObs();
    }

    /**
     * renvoi l'action donnee par la politique
     */
    @Override
    public Action getAction(Etat e) {
        //TODO CODE
        List<Action> actions = this.getPolitique(e);
        if (actions.isEmpty()) {
            return ActionGridworld.NONE;
        }
        int rand = (int) (Math.random() * (actions.size() - 1));
        return actions.get(rand);
    }

    @Override
    public double getValeur(Etat _e) {
        //TODO CODE
        if (currentValue.get(_e) == null) {
            return 0.0;
        }
        return currentValue.get(_e);
    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans etat (plusieurs actions
     * sont renvoyees si valeurs identiques, liste vide si aucune action n'est
     * possible)
     * @param _e
     * @return 
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        List<Action> l = new ArrayList<Action>();

        Double tmp;
        Double max = -Double.MAX_VALUE;

        for (Action action : mdp.getActionsPossibles(_e)) {
            try {
                tmp = 0.0;
                Map<Etat, Double> probas = mdp.getEtatTransitionProba(_e, action);
                for (Etat etatProba : probas.keySet()) {
                    tmp += probas.get(etatProba) * (mdp.getRecompense(_e, action, etatProba) + this.gamma * olderValues.get(etatProba));
                }
                if (tmp > max) {
                    max = tmp;
                    l.clear();
                    l.add(action);
                } else if (tmp == max) {
                    l.add(action);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //TODO CODE
        return l;

    }

    @Override
    public void reset() {
        super.reset();
        //TODO CODE
        olderValues.clear();
        currentValue.clear();

        for (Etat etat : mdp.getEtatsAccessibles()) {
            olderValues.put(etat, 0.0);
            currentValue.put(etat, 0.0);
        }

        /*-----------------*/
        this.notifyObs();

    }

    @Override
    public void setGamma(double arg0) {
        //TODO CODE
        this.gamma = arg0;

    }
}
