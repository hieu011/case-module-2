package customersServices;




import Services.*;
import adminManagement.FruitManagement;
import model.Fruit;
import model.Order;
import model.OrderItem;
import model.User;
import utils.CSVUtils;
import utils.ValidateUtils;
import views.CustomerView;
import views.Exit;
import views.LoginView;
import views.Menu;

import java.util.*;

public class FruitBuy {
    private static IFruitService fruitService = new FruitService();
    private static IUserService userService = new UserService();
    private static IOrderService orderService = new OrderService();
    private static IOderItemService orderItemService = new OrderItemService();
    private static final Scanner input = new Scanner(System.in);

    public static void setInformation() {
        do {
            Order newOrder = new Order();
            System.out.println("\n!Trước khi đặt hàng, chúng tôi cần bạn cung cấp thông tin cá nhân!\n");
            System.out.println("     * Nhập '1' để sử dụng thông tin hiện tại.");
            System.out.println("     * Nhập '2' để sử dụng thông tin khác.");
            System.out.println("     * Nhập '0' to exit.");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    setNewOrderWithDefaultInfo(newOrder);
                }
                if (number == 2) {
                    setNewOrderWithOtherInfo(newOrder);
                }
                if (number == 0) {
                    CustomerView.chooseServicesForGuest();
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }

            if (newOrder.getName() == null || newOrder.getPhoneNumber() == null || newOrder.getAddress() == null) {
                System.out.println("\nKhông có thông tin, vui lòng thử lại!\n");
                continue;
            }

            List<Fruit> fruits = fruitService.getFruits();
            ArrayList<OrderItem> orderItemList = showHowToGetDrug(newOrder, fruits);
            if (orderItemList.size() == 0) {
                System.out.println("\n----- Không có mặt hàng nào trong giỏ hàng của bạn. Vui lòng thử lại!");
                continue;
            }
            if (orderItemList.size() > 0) {
                System.out.println("\n--- Thêm toàn bộ mặt hàng vào giỏ hàng thành công ---");
                System.out.println("*** Nhập '1' để thấy hóa đơn của bạn.");
                System.out.println("*** Nhập '0' để xóa giỏ hàng và quay lại đặt hàng.");
            }
            int num = Integer.parseInt(input.nextLine());
            switch (num){
                case 1:
                    showBill(newOrder, orderItemList);
                    break;
                case 0:
                    continue;
            }


                try{
                    do {
                        System.out.println("\n---> Vui lòng xác nhận thanh toán cho đơn hàng!");
                        System.out.println("(Nhập '1' để xác nhận hoặc '2' để thoát)");
                        int letter = Integer.parseInt(input.nextLine());
                        if (letter == 1){
                            System.out.println("\n----- Thanh toán thành công. Xin cảm ơn! -----");
                            orderService.add(newOrder);
                            orderItemService.addMoreOrderItems(orderItemList);
                            CSVUtils.writeData("C:\\Users\\LAPTOP\\IdeaProjects\\casemodule2\\src\\main\\java\\datas\\fruits.txt", fruits);
                            CustomerView.chooseServicesForGuest();
                            break;
                        }
                        if (letter == 2){
                            CustomerView.chooseServicesForGuest();
                            break;
                        }
                    } while (true);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Menu.alert();
                }
        } while (true);
    }

