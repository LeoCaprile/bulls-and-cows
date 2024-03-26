package bullscows;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();
    final static int MAX_SECRET_CODE_LENGTH = 36;

    public static void main(String[] args) {
        int maxLength;
        int charRange;
        int turn = 1;
        boolean playerWin = false;

        String secretCode;


        try {
            System.out.println("Please, enter the secret code's length:");
            maxLength = sc.nextInt();
            System.out.println("Please, enter the secret code's length:");
            charRange = sc.nextInt();

            if (maxLength == 0 || maxLength > charRange) {
                throw new MissingUniqueSymbolsException("Error: it's not possible to generate a code with a length of " +
                        maxLength +
                        " with " +
                        charRange +
                        " unique symbols.");
            } else if (charRange > MAX_SECRET_CODE_LENGTH) {
                throw new OutOfSymbolsException("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            }

            System.out.println("The secret is prepared: " + "*".repeat(maxLength) + " (0-9" + getCharRange(charRange) + ").");
            System.out.println("Okay, let's start a game!");
            secretCode = generateSecretCode(maxLength, charRange);
            while (!playerWin) {
                System.out.println("Turn " + turn + ":");
                String guess = sc.next();
                playerWin = compareSecretCodeWithUserInput(guess, secretCode);
                turn++;
            }

            System.out.println("Congratulations! You guessed the secret code.");
        } catch (InputMismatchException e) {
            System.out.println("Error: input provided isn't a valid number");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


    }

    public static String generateSecretCode(int userLength, int charRange) {
        StringBuilder secretCode = new StringBuilder();

        if (userLength <= MAX_SECRET_CODE_LENGTH) {
            while (secretCode.length() < userLength) {
                Integer randomDigit = getRandomDigit();

                if (charRange > 10) {
                    Character randomLetter = getRandomLowerCaseLetter(charRange);

                    if (randomDigit % 2 == 0) {
                        if (secretCode.indexOf(randomDigit.toString()) == -1) {
                            secretCode.append(randomDigit);
                        } else if (secretCode.indexOf(randomLetter.toString()) == -1) {
                            secretCode.append(getRandomLowerCaseLetter(charRange));
                        }
                    }
                } else {
                    if (secretCode.indexOf(randomDigit.toString()) == -1) {
                        secretCode.append(randomDigit);
                    }
                }

            }
            return secretCode.toString();
        } else {
            System.out.println("Error: can't generate a secret number with a length of " + userLength + " because there aren't enough unique digits.");
            return "";
        }
    }

    public static Integer getRandomDigit() {
        return rand.nextInt(9);
    }

    public static Character getRandomLowerCaseLetter(int charRange) {
        int charBound = charRange - 10 + 'a';
        return (char) rand.nextInt('a', charBound);
    }


    public static String getCharRange(int charRange) {
        StringBuilder sb = new StringBuilder(", a-");
        if (charRange > 10) {
            char lastChar = (char) (charRange - 11 + 'a');
            sb.append(lastChar);
            return sb.toString();
        } else {
            return "";
        }
    }

    public static boolean compareSecretCodeWithUserInput(String guess, String secretCode) {
        String[] playerGuess = guess.split("");

        int cows = 0;
        int bulls = 0;

        for (int i = 0; i < secretCode.length(); i++) {
            // Get the player guess number of this iteration
            String playerGuessNum = playerGuess[i];
            // search if player guess nums are in secretCode.
            int playerGuessNumPositionOnSecretCode = secretCode.indexOf(playerGuessNum);
            // if the num of player guess is in the same position
            if (playerGuessNumPositionOnSecretCode == i) {
                bulls++;
                // If the guessed number is not equal to -1 (not found on secret code)
            } else if (playerGuessNumPositionOnSecretCode != -1) {
                cows++;
            }
        }

        printResult(bulls, cows);

        return bulls == secretCode.length();
    }

    public static void printResult(int bulls, int cows) {

        String output = "";

        if (bulls > 0) {
            output += bulls + " bull(s)";
            if (cows > 0) {
                output += " " + cows + " cow(s)";
            }
        }

        if (output.isEmpty() && cows > 0) {
            output += cows + " cow(s)";
        }

        if (output.isEmpty()) {
            output += "None";
        }

        System.out.println("Grade: " + output);
    }
}
