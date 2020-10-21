package search;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        File file = new File(args[1]);
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> map = new HashMap();

        /*--- read all data from file ---*/
        int i = 0;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {

                map.put(fileScanner.nextLine().trim(), i);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*--- read all data and put in map ---*/

        /*--- MENU ---*/

        boolean isMenu = true;

        while (isMenu) {
            System.out.println("\n=== Menu ===\n" +
                    "1. Find a person\n" +
                    "2. Print all people\n" +
                    "0. Exit\n");

            switch (Integer.parseInt(scanner.nextLine())) {

                case 1:

                    System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");

                    Finder finder = null;

                    String type = scanner.nextLine();

                    System.out.println("\nEnter a name or email to search all suitable people.");

                    List search = new ArrayList(Arrays.asList(scanner.nextLine().split(" ")));

                    switch (type) {
                        case "ALL":
                            finder = new Finder(new allFind());
                            break;
                        case "ANY":
                            finder = new Finder(new anyFind());
                            break;
                        case "NONE":
                            finder = new Finder(new noneFind());
                            break;
                        default:
                            break;
                    }

                    finder.find(map, search);

                    break;

                case 2:
                    System.out.println("=== List of people ===");
                    for (var entry : map.entrySet()) {
                        System.out.println(entry.getKey());
                    }
                    break;

                case 0:
                    isMenu = false;
                    System.out.println("\nBye!");
                    break;

                default:
                    System.out.println("Sorry, incorrect choice, please try again");
                    break;
            }
        }
    }
}

class Finder {

    final private FindingStrategy strategy;

    public Finder(FindingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * It performs the search algorithm according to the given strategy
     */
    public void find(Map<String, Integer> map, List<String> search) {
        strategy.getResult(map, search);
    }
}

interface FindingStrategy {

    /**
     * Returns search result
     */
    void getResult(Map<String, Integer> map, List<String> search);

}

class allFind implements FindingStrategy {

    @Override
    public void getResult(Map<String, Integer> map, List<String> search) {
        int count = 0;
        List output = new ArrayList();

        for (var entry : map.entrySet()) {
            List temp = new ArrayList(Arrays.asList(entry.getKey().toLowerCase().split(" ")));

            if (temp.containsAll(search)) {
                output.add(temp);
                count++;
            }
        }

        if (count > 0) {
            System.out.println(count + " persons found:");
            output.forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }
}

class anyFind implements FindingStrategy {

    @Override
    public void getResult(Map<String, Integer> map, List<String> search) {
        int count = 0;
        List output = new ArrayList();

        for (var entry : map.entrySet()) {
            for (String s : search) {
                if (entry.getKey().toLowerCase().contains(s)) {
                    count++;
                    output.add(entry.getKey());
                    break;
                }
            }
        }

        if (count > 0) {
            System.out.println(count + " persons found:");
            output.forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }
}

class noneFind implements FindingStrategy {

    @Override
    public void getResult(Map<String, Integer> map, List<String> search) {
        int count = 0;
        ArrayList<String> output = new ArrayList();
        boolean flag;

        for (var entry : map.entrySet()) {
            flag = true;
            for (String s : search) {
                if (entry.getKey().toLowerCase().contains(s)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                count++;
                output.add(entry.getKey());
            }
        }

        if (count > 0) {
            System.out.println(count + " persons found:");
            output.forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }
}
