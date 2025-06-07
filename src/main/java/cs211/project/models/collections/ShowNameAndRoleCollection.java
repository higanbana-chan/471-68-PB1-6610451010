package cs211.project.models.collections;
import cs211.project.models.ShowNameAndRole;

import java.util.ArrayList;
import java.util.List;

public class ShowNameAndRoleCollection {
    private List<ShowNameAndRole> items;

    public ShowNameAndRoleCollection() {
        this.items = new ArrayList<>();
    }

    public void addShowNameAndRole(ShowNameAndRole showNameAndRole) {
        items.add(showNameAndRole);
    }

    public List<ShowNameAndRole> getItems() {
        return items;
    }
}
