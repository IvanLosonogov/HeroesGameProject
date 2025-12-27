package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {

        List<Unit> turnQueue = new ArrayList<>();
        for (Unit unit : playerArmy.getUnits()) {
            if (unit.isAlive()) {
                turnQueue.add(unit);
            }
        }
        for (Unit unit : computerArmy.getUnits()) {
            if (unit.isAlive()) {
                turnQueue.add(unit);
            }
        }

        turnQueue.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

        while (hasAliveUnits(playerArmy) && hasAliveUnits(computerArmy) && !turnQueue.isEmpty()) {

            Iterator<Unit> iterator = turnQueue.iterator();
            boolean someoneDied = false;

            while (iterator.hasNext()) {
                Unit unit = iterator.next();

                if (!unit.isAlive()) {
                    iterator.remove();
                    continue;
                }

                Unit target = unit.getProgram().attack();

                if (target != null && printBattleLog != null) {
                    printBattleLog.printBattleLog(unit, target);
                }

                Thread.sleep(50);

                if (target != null && !target.isAlive()) {
                    someoneDied = true;
                }

                if (!hasAliveUnits(playerArmy) || !hasAliveUnits(computerArmy)) {
                    return;
                }
            }

            if (someoneDied) {
                turnQueue.removeIf(u -> !u.isAlive());
                turnQueue.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
            }
        }
    }

    private boolean hasAliveUnits(Army army) {
        for (Unit unit : army.getUnits()) {
            if (unit.isAlive()) {
                return true;
            }
        }
        return false;
    }
}

