package hu.rezi.gscftest.classes;

import io.cucumber.java.DataTableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class Item {
    private static final Logger LOGGER = LoggerFactory.getLogger(Item.class);

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    String name;
    double price;
    String category;

    public Item() {
        // Needed for Cucumber
    }

    public Item(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }


    @DataTableType
    public Item itemEntryTransformer(Map<String, String> row) {
        return new Item(
                row.get("name"),
                Double.parseDouble(row.get("price")),
                row.get("category")
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return
                Double.compare(price, item.price) == 0
                && Objects.equals(name, item.name)
                && Objects.equals(category, item.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }
}
