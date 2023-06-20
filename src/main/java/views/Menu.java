package views;



import java.util.Scanner;

public class Menu {

    public static void showEntrance() {
        System.out.println("\n ____________________________________________ ");
        System.out.println("|        << VH Fruits Shop Online >>         |");
        System.out.println("|                                            |");
        System.out.println("|    1. Đăng nhập:                           |");
        System.out.println("|    2. Đăng ký tài khoản mới:               |");
        System.out.println("|                                            |");
        System.out.println("|____________________________________________|");
    }

    public static int chooseActionByNumber() {
        Scanner input = new Scanner(System.in);
        System.out.print("\n ==> ");
        return Integer.parseInt(input.nextLine());
    }

    public static String chooseActionByLetter() {
        Scanner input = new Scanner(System.in);
        do {
            System.out.print("\n ==> ");
            String letter = input.nextLine();
            if (letter.length() == 1) return letter;
            Menu.alert();
        } while (true);
    }

    public static void alert() {
        System.out.println("!!!!! INVALID input. Please try again !!!!!\n");
    }

    public static void chooseInEntrance() {
        do {
            showEntrance();
            try {
                int number = chooseActionByNumber();
                if (number == 1) {
                    LoginView.signIn();
                    break;
                }
                if (number == 2) {
                    SignUpView newRegister = new SignUpView();
                    newRegister.createAccount();
                    break;
                }
                alert();
            } catch (Exception io) {
                alert();
            }
        } while (true);
    }

}

