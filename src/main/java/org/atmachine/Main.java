package org.atmachine;

import java.text.DecimalFormat;
import java.util.Scanner;

import org.atmachine.bank.BankAccountModel;
import org.atmachine.database.DatabaseExecutor;

public class Main {
    private static BankAccountModel model;
    private static String cardNum;
    private static short passcode;

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        if (sync()) System.out.println("Подключение успешно.");
        else {
            System.out.println("Ошибка. База данных недоступна.");
            return;
        }

        System.out.println(
                "Добро пожаловать в банкомат TheBestBankOfTheWorld банка. Введите номер карты и пин-код."
        );

        insertCard();

        if (model == null) {
            System.out.println(
                    "Неверный номер карты или пин-код. Завершение сеанса."
            );

            DatabaseExecutor.getInstance().closeConnection();
            return;
        }

        System.out.println("Добро пожаловать, " + model.getAccountHolderName() + " " + model.getAccountHolderSurname());

        getAction();
    }

    private static void insertCard() {
        while (true) {
            try {
                System.out.print("Введите номер карты: ");
                cardNum = SCANNER.nextLine();

                System.out.print("Введите пин-код: ");
                passcode = SCANNER.nextShort();

                System.out.println("Загрузка...");
                Thread.sleep(300);

                break;
            } catch (Exception ex) {
                SCANNER.nextLine();
                System.out.println("Данные введены неверно. Повторите попытку.");
            }
        }

        model = DatabaseExecutor.getInstance().getDataFromDb(cardNum, passcode);
    }

    private static boolean sync() throws Exception {
        Class.forName("org.sqlite.JDBC");

        System.out.println("Настройка системы.");
        Thread.sleep(100);

        System.out.println("Подключение к базе данных...");
        Thread.sleep(200);

        return DatabaseExecutor.getInstance().openConnection();
    }

    private static void getAction() {
        int action;

        while (true) {
            System.out.println("1) Посмотреть данные\n2) Пополнить баланс счёта\n3) Снять деньги со счёта\n4) Выйти");

            action = SCANNER.nextInt();
            if (action < 1 || action > 4) {
                System.out.println("Неверное действие. Повторите попытку");
            }
            else {
                action(action);
                break;
            }
        }
    }

    private static void action(int action) {
        switch (action) {
            case 1 -> System.out.println(model);
            case 2 -> {
                while (true) {
                    System.out.print("Введите сумму, которую хотите положить: ");
                    double newAm = SCANNER.nextInt();

                    if (newAm <= 0) {
                        continue;
                    }

                    DatabaseExecutor.getInstance().updateAmountInDb(
                            cardNum,
                            passcode,
                            Double.parseDouble(
                                    new DecimalFormat("#.##")
                                            .format(
                                                    model.getAccountAmount() - newAm
                                            )
                            )
                    );

                    System.out.println("Успешно.");
                    break;
                }
            }
            case 3 -> {
                while (true) {
                    System.out.print("Введите сумму, которую хотите снять: ");
                    double getAm = SCANNER.nextDouble();

                    if (getAm <= 0 || getAm > model.getAccountAmount()) {
                        System.out.println("Невозможно снять такую сумму. Повторите попытку.");
                        continue;
                    }

                    DatabaseExecutor.getInstance().updateAmountInDb(
                            cardNum,
                            passcode,
                            Double.parseDouble(
                                    new DecimalFormat("#.##")
                                            .format(
                                                    model.getAccountAmount() - getAm
                                            )
                            )
                    );
                    System.out.println("Заберите Ваши деньги.");
                    break;
                }
            }
            case 4 -> System.out.println("До свидания, " + model.getAccountHolderName() + " " + model.getAccountHolderName());
        }
    }
}
