package hu.rezi.gscftest.steps;

import hu.rezi.gscftest.classes.Item;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskSteps {

    private static final Logger logger = LoggerFactory.getLogger(TaskSteps.class);
    private List<Item> list1 = new ArrayList<>();
    private List<Item> list2 = new ArrayList<>();
    private boolean isTheSameList = false;

    @Given("I have the following items in the first list:")
    public void setList1(DataTable dataTable) {
        list1 = dataTable.asList(Item.class);
    }

    @Given("I have the following items in the second list:")
    public void setList2(DataTable dataTable) {
        list2 = dataTable.asList(Item.class);
    }

    @When("I compare both lists")
    public void compareLists() {
        isTheSameList = matchByItems(list1, list2);
    }

    private boolean matchByItems(List<Item> list1, List<Item> list2) {
        if (list1.size() != list2.size()) {
            System.out.println("List size does not match!");
            return false;
        }

        List<Item> sortedList1 = new ArrayList<>(list1);
        List<Item> sortedList2 = new ArrayList<>(list2);

        // Sort both lists by a specified order, e.g., by `name`
        Comparator<Item> itemComparator = Comparator.comparing(Item::getName)
                .thenComparing(Item::getPrice)
                .thenComparing(Item::getCategory);
        sortedList1.sort(itemComparator);
        sortedList2.sort(itemComparator);

        List<String> diffs = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            Item item1 = sortedList1.get(i);
            Item item2 = sortedList2.get(i);
            if (!item1.equals(item2)) {
                StringBuilder discrepancy = new StringBuilder();
                discrepancy.append("Mismatch found at index ").append(i).append(":\n");
                if (!item1.getName().equals(item2.getName())) {
                    discrepancy.append("  - Name mismatch: ")
                            .append("list1 has '").append(item1.getName()).append("', ")
                            .append("list2 has '").append(item2.getName()).append("'\n");
                }
                if (Double.compare(item1.getPrice(), item2.getPrice()) != 0) {
                    discrepancy.append("  - Price mismatch: ")
                            .append("list1 has ").append(item1.getPrice()).append(", ")
                            .append("list2 has ").append(item2.getPrice()).append("\n");
                }
                if (!item1.getCategory().equals(item2.getCategory())) {
                    discrepancy.append("  - Category mismatch: ")
                            .append("list1 has '").append(item1.getCategory()).append("', ")
                            .append("list2 has '").append(item2.getCategory()).append("'\n");
                }
                diffs.add(discrepancy.toString());
                logger.warn(discrepancy.toString());
            }
        }
        return diffs.isEmpty();
    }

    @Then("the lists should contain the same items with name, price, and category, regardless of order")
    public void assertItemsInLists() {
        assertTrue(isTheSameList, "The two lists do not match!");
    }
}