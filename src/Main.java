import database.DatabaseConnectionHandler;
import ui.DatabaseLoginUi;
import ui.UIDrawer;

public class Main {
    private static UIDrawer ui;
    public static void main(String[] args) {
        DatabaseConnectionHandler db = new DatabaseConnectionHandler();
        ui = new UIDrawer(db);
        DatabaseLoginUi dli = new DatabaseLoginUi(db, ui);
    }

}
