package adminManagement;




import Services.IOrderService;
import Services.OrderService;
import customersServices.PurchaseHistory;
import model.Order;
import model.Role;
import views.AdminView;
import views.Menu;

import java.util.List;

public class OrderManagement {
    private static IOrderService orderService = new OrderService();

    private static void showActionForm() {
        System.out.println("\n1. Sắp xếp theo ngày thanh toán.");
        System.out.println("2. Sắp xếp theo tổng thanh toán.");
        System.out.println("3. Tìm thông tin bằng ID đơn hàng.");
        System.out.println("0. Thoát.");
    }

    public static void chooseAction() {
        List<Order> userOrdersList = orderService.getOrders();
        do {
            PurchaseHistory.showOrderList(Role.ADMIN, userOrdersList);
            showActionForm();
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    sortByCreationDateACSE(userOrdersList);
                    continue;
                }
                if (number == 2) {
                    sortByTotalPriceACSE(userOrdersList);
                    continue;
                }
//                if (number == 3) {
//                    PurchaseHistory.searchOrder(Role.ADMIN, userOrdersList);
//                    continue;
//                }
                if (number == 3) {
                    PurchaseHistory.showOrderDetails(userOrdersList);
                    continue;
                }
                if (number == 0) {
                    AdminView.chooseAdminAction();
                    break;
                }
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void sortByCreationDateACSE(List<Order> userOrdersList) {
        userOrdersList.sort((e1, e2) -> Long.compare(e1.getCreationTime() - e2.getCreationTime(), 0));
        showResultAndOperation(userOrdersList);
    }

    private static void sortByTotalPriceACSE(List<Order> userOrdersList) {
        userOrdersList.sort((e1, e2) -> Double.compare(e1.getGrandTotal() - e2.getGrandTotal(), 0));
        showResultAndOperation(userOrdersList);
    }

    private static void showResultAndOperation(List<Order> userOrdersList) {
        PurchaseHistory.showOrderList(Role.ADMIN, userOrdersList);
        PurchaseHistory.showOrderDetails(userOrdersList);
    }

}

