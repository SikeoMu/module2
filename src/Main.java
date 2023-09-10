import Island.Island;
import Island.Settings;
import Island.Unit.Animal;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Settings.fillParameters();
        Island island = new Island(10, 5);
        island.fillMap();
        getInfo(island);
        reproduction(island);

        getInfo(island);
    }

    public static void getInfo(Island island) {
        for (int i = 0; i < island.getWidth(); i++) {
            for (int j = 0; j < island.getLength(); j++) {
                System.out.print(j + ":" + i + "\t|" + island.getCell(j, i).getAnimals().size() + "| \t\t");
            }
            System.out.println();
        }
    }

    public static void reproduction(Island island) {
        Random random = new Random();
        for (String coordinate : island.getCoordinates()) {
            int countAnimal = 0;
            if (island.getCell(coordinate).getAnimals().size() <= 1) continue;
            if (island.getCell(coordinate).getAnimals().size() == island.getCell(coordinate).getAnimals().get(0).getMaxInCell())
                continue;
            Iterator<Animal> iterator = island.getCell(coordinate).getAnimals().listIterator();
            while (iterator.hasNext()) {
                countAnimal++;
                boolean isMultiply = random.nextBoolean();
                Animal animal = iterator.next();
                if (animal.getMaxInCell() == island.getCell(coordinate).getAnimals().size()) return;
                if (countAnimal % 2 != 0) continue;
                if (!isMultiply) continue;
                island.getCell(coordinate).getAnimals().add(animal.multiply(animal));
            }
        }
    }

    public static void movement(Island island) {
        HashMap<String, List<Animal>> waitingTransfer = new HashMap<>();
        for (String coordinate : island.getCoordinates()) {
            if (island.getCell(coordinate).getAnimals().size() == 0) continue;
            for (int i = island.getCell(coordinate).getAnimals().size() - 1; i >= 0; --i) {
                if (island.getCell(coordinate).getAnimals().size() == 0) continue;
                Animal animal = island.getCell(coordinate).getAnimals().get(i);
                String newPosition = animal.move(coordinate, animal.getMaxShift(), island.getLength(), island.getLength());

                if (newPosition.equals("stop")) continue;

                if (!(island.getCoordinates().contains(newPosition))) continue;
                if (island.getCell(newPosition).getAnimals().size() == 0) {
                    island.getCell(newPosition).getAnimals().add(animal);
                    island.getCell(coordinate).getAnimals().remove(animal);
                    continue;
                }

                if (animal.toString().equals(island.getCell(newPosition).getAnimals().get(0).toString())) {
                    if (waitingTransfer.containsKey(newPosition) && waitingTransfer.get(newPosition).get(0).toString().equals(animal.toString())) {
                        waitingTransfer.get(newPosition).add(animal);
                        island.getCell(coordinate).getAnimals().remove(animal);
                    } else {
                        waitingTransfer.put(newPosition, new ArrayList<>());
                        waitingTransfer.get(newPosition).add(animal);
                        island.getCell(coordinate).getAnimals().remove(animal);
                    }
                }


            }
        }
        for (Map.Entry<String, List<Animal>> entry : waitingTransfer.entrySet()) {
            for (Animal animal : entry.getValue()) {
                island.getCell(entry.getKey()).getAnimals().add(animal);
            }
        }

    }
}