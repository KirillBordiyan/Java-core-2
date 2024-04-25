package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final char EMPTY_DOT = '*';
    private static final char HUMAN_DOT = 'x';
    private static final char AI_DOT = '0';
    private static final char WIN_DOT = 3;
    private static int turns = 0;
    private static int crossX;
    private static int crossY;
    private static char[][] crossTable;
    private static Scanner scanner = new Scanner(System.in);
    private static Random rnd = new Random();
    private static final String HUMAN_WIN = "Крестики выиграли! Поздравляем пользователя :)";
    private static final String AI_WIN = "Нолики выиграли! Поздравляем компьютер :)";
    private static final String DRAW = "Увы, ничья";


    public static void main(String[] args) {

        initStartData();
        showTable();

        if(Math.min(crossX, crossY) < WIN_DOT){
            System.out.println("Выигрышная комбинация больше диагонали поля\nИгры не будет");
            return;
        }

        while (true){
            humanTurn();
            aiTurn();
            if(turns >= (WIN_DOT * 2) - 1){
                if(checkWin()){
                    break;
                }
                if(isGameDraw()){
                    break;
                }
            }
            showTable();
        }
        showTable();
        System.out.println("Игра окончена!");
    }

    /**
     * Инициализация параметров и поля
     */
    static void initStartData() {
        crossX = 3;
        crossY = 3;
        crossTable = new char[crossX][crossY];

        for (int x = 0; x < crossX; x++) {
            for (int y = 0; y < crossY; y++) {
                crossTable[x][y] = EMPTY_DOT;
            }
        }
    }


    /**
     * Демонстрация игрового поля
     */
    static void showTable() {
        System.out.print("+");
        for (int y = 0; y < crossY; y++) {
            System.out.print("-" + (y + 1));
        }
        System.out.println("-");

        for (int x = 0; x < crossX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < crossY; y++) {
                System.out.print(crossTable[x][y] + "|");
            }
            System.out.println();
        }
        System.out.println("--------");
    }


    /**
     * Ход компьютера
     */
    static void aiTurn() {
        if (turns == crossX * crossY) return;
        int aiX;
        int aiY;

        do {
            aiX = rnd.nextInt(crossX);
            aiY = rnd.nextInt(crossY);
        } while (!isEmptyDot(aiX, aiY));

        crossTable[aiX][aiY] = AI_DOT;
        turns++;
    }


    /**
     * Ход пользователя
     */
    static void humanTurn() {
        if (turns == crossX * crossY) return;
        int humanX;
        int humanY;

        do {
            System.out.printf("Введите координаты в пределах игрового поля (до %d по Х; до %d по У) \n",crossX, crossY );
            humanX = scanner.nextInt() - 1;
            humanY = scanner.nextInt() - 1;


        } while (!isValidInput(humanX, humanY));

        crossTable[humanX][humanY] = HUMAN_DOT;
        turns++;
    }


    /**
     * Объединенный метод проверки подходящего инпута
     *
     * @param x ось X
     * @param y ось Y
     * @return false, если данные не прошли проверку
     */
    static boolean isValidInput(int x, int y) {
        if (x >= 0 && x < crossX && y >= 0 && y < crossY) {
            return isEmptyDot(x, y);
        }
        return false;
    }


    /**
     * Метод проверки пустая ячейка или нет
     */
    static boolean isEmptyDot(int x, int y) {
        return crossTable[x][y] == EMPTY_DOT;
    }


    /**
     * Проверка на ничью
     */
    static boolean isGameDraw() {
        for (int x = 0; x < crossX; x++) {
            for (int y = 0; y < crossY; y++) {
                if (crossTable[x][y] == EMPTY_DOT) {
                    return false;
                }
            }
        }
        System.out.println(DRAW);
        return true;
    }


    /**
     * Метод совмещающий проверки вариантов победы
     * @return true/false в зависимости, есть ли выигрышная комбинация
     */
    static boolean checkWin() {
        int humanScore = 0;
        int aiScore = 0;

        boolean winLessThenRowLength = WIN_DOT < crossX;
        boolean winLessThenColLength = WIN_DOT < crossY;

        int minSideSize = Math.min(crossX, crossY);

        return checkRows(humanScore, aiScore, winLessThenRowLength) ||
                checkCol(humanScore, aiScore, winLessThenColLength) ||
                checkMainMid(humanScore, aiScore, winLessThenRowLength, winLessThenColLength, minSideSize) ||
                checkSupMid(humanScore, aiScore, winLessThenRowLength, winLessThenColLength, minSideSize);
    }


    /**
     * Проверка комбинации по строкам
     * @param humanScore суммарная комбинация человека
     * @param aiScore суммарная комбинация ИИ
     * @param winLessThenRowLength является ли выигрышная комбинация меньше, чем размер строки
     * @return true/false в зависимости, есть ли выигрышная комбинация по строке
     */
    static boolean checkRows(int humanScore, int aiScore, boolean winLessThenRowLength) {
        char firstElem;
        char currentElem;

        for (int x = 0; x < crossX; x++) {
            firstElem = crossTable[x][0];
            for (int y = 0; y < crossY; y++) {
                if (!winLessThenRowLength && firstElem == EMPTY_DOT) break;
                currentElem = crossTable[x][y];
                switch (currentElem) {
                    case HUMAN_DOT -> {
                        humanScore++;
                        aiScore = 0;
                    }
                    case AI_DOT -> {
                        aiScore++;
                        humanScore = 0;
                    }
                }

                if (humanScore == WIN_DOT) {
                    System.out.println(HUMAN_WIN);
                    return true;
                }
                if (aiScore == WIN_DOT) {
                    System.out.println(AI_WIN);
                    return true;
                }

            }
            humanScore = 0;
            aiScore = 0;
        }
        return false;
    }


    /**
     * Проверка комбинации по столбцам
     * @param humanScore суммарная комбинация человека
     * @param aiScore суммарная комбинация ИИ
     * @param winLessThenColLength является ли выигрышная комбинация меньше, чем размер столбца
     * @return true/false в зависимости, есть ли выигрышная комбинация по столбцу
     */
    static boolean checkCol(int humanScore, int aiScore, boolean winLessThenColLength){
        char firstElem;
        char currentElem;

        for (int y = 0; y < crossX; y++) {
            firstElem = crossTable[0][y];
            for (int x = 0; x < crossX; x++) {
                if (!winLessThenColLength && firstElem == EMPTY_DOT) break;
                currentElem = crossTable[x][y];
                switch (currentElem) {
                    case HUMAN_DOT -> {
                        humanScore++;
                        aiScore = 0;
                    }
                    case AI_DOT -> {
                        aiScore++;
                        humanScore = 0;
                    }
                }

                if (humanScore == WIN_DOT) {
                    System.out.println(HUMAN_WIN);
                    return true;
                }
                if (aiScore == WIN_DOT) {
                    System.out.println(AI_WIN);
                    return true;
                }
            }
            humanScore = 0;
            aiScore = 0;
        }
        return false;
    }


    /**
     * Проверка комбинации по главной диагонали (левый верхний -> правый нижний угол)
     * @param humanScore суммарная комбинация человека
     * @param aiScore суммарная комбинация ИИ
     * @param winLessThenRowLength является ли выигрышная комбинация меньше, чем размер строки
     * @param winLessThenColLength является ли выигрышная комбинация меньше, чем размер столбца
     * @param minSideSize размер минимальной стороны
     * @return true/false в зависимости, есть ли выигрышная комбинация по главной диагонали
     */
    static boolean checkMainMid(int humanScore, int aiScore,
                                boolean winLessThenRowLength, boolean winLessThenColLength,
                                int minSideSize) { // из левого верхнего в правый нижний
        char firstElem;
        char currentElem;

        for (int x = 0; x < minSideSize; x++) {
            firstElem = crossTable[0][0];
            if ((!winLessThenColLength || !winLessThenRowLength) && firstElem == EMPTY_DOT) break;
            currentElem = crossTable[x][x];
            switch (currentElem) {
                case HUMAN_DOT -> {
                    humanScore++;
                    aiScore = 0;
                }
                case AI_DOT -> {
                    aiScore++;
                    humanScore = 0;
                }
            }
            if (humanScore == WIN_DOT) {
                System.out.println(HUMAN_WIN);
                return true;
            }
            if (aiScore == WIN_DOT) {
                System.out.println(AI_WIN);
                return true;
            }
        }
        return false;
    }


    /**
     * Проверка комбинации по побочной диагонали (левый нижний -> правый верхний угол)
     * @param humanScore суммарная комбинация человека
     * @param aiScore суммарная комбинация ИИ
     * @param winLessThenRowLength является ли выигрышная комбинация меньше, чем размер строки
     * @param winLessThenColLength является ли выигрышная комбинация меньше, чем размер столбца
     * @param minSideSize размер минимальной стороны
     * @return true/false в зависимости, есть ли выигрышная комбинация по побочной диагонали
     */
    static boolean checkSupMid(int humanScore, int aiScore,
                            boolean winLessThenRowLength, boolean winLessThenColLength,
                            int minSideSize){ //из левого нижнего в правый верхний
        char firstElem;
        char currentElem;

        for (int x = 0; x < minSideSize; x++) {
            firstElem = crossTable[minSideSize - 1][0];
            if ((!winLessThenColLength || !winLessThenRowLength) && firstElem == EMPTY_DOT) break;
            currentElem = crossTable[minSideSize - x - 1][x];
            switch (currentElem) {
                case HUMAN_DOT -> {
                    humanScore++;
                    aiScore = 0;
                }
                case AI_DOT -> {
                    aiScore++;
                    humanScore = 0;
                }
            }
            if (humanScore == WIN_DOT) {
                System.out.println(HUMAN_WIN);
                return true;
            }
            if (aiScore == WIN_DOT) {
                System.out.println(AI_WIN);
                return true;
            }
        }
        return false;
    }

}
