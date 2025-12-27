package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        if (unitsByRow == null || unitsByRow.isEmpty()) return suitableUnits;

        for (int row = 0; row < unitsByRow.size(); row++) {
            List<Unit> currentRow = unitsByRow.get(row);
            if (currentRow == null || currentRow.isEmpty()) continue;

            int cols = currentRow.size();

            for (int col = 0; col < cols; col++) {
                Unit unit = currentRow.get(col);
                if (unit == null || !unit.isAlive()) continue;

                if (isLeftArmyTarget) {
                    boolean blockedLeft = (col > 0 && currentRow.get(col - 1) != null
                            && currentRow.get(col - 1).isAlive());
                    if (!blockedLeft) {
                        suitableUnits.add(unit);
                    }
                } else {
                    boolean blockedRight = (col < cols - 1 && currentRow.get(col + 1) != null
                            && currentRow.get(col + 1).isAlive());
                    if (!blockedRight) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }
}
