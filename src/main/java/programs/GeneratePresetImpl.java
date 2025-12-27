package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int COMPUTER_X_START = 0;
    private static final int COMPUTER_X_END = 2;

    private static final Random random = new Random();

    private Set<String> occupiedPositions = new HashSet<>();

    private int[] getRandomComputerCoordinates() {
        int x, y;
        String key;
        do {
            x = COMPUTER_X_START + random.nextInt(COMPUTER_X_END - COMPUTER_X_START + 1);
            y = random.nextInt(HEIGHT);
            key = x + "-" + y;
        } while (occupiedPositions.contains(key));
        occupiedPositions.add(key);
        return new int[]{x, y};
    }

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {

        List<Unit> armyUnits = new ArrayList<>();
        int pointsLeft = maxPoints;

        Map<String, Integer> unitTypeCount = new HashMap<>();

        for (Unit unit : unitList) {
            unitTypeCount.put(unit.getUnitType(), 0);
        }

        unitList.sort((a, b) -> {
            double attackA = (double) a.getBaseAttack() / a.getCost();
            double attackB = (double) b.getBaseAttack() / b.getCost();

            if (attackA != attackB) {
                return Double.compare(attackB, attackA);
            }

            double healthA = (double) a.getHealth() / a.getCost();
            double healthB = (double) b.getHealth() / b.getCost();

            return Double.compare(healthB, healthA);
        });

        boolean addedUnit = true;

        while (addedUnit) {
            addedUnit = false;

            for (Unit template : unitList) {

                String type = template.getUnitType();
                int count = unitTypeCount.get(type);

                if (count == 11) continue;
                if (template.getCost() > pointsLeft) continue;

                Unit newUnit = new Unit(
                        template.getName() + " " + (count + 1),
                        template.getUnitType(),
                        template.getHealth(),
                        template.getBaseAttack(),
                        template.getCost(),
                        template.getAttackType(),
                        new HashMap<>(template.getAttackBonuses()),
                        new HashMap<>(template.getDefenceBonuses()),
                        0,
                        0
                );

                int[] coords = getRandomComputerCoordinates();
                newUnit.setxCoordinate(coords[0]);
                newUnit.setyCoordinate(coords[1]);

                newUnit.setAlive(true);
                armyUnits.add(newUnit);
                unitTypeCount.put(type, count + 1);
                pointsLeft -= template.getCost();
                addedUnit = true;
            }
        }

        Army army = new Army();
        army.setUnits(armyUnits);
        army.setPoints(maxPoints - pointsLeft);

        return army;
    }
}