//    public static void modifyOrderItemList(ArrayList<OrderItem> orderItemList) {
//        orderItemList.sort(Comparator.comparing(OrderItem::getDrugName).thenComparingDouble(OrderItem::getDrugContent));
//        for (int i = 0; i < orderItemList.size() - 1; ) {
//            OrderItem orderItem1 = orderItemList.get(i);
//            OrderItem orderItem2 = orderItemList.get(i + 1);
//            if (orderItem1.equals(orderItem2)) {
//                orderItem1.setQuantity(orderItem1.getQuantity() + orderItem2.getQuantity());
//                orderItemList.remove(i + 1);
//                continue;
//            }
//            i++;
//        }
//    }

    public static boolean confirmBuying() {
        do {
            System.out.println("\n---> Vui lòng xác nhận thanh toán cho đơn hàng!");
            System.out.println("(Nhập '1' để xác nhận hoặc '2' để thoát)");
            int letter = Integer.parseInt(Menu.chooseActionByLetter());
            if (letter == 1) return true;
            if (letter == 2) return false;
            Menu.alert();
        } while (true);
    }

    public static void showBill(Order newOrder, List<OrderItem> orderItemList) {
        System.out.println("\nFRUITS BILL --------------------------------------------------------------------------");
        System.out.printf("\n%-20s %-15s %-13s %-15s\n\n", "Tên mặt hàng", "Gía tiền (VND)", "Số lượng", "Tổng tiền (VND)");
        for (OrderItem orderItem : orderItemList) {
            System.out.printf("%-20s %-15s %-13s %-15s\n", orderItem.getDrugName(), ValidateUtils.priceWithDecimal(orderItem.getPricePerPill()),
                    orderItem.getQuantity(), ValidateUtils.priceWithDecimal(orderItem.getTotalPrice()));
        }
        System.out.printf("\n%-52s %s %s\n", "", "Tổng thanh toán (VND):", ValidateUtils.priceWithDecimal(newOrder.getGrandTotal()));
        System.out.println("Thông tin khách hàng:");
        System.out.println("     * Họ và Tên: " + newOrder.getName());
        System.out.println("     * Số điện thoại: " + newOrder.getPhoneNumber());
        System.out.println("     * Địa chỉ: " + newOrder.getAddress());
        System.out.println("Ngày thanh toán: " + ValidateUtils.convertMilliToDate(newOrder.getCreationTime()));
        System.out.println("\n---------------------------------------------------------------------------------------\n");
    }

    public static void showDrugFromGetting(Order newOrder, List<OrderItem> orderItemList) {
        System.out.printf("\n%-20s %-15s %-13s %-15s\n\n", "Tên mặt hàng", "Gía tiền (VND)", "Số lượng", "Tổng tiền (VND)");
        double grandTotal = 0;
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderID(newOrder.getId());
            orderItem.setCreationTime(newOrder.getCreationTime());
            double total = orderItem.getTotalPrice();
            System.out.printf("%-20s %-15s %-13s %-15s\n", orderItem.getDrugName(),
                    ValidateUtils.priceWithDecimal(orderItem.getPricePerPill()), orderItem.getQuantity(), ValidateUtils.priceWithDecimal(total));
            grandTotal += total;
        }
        newOrder.setGrandTotal(grandTotal);
        System.out.printf("\n%-52s %s %s\n", "", "Tổng tiền (VND):", ValidateUtils.priceWithDecimal(grandTotal));
    }

    public static void showDrugsGot(Order newOrder, List<OrderItem> orderItemList) {
        System.out.print("\n---------------------------------------------------------------------------------------");
        showDrugFromGetting(newOrder, orderItemList);
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

    public static void setNewOrderWithDefaultInfo(Order newOder) {
        System.out.println("\n----- Thông tin của bạn đã được thêm -----");
        User currentUser = userService.getUserById(LoginView.getUserID());
        newOder.setId(System.currentTimeMillis() % 1000);
        newOder.setUserId(currentUser.getId());
        newOder.setName(currentUser.getFullName());
        newOder.setPhoneNumber(currentUser.getPhoneNumber());
        newOder.setAddress(currentUser.getAddress());
        newOder.setCreationTime(System.currentTimeMillis());
    }

    public static void setNewOrderWithOtherInfo(Order newOder) {
        System.out.println("\n----- Vui lòng điền đầy đủ những thông tin bên dưới -----");
        System.out.println("\n(Nhập 'ex5' tại bất kì bước nào để kết thúc)");
        String name = enterFullName();
        if (name == null) return;
        String phone = enterPhoneNumber();
        if (phone == null) return;
        String address = enterAddress();
        if (address == null) return;

        newOder.setId(System.currentTimeMillis());
        newOder.setUserId(LoginView.getUserID());
        newOder.setName(name);
        newOder.setPhoneNumber(phone);
        newOder.setAddress(address);
        newOder.setCreationTime(System.currentTimeMillis());
    }

    private static String enterAddress() {
        System.out.println("3. Nhập địa chỉ (Ví dụ: 28 Nguyen Tri Phuong).");
        System.out.print("==> ");
        String address = input.nextLine().trim();
        while (address.equals("")) {
            if (cancelEntering(address)) return null;
            System.out.println("Bắt buộc nhập địa chỉ, vui lòng thử lại!\n");
            System.out.print("==> ");
        }
        return address;
    }

    private static String enterPhoneNumber() {
        String phone;
        System.out.println("2. Nhập SĐT.");
        System.out.println("(Note: Số đầu tiên là '0', số thứ hai từ '1' đến '9' và có độ dài từ 10 - 11 chữ số)");
        System.out.print("==> ");
        while (!ValidateUtils.isPhoneValid(phone = input.nextLine().trim())) {
            if (cancelEntering(phone)) return null;
            System.out.println("SĐT không hợp lệ, vui lòng nhập lại!\n");
            System.out.println("2. Nhập SĐT.");
            System.out.println("(Note: Số đầu tiên là '0', số thứ hai từ '1' đến '9' và có độ dài từ 10 - 11 chữ số)");
            System.out.print("==> ");
        }
        return phone;
    }

    private static String enterFullName() {
        String fullName;
        System.out.println("1. Nhập Họ và Tên (Ví dụ: Nguyen Van An).");
        System.out.print("==> ");
        while (!ValidateUtils.isNameValid(fullName = input.nextLine().trim())) {
            if (cancelEntering(fullName)) return null;
            System.out.println("Họ và Tên không hợp lệ, vui lòng nhập lại!\n");
            System.out.println("1. Nhập Họ và Tên (Ví dụ: Nguyen Van An).");
            System.out.print("==> ");
        }
        return fullName;
    }

    private static boolean cancelEntering(String string) {
        if (Exit.E5.equalsIgnoreCase(string)) {
            System.out.println("\n-----> Đã bỏ qua tiến trình!");
            return true;
        }
        return false;
    }

    public static ArrayList<OrderItem> showHowToGetDrug(Order newOrder, List<Fruit> fruits) {
        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        do {
            System.out.println("\n** 1. Đặt hàng bằng cách tìm kiếm ID **");
            System.out.println("\n** 0. Kết thúc **");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    orderItemList = getDrugsBoughtByID(newOrder, fruits);
                    break;
                }
                if (number == 2) {
                    orderItemList = searchDrugByName(newOrder, fruits);
                    break;
                }
                if (number == 0) {
                    break;
                }
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
        return orderItemList;
    }

    public static ArrayList<OrderItem> searchDrugByName(Order newOrder, List<Fruit> drugs) {
        ArrayList<OrderItem> newOrderItemList = new ArrayList<>();
        do {
            System.out.println("\nNhập tên mặt hàng bạn muốn mua (Nhập '0' để kết thúc). ");
            System.out.print("---> ");
            String drugNameSearch = input.nextLine().trim().toLowerCase();
            if (drugNameSearch.equals("0")) return newOrderItemList;
            List<Fruit> drugListSearch = new ArrayList<>();
            for (Fruit drug : drugs) {
                if (drug.getFruitName().toLowerCase().contains(drugNameSearch)) {
                    drugListSearch.add(drug);
                }
            }
            if (drugListSearch.size() > 0) {
                newOrderItemList.addAll(getDrugsBoughtByID(newOrder, drugListSearch));
                changeQuantityAfterGetting(drugs, drugListSearch);
                continue;
            }
            System.out.printf("\n'%s' không tìm thấy. Vui lòng thử lại!\n", drugNameSearch);
        } while (true);
    }

    public static void changeQuantityAfterGetting(List<Fruit> drugs, List<Fruit> drugListSearch) {
        for (Fruit drugSearch : drugListSearch) {
            for (Fruit drug : drugs) {
                if (drugSearch.getId() == drug.getId()) {
                    drug.setQuantity(drugSearch.getQuantity());
                    break;
                }
            }
        }
    }

    private static ArrayList<OrderItem> getDrugsBoughtByID(Order newOrder, List<Fruit> fruits) {
        ArrayList<OrderItem> drugsBought = new ArrayList<>();
        boolean stopBuying;
        do {
            FruitManagement.showAllDrugs(fruits);

            Fruit availableDrug = getAvailableDrug();

            if (availableDrug == null) return drugsBought;

            int quantityBuy = getQuantityBuy(fruits, availableDrug);

            drugsBought.add(getNewOrderDrug(availableDrug, quantityBuy));
            System.out.printf("\n---> '%s' - %d đã được thêm vào giỏ hàng.\n", availableDrug.getFruitName(), quantityBuy);
            modifyOrderItemList(drugsBought);

            showDrugsGot(newOrder, drugsBought);
            stopBuying = confirmContinuing();
        } while (!stopBuying);
        return drugsBought;
    }

    public static void modifyOrderItemList(ArrayList<OrderItem> orderItemList) {
//        orderItemList.sort(Comparator.comparing(OrderItem::getDrugName).thenComparingDouble(OrderItem::getPricePerPill));
        for (int i = 0; i < orderItemList.size() - 1; ) {
            OrderItem orderItem1 = orderItemList.get(i);
            OrderItem orderItem2 = orderItemList.get(i + 1);
            if (orderItem1.getDrugName().equals(orderItem2.getDrugName())) {
                orderItem1.setQuantity(orderItem1.getQuantity() + orderItem2.getQuantity());
                orderItemList.remove(i + 1);
                continue;
            }
            i++;
        }
    }

    private static Fruit getAvailableDrug() {
        Fruit availableDrug;
        do {
            try {
                System.out.println("\nNhập ID mặt hàng bạn muốn mua (Nhập '0' để kết thúc).");
                System.out.print("---> ");
                int drugBoughtID = Integer.parseInt(input.nextLine());
                if (drugBoughtID == 0) return null;
                availableDrug = fruitService.getFruitById(drugBoughtID);
                if (availableDrug == null) {
                    System.out.println("Không tồn tại ID. Vui lòng nhập lại!\n");
                    continue;
                }
                System.out.printf("Fruit Name: %s \n", availableDrug.getFruitName());
                break;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
        return availableDrug;
    }

    private static int getQuantityBuy(List<Fruit> drugs, Fruit availableDrug) {
        int quantityBuy;
        do {
            try {
                System.out.print("\nNhập số lượng mà bạn muốn mua: ");
                quantityBuy = Integer.parseInt(input.nextLine());
                if (quantityBuy <= 0) {
                    System.out.println("Bạn không thể nhập số bé hơn 0 hoặc bằng 0. Vui lòng nhập lại!\n");
                    continue;
                }
                int oldQuantity = availableDrug.getQuantity();
                if (quantityBuy > oldQuantity) {
                    System.out.println("\n---> Không đủ số lượng. Vui lòng nhập số nhỏ hơn '" + oldQuantity + "'.\n");
                    continue;
                }
                int quantityLeft = oldQuantity - quantityBuy;
                for (int i = 0; i < drugs.size(); i++){
                    if (drugs.get(i).getId() == availableDrug.getId()){
                        availableDrug.setQuantity(quantityLeft);
                        drugs.get(i).setQuantity(quantityLeft);
                    }
                }
                break;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
        return quantityBuy;
    }

    private static boolean confirmContinuing() {
        do {
            System.out.println("\nBạn có muốn đặt hàng thêm không?");
            System.out.println("(Nhập 'y' để tiếp tục hoặc 'n' để dừng lại)");
            String letter = Menu.chooseActionByLetter();
            if (letter.equals("y")) return false;
            if (letter.equals("n")) return true;
            Menu.alert();
        } while (true);
    }

    public static OrderItem getNewOrderDrug(Fruit availableDrug, int quantityBuy) {
        OrderItem newOrderDrug = new OrderItem();
        newOrderDrug.setId(System.currentTimeMillis() % 1000);
        newOrderDrug.setDrugID(availableDrug.getId());
        newOrderDrug.setDrugName(availableDrug.getFruitName());
        newOrderDrug.setPricePerPill(availableDrug.getPricePerPill());
        newOrderDrug.setQuantity(quantityBuy);
        newOrderDrug.setTotalPrice();
        return newOrderDrug;
    }

}

