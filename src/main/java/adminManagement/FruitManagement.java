package adminManagement;




import Services.IFruitService;
import Services.IOderItemService;
import Services.FruitService;
import Services.OrderItemService;
import model.Fruit;
import utils.CSVUtils;
import utils.ValidateUtils;
import views.AdminView;
import views.Exit;
import views.Menu;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FruitManagement {
    private static IFruitService fruitService = new FruitService();
    private static IOderItemService orderItemService = new OrderItemService();
    private static final Scanner input = new Scanner(System.in);
    private static final int YEARS = 2;

    private static void showActionForm() {
        System.out.println("\n --------------- QUẢN LÍ HÀNG HÓA -----------------");
        System.out.println("|                                                     |");
        System.out.println("|        1. Hiển thị danh sách mặt hàng.              |");
        System.out.println("|        2. Sắp xếp theo số lượng.                    |");
        System.out.println("|        3. Sắp xếp theo giá tiền.                    |");
        System.out.println("|        4. Hiển thị hàng đã hết hạn sử dụng.         |");
        System.out.println("|        5. Kiểm tra thông tin hàng hóa bằng ID.      |");
        System.out.println("|        6. Tìm kiếm hàng hóa theo tên.               |");
        System.out.println("|        7. Thêm mặt hàng.                            |");
        System.out.println("|        0. Thoát.                                    |");
        System.out.println("|                                                     |");
        System.out.println(" -----------------------------------------------------");
    }

    public static void chooseActionInMedicineManagement() {
        List<Fruit> drugs = fruitService.getFruits();
        do {
            showActionForm();
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    showDrugsList(drugs, 1);
                    break;
                }
                if (number == 2) {
                    showDrugsList(drugs, 2);
                    break;
                }
                if (number == 3) {
                    showDrugsList(drugs, 3);
                    break;
                }
                if (number == 4) {
                    showDrugsList(drugs, 4);
                    break;
                }
                if (number == 5) {
                    updateDrug();
                    break;
                }
                if (number == 6) {
                    searchDrugByName(drugs);
                    break;
                }
                if (number == 7) {
                    addNewDrug();
                    break;
                }
                if (number == 0) {
                    AdminView.chooseAdminAction();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }
        } while (true);
    }

    public static void showDrugsList(List<Fruit> drugs, int action) throws ParseException {
        switch (action) {
            case 1:
                showAllDrugs(drugs);
                break;
            case 2:
                sortByQuantityASCE(drugs);
                break;
            case 3:
                sortByPricePerPillASCE(drugs);
                break;
            case 4:
                showExpiredDrugs(drugs);
                break;
        }
        chooseNextOperation();
    }

    public static void showAllDrugs(List<Fruit> fruits) {
        System.out.println("\nFRUITS LIST ----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s %-25s %-23s %-17s %-20s \n",
                "ID", "Fruit Name", "Price (VND)", "Quantity (pill)", "Expiration Date");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        for (Fruit fruit : fruits) {
            System.out.printf("%-12s %-25s %-23s %-17s %-20s \n", fruit.getId(), fruit.getFruitName(),
                    ValidateUtils.priceWithDecimal(fruit.getPricePerPill()), fruit.getQuantity(), fruit.getExpirationDate());
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }


    private static void chooseNextOperation() {
        do {
            System.out.println("\n---> Nhập '1' để cập nhật (bằng ID).");
            System.out.println("---> Nhập '2' để xóa (bằng ID).");
            System.out.println("---> Nhập '0' để thoát.");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    updateDrug();
                    break;
                }
                if (number == 2) {
                    removeDrug();
                    break;
                }
                if (number == 0) {
                    chooseActionInMedicineManagement();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static void sortByQuantityASCE(List<Fruit> drugs) {
        drugs.sort((e1, e2) -> Integer.compare(e1.getQuantity() - e2.getQuantity(), 0));
        showAllDrugs(drugs);
    }

    private static void sortByPricePerPillASCE(List<Fruit> drugs) {
        drugs.sort((e1, e2) -> Double.compare(e1.getPricePerPill() - e2.getPricePerPill(), 0));
        showAllDrugs(drugs);
    }

    private static void showExpiredDrugs(List<Fruit> drugs) throws ParseException {
        List<Fruit> expiredDrugs = new ArrayList<>();
        for (Fruit drug : drugs) {
            long expirationTime = ValidateUtils.convertDateToMilli(drug.getExpirationDate());
            long expiredTime = (long) (System.currentTimeMillis() + 2592 * Math.pow(10, 6));
            if (expirationTime <= expiredTime) {
                expiredDrugs.add(drug);
            }
        }
        System.out.println("\n----- Danh sách này bao gồm mặt hàng đã hết hạn hoặc sẽ hết hạn vào tháng tới -----");
        showAllDrugs(expiredDrugs);
    }

    private static void searchDrugByName(List<Fruit> drugs) {
        boolean is;
        do {
            System.out.print("\nNhập tên mặt hàng bạn muốn tìm kiếm: ");
            String searchName = input.nextLine().toLowerCase().trim();
            List<Fruit> drugsListSearch = fruitService.getSearchFruitList(searchName, drugs);
            if (drugsListSearch.size() == 0) {
                System.out.printf("\nKhông tìm thấy với tên '%s'. Bạn có muốn thử lại?\n", searchName);
                do {
                    System.out.println("(Nhập 'y' để thử lại hoặc 'n' để thoát)");
                    try {
                        String letter = Menu.chooseActionByLetter();
                        if (letter.charAt(0) == 'y' && letter.length() == 1) {
                            is = false;
                            break;
                        }
                        if (letter.charAt(0) == 'n' && letter.length() == 1) {
                            is = true;
                            chooseActionInMedicineManagement();
                            break;
                        }
                        Menu.alert();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Menu.alert();
                    }
                } while (true);
                continue;
            }
            showAllDrugs(drugsListSearch);
            is = true;
        } while (!is);
        chooseNextOperation();
    }

    public static void setID(Fruit newDrug) {
        int min = 100000;
        int max = 999999;
        int id;
        do {
            id = (int) Math.floor(Math.random() * (max - min + 1) + min);
        } while (fruitService.isIdExisted(id));
        newDrug.setId(id);
        System.out.println("1. Fruit ID: " + id);
    }

    private static boolean cancelEntering(String string) {
        if (string.equalsIgnoreCase(Exit.E4)) {
            System.out.println("\nTiến trình đã dừng lại!");
            chooseActionInMedicineManagement();
            return true;
        }
        return false;
    }

    private static boolean enterDrugName(Fruit newDrug) {
        do {
            System.out.println("2. Nhập tên mặt hàng: (Ví dụ: Apple). ");
            System.out.print("==> ");
            String drugName = input.nextLine().trim();
            System.out.println();
            if (ValidateUtils.isNameValid(drugName)) {
                newDrug.setFruitName(drugName);
                return false;
            }
            if (cancelEntering(drugName)) return true;
            System.out.println("Tên không hợp lệ. Vui lòng nhập lại!\n");
        } while (true);
    }

//    private static boolean enterDrugContent(Fruit newDrug) {
//        do {
//            try {
//                System.out.println("3. Nhập mô tả cho mặt hàng. ");
//                System.out.print("==> ");
//                String drugContent = input.nextLine();
//                if (cancelEntering(drugContent)) return true;
//                double drugContentValue = Double.parseDouble(drugContent);
//                System.out.println();
//                if (drugContentValue <= 0) {
//                    System.out.println("You can't enter a negative value or '0', please try again!\n");
//                    continue;
//                }
//                newDrug.setFruitContent(drugContentValue);
//                return false;
//            } catch (Exception ex) {
//                Menu.alert();
//            }
//        } while (true);
//    }

    public static boolean checkDuplicateDrug(Fruit newDrug) {
        List<Fruit> drugList = fruitService.getFruits();
        for (Fruit drug : drugList) {
            if (drug.getFruitName().equals(newDrug.getFruitName())) {
                System.out.println("Mặt hàng đã tồn tại. Vui lòng nhập lại!\n");
                return false;
            }
        }
        return true;
    }

    private static boolean enterQuantity(Fruit newDrug) {
        do {
            try {
                System.out.println("4. Nhập số lượng: ");
                System.out.print("==> ");
                String quantity = input.nextLine();
                if (cancelEntering(quantity)) return true;
                int quantityValue = Integer.parseInt(quantity);
                System.out.println();
                if (quantityValue < 0) {
                    System.out.println("Số lượng phải lớn hơn 0. Vui lòng nhập lại!\n");
                    continue;
                }
                newDrug.setQuantity(quantityValue);
                return false;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

//    private static boolean enterDosageForm(Fruit newDrug) {
//        do {
//            try {
//                System.out.print("5. Enter dosage form (Choose '1' or '2').\n");
//                System.out.printf("%-10s %-15s\n", " ", "1. Tablet.");
//                System.out.printf("%-10s %-15s\n", " ", "2. Capsule.");
//                System.out.print("==> ");
//                String value = input.nextLine();
//                if (cancelEntering(value)) return true;
//                int number = Integer.parseInt(value);
//                if (number == 1) {
//                    newDrug.setDosageForm("tablet");
//                    return false;
//                }
//                if (number == 2) {
//                    newDrug.setDosageForm("capsule");
//                    return false;
//                }
//                Menu.alert();
//            } catch (Exception ex) {
//                Menu.alert();
//            }
//        } while (true);
//    }

//    private static boolean enterUsage(Fruit newDrug) {
//        System.out.println("6. Enter common usage (Example: fever, cough).");
//        System.out.print("==> ");
//        String usage = input.nextLine().trim();
//        if (cancelEntering(usage)) return true;
//        newDrug.setUsage(usage);
//        return false;
//    }
//
//    private static boolean enterDosagePerDay(Fruit newDrug) {
//        System.out.println("7. Enter dosage per day (Example: 1 Morning, 1 Night).");
//        System.out.print("==> ");
//        String dosagePerDay = input.nextLine().trim();
//        if (cancelEntering(dosagePerDay)) return true;
//        newDrug.setDosagePerDay(dosagePerDay);
//        return false;
//    }

//    private static boolean enterTotalDosage5Days(Fruit newDrug) {
//        do {
//            try {
//                System.out.println("8. Enter total dosage in 5 days.");
//                System.out.print("==> ");
//                String totalDosage5Days = input.nextLine();
//                if (cancelEntering(totalDosage5Days)) return true;
//                int totalDosage5DaysValue = Integer.parseInt(totalDosage5Days);
//                if (totalDosage5DaysValue <= 0) {
//                    System.out.println("You can't enter a negative value, please try again!\n");
//                    continue;
//                }
//                newDrug.setTotalDosage5Days(totalDosage5DaysValue);
//                return false;
//            } catch (Exception ex) {
//                Menu.alert();
//            }
//        } while (true);
//    }

    private static boolean enterPricePerPill(Fruit newDrug) {
        do {
            try {
                System.out.println("9. Nhập giá tiền:");
                System.out.print("==> ");
                String pricePerPill = input.nextLine();
                if (cancelEntering(pricePerPill)) return true;
                double pricePerPillValue = Double.parseDouble(pricePerPill);
                if (pricePerPillValue <= 0) {
                    System.out.println("Số tiền phải lớn hơn 0. Vui lòng nhập lại!\n");
                    continue;
                }
                newDrug.setPricePerPill(pricePerPillValue);
                return false;
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    private static boolean enterProductionDate(Fruit newDrug) throws ParseException {
        do {
            System.out.println("10. Nhập ngày nhập kho (Ví dụ: 12/04/2021).");
            System.out.print("==> ");
            String productionDate = input.nextLine().trim();
            if (cancelEntering(productionDate)) return true;
            System.out.println();
            if (!checkProductionDate(productionDate)) continue;
            if (ValidateUtils.isDateValid(productionDate)) {
                newDrug.setProductionDate(productionDate);
                return false;
            }
            System.out.println("Ngày không hợp lệ. Vui lòng nhập lại!\n");
        } while (true);
    }

    private static boolean checkProductionDate(String productionDate) throws ParseException {
        long productionTime = ValidateUtils.convertDateToMilli(productionDate);
        long currentTime = System.currentTimeMillis();
        long range = Math.abs(currentTime - productionTime);
        if (productionTime > currentTime) {
            System.out.println("\nNgày nhập kho không thể lớn hơn ngày hiện tại '" + ValidateUtils.convertMilliToDate(currentTime) + "'\n");
            return false;
        }
        if (TimeUnit.DAYS.convert(range, TimeUnit.MILLISECONDS) / 365.0 > 2) {
            long years2Milli = (long) YEARS * 1000 * 60 * 60 * 24 * 365;
            System.out.println("\nMặt hàng produced over " + YEARS + " years ago from today MUST NOT be added!");
            System.out.println("(Bạn đã nhập ngày lớn hơn '" + ValidateUtils.convertMilliToDate(currentTime - years2Milli) + "')\n");
            return false;
        }
        return true;
    }

    private static boolean enterExpirationDate(Fruit newDrug) throws ParseException {
        do {
            System.out.println("11. Nhập ngày hết hạn (Ví dụ: 12/04/2022) ");
            System.out.print("==> ");
            String expirationDate = input.nextLine().trim();
            if (cancelEntering(expirationDate)) return true;
            System.out.println();
            if (!ValidateUtils.isDateValid(expirationDate)) {
                System.out.println("Ngày không hợp lệ, vui lòng thử lại!\n");
                continue;
            }
            long expirationDateToMilli = ValidateUtils.convertDateToMilli(expirationDate);
            long productionDateToMilli = ValidateUtils.convertDateToMilli(newDrug.getProductionDate());
            if (expirationDateToMilli <= productionDateToMilli) {
                System.out.println("Ngày hết hạn không thể lớn hơn ngày nhập kho, vui lòng thử lại!\n");
                continue;
            }
            newDrug.setExpirationDate(expirationDate);
            return false;
        } while (true);
    }

    private static boolean enterNote(Fruit newDrug) {
        System.out.println("12. Nhập ghi chú (Ví dụ: bảo quản lạnh).");
        System.out.print("==> ");
        String note = input.nextLine();
        if (cancelEntering(note)) return true;
        newDrug.setNote(note);
        return false;
    }

    public static void showConfirmForm() {
        System.out.println("\nXác nhận bạn muốn thêm mặt hàng với những thông tin dưới đây!");
        System.out.println("1. Xác nhận.");
        System.out.println("2. Đóng.\n");
    }

    public static void confirmAddingNewDrug(Fruit newDrug) {
        do {
            try {
                showConfirmForm();
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    fruitService.add(newDrug);
                    System.out.println("\nMặt hàng mới đã thêm thành công!");
                    chooseActionInMedicineManagement();
                    break;
                }
                if (number == 2) {
                    chooseActionInMedicineManagement();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    public static void showDrugDetail(Fruit drug) {
        System.out.println("\nDRUG DETAIL ------------------------------------------\n");
        System.out.printf("%-30s %-12d\n", "1. ID:", drug.getId());
        System.out.printf("%-30s %-12s\n", "2. Fruit Name:", drug.getFruitName());
//        System.out.printf("%-30s %-12s\n", "3. Drug Content (mg):", drug.getFruitContent());
//        System.out.printf("%-30s %-12s\n", "4. Quantity (pill):", drug.getQuantity());
//        System.out.printf("%-30s %-12s\n", "5. Dosage Form:", drug.getDosageForm());
//        System.out.printf("%-30s %-12s\n", "6. Usage:", drug.getUsage());
//        System.out.printf("%-30s %-12s\n", "7. Dosage per Day:", drug.getDosagePerDay());
//        System.out.printf("%-30s %-12s\n", "8. Total dosage in 5 days:", drug.getTotalDosage5Days());
        System.out.printf("%-30s %-12s\n", "3. Price (VND):", ValidateUtils.priceWithDecimal(drug.getPricePerPill()));
        System.out.printf("%-30s %-12s\n", "4. Production Date:", drug.getProductionDate());
        System.out.printf("%-30s %-12s\n", "5. Expiration Date:", drug.getExpirationDate());
        System.out.printf("%-30s %-12s\n", "6. Note:", drug.getNote());
        System.out.println("\n-----------------------------------------------------\n");
    }

    public static Fruit getExistedDrug() {
        do {
            try {
                System.out.println(" Nhập ID : ");
                int id = Integer.parseInt(input.nextLine());
                if (id == 0) {
                    chooseActionInMedicineManagement();
                    break;
                }
                if (!fruitService.isIdExisted(id)) {
                    System.out.println("ID không tồn tại, vui lòng thử lại hoặc nhập '0' để quay lại.");
                    continue;
                }
                if (orderItemService.isItemOrdered(id)) {
                    System.out.println("Bạn không thể xóa mặt hàng này vì nó đã được đặt hàng.");
                    System.out.println("Vui lòng nhập ID khác!");
                    continue;
                }
                return fruitService.getFruitById(id);
            } catch (Exception ex) {
              ex.printStackTrace();
            }
        } while (true);
        return null;
    }

    public static void removeDrug() {
        Fruit fruit = getExistedDrug();
        do {
            System.out.printf("\nXác nhận bạn muốn xóa mặt hàng %s '%s'.\n", fruit.getId(), fruit.getFruitName());
            System.out.println("1. Xác nhận xóa.");
            System.out.println("2. Đóng.");
            try {
                int number = Menu.chooseActionByNumber();
                if (number == 1) {
                    System.out.printf("\nMặt hàng '%s' đã xóa thành công!\n", fruit.getId());
                    fruitService.remove(fruit);

//                    CSVUtils.writeData("C:\\Users\\LAPTOP\\IdeaProjects\\casemodule2\\src\\main\\java\\datas\\fruits.txt",fruitService.getFruits());
                    chooseActionInMedicineManagement();
                    break;
                }
                if (number == 2) {
                    chooseActionInMedicineManagement();
                    break;
                }
                Menu.alert();
            } catch (Exception ex) {
                Menu.alert();
            }
        } while (true);
    }

    public static void updateDrug() {
        Fruit oldDrug = null;
        do {
            try {
                System.out.println(" Nhập ID : ");
                int id = Integer.parseInt(input.nextLine());
                if (id == 0) {
                    chooseActionInMedicineManagement();
                    break;
                }
                if (!fruitService.isIdExisted(id)) {
                    System.out.println("ID không tồn tại, vui lòng thử lại hoặc nhập '0' để quay lại.");
                    continue;
                }
//                if (orderItemService.isItemOrdered(id)) {
//                    System.out.println("Bạn không thể xóa mặt hàng này vì nó đã được đặt hàng.");
//                    System.out.println("Vui lòng nhập ID khác!");
//                    continue;
//                }
                oldDrug = fruitService.getFruitById(id);
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } while (true);
    
        boolean is = true;
        int number = -1;
        do {
            showDrugDetail(oldDrug);
            showChangeStatus(number);
            System.out.println("Chọn thông tin bạn muốn cập nhật.");
            System.out.println("NOTE: You CANNOT update drug ID. Please enter a value in range '2-12'!\n");
            System.out.println("---> Nhập '13' để xác nhận bạn muốn cập nhật với những thông tin bên dưới.\n");
            System.out.println("---> Nhập '0' để dừng.");
            System.out.println("---> NOTE: Nhập '" + Exit.E4 + "' để dừng tại bước (2-12).\n");
            try {
                number = Menu.chooseActionByNumber();
                switch (number) {
                    case 2:
                        is = enterDrugName(oldDrug);
                        break;
//                    case 3:
//                        is = enterDrugContent(oldDrug);
//                        break;
//                    case 4:
//                        is = enterQuantity(oldDrug);
//                        break;
//                    case 5:
//                        is = enterDosageForm(oldDrug);
//                        break;
//                    case 6:
//                        is = enterUsage(oldDrug);
//                        break;
//                    case 7:
//                        is = enterDosagePerDay(oldDrug);
//                        break;
//                    case 8:
//                        is = enterTotalDosage5Days(oldDrug);
//                        break;
                    case 3:
                        is = enterPricePerPill(oldDrug);
                        break;
                    case 4:
                        is = enterProductionDate(oldDrug);
                        break;
                    case 5:
                        is = enterExpirationDate(oldDrug);
                        break;
                    case 6:
                        is = enterNote(oldDrug);
                        break;
                    case 13:
                        is = true;
                        fruitService.update(oldDrug);
                        System.out.println("\n-----> Đã cập nhật thành công!\n");
                        chooseActionInMedicineManagement();
                        break;
                    case 0:
                        is = true;
                        chooseActionInMedicineManagement();
                        break;
                    default:
                        Menu.alert();
                        is = false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Menu.alert();
            }
        } while (!is);
    }

    private static void showChangeStatus(int number) {
        switch (number) {
            case 2:
                System.out.println("---  Tên mặt hàng đã được cập nhật.\n");
                break;
            case 3:
                System.out.println("---  Số lượng đã được cập nhật.\n");
                break;
            case 4:
                System.out.println("---  Gía tiền đã được cập nhật.\n");
                break;
            case 5:
                System.out.println("---  Ngày nhập kho đã được cập nhật.\n");
                break;
            case 6:
                System.out.println("---  Ngày hết hạn đã được cập nhật.\n");
                break;
            case 7:
                System.out.println("---  Lưu ý đã được cập nhật.\n");
                break;
//            case 8:
//                System.out.println("---  Total dosage in 5 days has been changed.\n");
//                break;
//            case 9:
//                System.out.println("---  Price has been changed.\n");
//                break;
//            case 10:
//                System.out.println("---  Production date has been changed.\n");
//                break;
//            case 11:
//                System.out.println("---  Expiration date has been changed.\n");
//                break;
//            case 12:
//                System.out.println("---  Note has been changed.\n");
//                break;
        }
    }

    public static void addNewDrug() {
        System.out.println("\n\n----- Thêm mặt hàng mới -----\n");
        Fruit newDrug = new Fruit();
        try {
            setID(newDrug);
            do {
                enterDrugName(newDrug);
//                enterDrugContent(newDrug);
            } while (!checkDuplicateDrug(newDrug));
            enterQuantity(newDrug);
//            enterDosageForm(newDrug);
//            enterUsage(newDrug);
//            enterDosagePerDay(newDrug);
//            enterTotalDosage5Days(newDrug);
            enterPricePerPill(newDrug);
            enterProductionDate(newDrug);
            enterExpirationDate(newDrug);
            enterNote(newDrug);
            confirmAddingNewDrug(newDrug);
        } catch (Exception ex) {
            Menu.alert();
        }
    }
}

