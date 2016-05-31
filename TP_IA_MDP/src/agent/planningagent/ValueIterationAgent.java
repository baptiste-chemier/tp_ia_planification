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
    private Map<Etat, Double> previousIterationValues;
    private Map<Etat, Double> iterationValue;

    /**
     * @param gamma
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        
        //*** TODO CODE
        this.gamma = gamma;
        previousIterationValues = new HashMap<Etat, Double>();
        iterationValue = new HashMap<Etat, Double>();

        for (Etat etat : mdp.getEtatsAccessibles()) {
            previousIterationValues.put(etat, 0.0);
            iterationValue.put(etat, 0.0);
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
        this.delta = -Double.MAX_VALUE;
        
        //TODO VOTRE CODE
        Double maxGlobal = -Double.MAX_VALUE;
        Double min = Double.MAX_VALUE;

        for (Etat e : iterationValue.keySet()) {
            previousIterationValues.put(e, iterationValue.get(e));
        }

        for (Etat etat : mdp.getEtatsAccessibles()) {
            Double tmp;
            Double tmpDelta;
            Double max = -Double.MAX_VALUE;
            for (Action action : mdp.getActionsPossibles(etat)) {
                try {
                    Map<Etat, Double> probas = mdp.getEtatTransitionProba(etat, action);
                    tmp = 0.0;
                    tmpDelta = 0.0;
                    for (Etat etatProba : probas.keySet()) {
                        Double proba = probas.get(etatProba);
                        double recompense = mdp.getRecompense(etat, action, etatProba);
                        Double previous = previousIterationValues.get(etatProba);
                        Double current = iterationValue.get(etatProba);
                        tmp += proba * (recompense + this.gamma * previous);
                        tmpDelta = Math.abs(previous - current);
                    }
                    if (tmpDelta > delta) {
                        delta = tmpDelta;
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
            iterationValue.put(etat, max);
        }
        vmax = maxGlobal;
        vmin = min;

        // mise a jour vmax et vmin pour affichage
        // ...
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
        if (iterationValue.get(_e) == null) {
            return 0.0;
        }
        return iterationValue.get(_e);
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
                    tmp += probas.get(etatProba) * (mdp.getRecompense(_e, action, etatProba) + this.gamma * previousIterationValues.get(etatProba));
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
        previousIterationValues.clear();
        iterationValue.clear();

        for (Etat etat : mdp.getEtatsAccessibles()) {
            previousIterationValues.put(etat, 0.0);
            iterationValue.put(etat, 0.0);
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
