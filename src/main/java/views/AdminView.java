package views;


import adminManagement.FruitManagement;
import adminManagement.OrderManagement;
import adminManagement.UserInformation;

public class AdminView {

    private static void showActionsForm() {
        System.out.println("\n**************************************");
        System.out.println("|                                    |");
        System.out.println("|            << ADMIN >>             |");
        System.out.println("|                                    |");
        System.out.println("|      1. Quản lí người dùng.        |");
        System.out.println("|      2. Quản lí hàng hóa.          |");
        System.out.println("|      3. Quản lí đơn hàng.          |");
        System.out.println("|      0. Về trang chủ.              |");
        System.out.println("|                                    |");
        System.out.println("**************************************\n");
    }

    public static void chooseAdminAction() {
        do {
            showActionsForm();
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    UserInformation.chooseActionInUsersInfo();
                    break;
                }
                if (number == 2) {
                    FruitManagement.chooseActionInMedicineManagement();
                    break;
                }
                if (number == 3) {
                    OrderManagement.chooseAction();
                    break;
                }
                if (number == 0) {
                    Menu.chooseInEntrance();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }
        } while (true);
    }
}

